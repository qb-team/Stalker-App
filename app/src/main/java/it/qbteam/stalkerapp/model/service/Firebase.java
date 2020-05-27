package it.qbteam.stalkerapp.model.service;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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



