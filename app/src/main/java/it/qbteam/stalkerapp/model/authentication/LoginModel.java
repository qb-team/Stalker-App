package it.qbteam.stalkerapp.model.authentication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import it.qbteam.stalkerapp.contract.LoginContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//Modello di Login
public class LoginModel implements LoginContract.Model {

    private LoginContract.onLoginListener mOnLoginListener;

    //Costruttore
    public LoginModel(LoginContract.onLoginListener onLoginListener){
        this.mOnLoginListener=onLoginListener;
    }

    //Metodo (di Firebase) che permette di effettuare il login sul server Firebase
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
                            mOnLoginListener.onFailure((FirebaseException) task.getException());
                        }
                    }
                });
    }
}
