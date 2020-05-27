package it.qbteam.stalkerapp.model.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import it.qbteam.stalkerapp.model.data.User;

public class Firebase extends Service {

    private static final String PACKAGE_NAME = "it.qbteam.stalkerapp.model.service.Firebase";
    public static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";
    public static final String EXTRA_FIREBASE = PACKAGE_NAME + ".firebase";
    private static final String TAG = Firebase.class.getSimpleName();
    private Handler mServiceHandler;
    private int mInterval = 5000;
    private User user;

    @Override
    public void onCreate() {

        user=new User();
        mServiceHandler= new Handler();
        startRepeatingTask();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Runnable mTokenCheck = new Runnable() {
        @Override
        public void run() {
            try {
                getFireBaseToken();
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mServiceHandler.postDelayed(mTokenCheck, mInterval);
            }
        }
    };

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public Firebase getService() {
            return Firebase.this;
        }

    }

    public void getFireBaseToken(){

        if (FirebaseAuth.getInstance().getCurrentUser() != null){

            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            mUser.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                user=new User(task.getResult().getToken(),FirebaseAuth.getInstance().getCurrentUser().getUid());
                                // Notify anyone listening for broadcasts about the new location.
                                Intent intent = new Intent(ACTION_BROADCAST);
                                intent.putExtra(EXTRA_FIREBASE, (Parcelable) user);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                                System.out.println("TOKEN CREATO CON IL SERVICE:"+task.getResult().getToken()+"UID CREATO:"+FirebaseAuth.getInstance().getCurrentUser().getUid());
                            } else {
                                // Handle error -> task.getException();
                            }
                        }
                    });
        }
    }
    public void stopFirebaseService(){
        stopSelf();
    }
    void startRepeatingTask() {
        mTokenCheck.run();
    }

    void stopRepeatingTask() {
        mServiceHandler.removeCallbacks(mTokenCheck);
    }
    @Override
    public void onDestroy() {

        super.onDestroy();
        stopRepeatingTask();
    }

}
