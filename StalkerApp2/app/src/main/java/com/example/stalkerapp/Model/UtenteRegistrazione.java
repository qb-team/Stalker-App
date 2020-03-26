package com.example.stalkerapp.Model;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.stalkerapp.Presenter.RegistrazioneContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UtenteRegistrazione implements RegistrazioneContract.Intractor {

    private static final String TAG = UtenteRegistrazione.class.getSimpleName();
    private RegistrazioneContract.onRegistrationListener mOnRegistrationListener;

    public UtenteRegistrazione(RegistrazioneContract.onRegistrationListener onRegistrationListener){
        this.mOnRegistrationListener = onRegistrationListener;
    }
    @Override
    public void performFirebaseRegistration(Fragment fragment, String email, String password) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            mOnRegistrationListener.onFailure(task.getException().getMessage());
                        }else{
                            mOnRegistrationListener.onSuccess(task.getResult().getUser());
                        }
                    }
                });
    }
}
