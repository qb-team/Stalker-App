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
public class LoginModel implements LoginContract.Interactor {

    private LoginContract.onLoginListener mOnLoginListener;

    //LoginModel costructor.
    public LoginModel(LoginContract.onLoginListener onLoginListener){
        this.mOnLoginListener=onLoginListener;
    }

    //Firebase's method that allows the user login to Stalker application(in Firebase server).
    @Override
    public void performFirebaseLogin(Fragment activity, String email, String password) {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){

                        mOnLoginListener.onSuccess(task.getResult().toString());

                    }
                    else {
                        mOnLoginListener.onFailure((FirebaseException) task.getException());
                    }
                });

}
}
