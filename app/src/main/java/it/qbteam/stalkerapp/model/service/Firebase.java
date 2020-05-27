package it.qbteam.stalkerapp.model.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.io.IOException;

import it.qbteam.stalkerapp.model.data.User;
import it.qbteam.stalkerapp.model.tracking.TrackingStalker;

public class Firebase extends IntentService {
    public static final String NOTIFICATION = "it.qbteam.stalkerapp.model.service";


    public Firebase() {
        super("firebaseService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
  if (FirebaseAuth.getInstance().getCurrentUser() != null){
      FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
           mUser.getIdToken(true).addOnCompleteListener(task -> {
               if (task.isSuccessful()) {

                   // Notify anyone listening for broadcasts about the new user's credential.
                   Intent intent1 = new Intent(NOTIFICATION);
                   intent1.putExtra("token", task.getResult().getToken());
                   intent1.putExtra("uID",FirebaseAuth.getInstance().getCurrentUser().getUid());
                   sendBroadcast(intent1);
               }
               else {
               // Handle error -> task.getException();
               }
           });
        }
    }
}



