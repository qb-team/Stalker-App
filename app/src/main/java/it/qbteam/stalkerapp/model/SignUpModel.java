package it.qbteam.stalkerapp.model;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import it.qbteam.stalkerapp.presenter.SignUpContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//Modello di Registrati
public class SignUpModel implements SignUpContract.Model {
    //private static final String TAG = UtenteRegistrazione.class.getSimpleName();
    private SignUpContract.onRegistrationListener mOnRegistrationListener;//

    //Costruttore
    public SignUpModel(SignUpContract.onRegistrationListener onRegistrationListener){
        this.mOnRegistrationListener = onRegistrationListener;
    }

    //Metodo (di Firebase) che permette di effettuare la registrazione sul server Firebase
    @Override
    public void performFirebaseRegistration(Fragment fragment, String email, String password) {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    mOnRegistrationListener.onFailure((FirebaseException) task.getException());
                }else{
                    mOnRegistrationListener.onSuccess(task.getResult().getUser());
                }
            }
        });
    }
}
