package it.qbteam.stalkerapp.model.data;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserFirebase {

    //Metodo (di Firebase) che permette di effettuare il login sul server Firebase
   // @Override
    public void performFirebaseLogin(Fragment activity, String email, String password) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            onSuccess(task.getResult().toString());
                        }
                        else {
                            onFailure((FirebaseException) task.getException());
                        }
                    }
                });
    }

    //Metodo invocato nel Model tramite il Contract e chiama il metodo onLoginSuccess nella View
   // @Override
    public String onSuccess(String message) {
        return message;
    }

    //Metodo invocato nel Model tramite il Contract e chiama il metodo onLoginFailure nella View
   // @Override
    public FirebaseException onFailure(FirebaseException e) {
        return e;
    }


}
