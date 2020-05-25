package it.qbteam.stalkerapp.model.authentication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import it.qbteam.stalkerapp.contract.SignUpContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//Modello di Registrati
public class SignUpModel implements SignUpContract.Interactor {

    private SignUpContract.onRegistrationListener mOnRegistrationListener;//

    //SignUpModel costructor.
    public SignUpModel(SignUpContract.onRegistrationListener onRegistrationListener){
        this.mOnRegistrationListener = onRegistrationListener;
    }

    //Firebase's method that allows the user sign up to Stalker application(in Firebase server).
    @Override
    public void performFirebaseRegistration(String email, String password) {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if(!task.isSuccessful()){
                    mOnRegistrationListener.onFailure((FirebaseException) task.getException());
                }else{
                    mOnRegistrationListener.onSuccess(task.getResult().getUser());
                }
            });
    }
}
