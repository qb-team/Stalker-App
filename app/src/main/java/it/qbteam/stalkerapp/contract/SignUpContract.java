package it.qbteam.stalkerapp.contract;

import androidx.fragment.app.Fragment;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseUser;

//Interfaccia del presenter di Registrati che comunica con il Interactor e la View
public interface SignUpContract {

    //Interfaccia che fa comunicare la View con il Presenter
    interface View {
        void onSignUpSuccess(FirebaseUser firebaseUser);
        void onSignUpFailure(FirebaseException e);
    }

    //Interfaccia del Presenter
    interface Presenter {
        void signUp(Fragment fragment, String email, String password);
    }

    //Interfaccia del Interactor
    interface Interactor {
        void performFirebaseRegistration(Fragment fragment, String email, String password);
    }

    //Interfaccia che fa comunicare il Interactor con il Presenter
    interface onRegistrationListener {
        void onSuccess(FirebaseUser firebaseUser);
        void onFailure(FirebaseException e);
    }
}