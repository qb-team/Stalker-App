package it.qbteam.stalkerapp.presenter;

import androidx.fragment.app.Fragment;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseUser;

//Interfaccia del presenter di Registrati che comunica con il Model e la View
public interface SignUpContract {

    //Interfaccia che fa comunicare la View con il Presenter
    interface View {
        void onRegistrationSuccess(FirebaseUser firebaseUser);
        void onRegistrationFailure(FirebaseException e);
    }

    //Interfaccia del Presenter
    interface Presenter {
        void register(Fragment fragment, String email, String password);
    }

    //Interfaccia del Model
    interface Model {
        void performFirebaseRegistration(Fragment fragment, String email, String password);
    }

    //Interfaccia che fa comunicare il Model con il Presenter
    interface onRegistrationListener {
        void onSuccess(FirebaseUser firebaseUser);
        void onFailure(FirebaseException e);
    }
}