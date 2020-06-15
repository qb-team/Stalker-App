package it.qbteam.stalkerapp.model.authentication;

import androidx.annotation.NonNull;
import it.qbteam.stalkerapp.contract.LoginContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;

//Modello di Login
public class LogInModel implements LoginContract.Interactor {

    private LoginContract.onLoginListener mOnLoginListener;

    //LoginModel costructor.
    public LogInModel(LoginContract.onLoginListener onLoginListener){
        this.mOnLoginListener=onLoginListener;
    }

    //Firebase's method that allows the user login to Stalker application(in Firebase server).
    @Override
    public void performFirebaseLogin(String email, String password) {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){

                        mOnLoginListener.onSuccess();

                    }
                    else {
                        mOnLoginListener.onFailure((FirebaseException) task.getException());
                    }
                });
    }

    public void performResetPassword(String email){
        FirebaseAuth.getInstance()
                .sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mOnLoginListener.sendEmailResetPasswordSuccess();
                        }
                        else
                            mOnLoginListener.onFailure((FirebaseException) task.getException());
                    }
                });
    }

}
