package com.example.stalkerapp.Model;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.stalkerapp.Presenter.LoginContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Utente implements LoginContract.Intractor {

    private LoginContract.onLoginListener mOnLoginListener;

    public Utente(LoginContract.onLoginListener onLoginListener){
        this.mOnLoginListener=onLoginListener;
    }

    @Override
    public void performFirebaseLogin(Fragment activity, String email, String password) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mOnLoginListener.onSuccess(task.getResult().toString());
                        }
                        else {
                            mOnLoginListener.onFailure(task.getException().toString());
                        }
                    }
                });
    }
}
