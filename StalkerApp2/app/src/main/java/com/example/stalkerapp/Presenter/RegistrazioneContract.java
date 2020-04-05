package com.example.stalkerapp.Presenter;

import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseUser;

//Interfaccia del presenter di Registrati che comunica con il Model e la View
public interface RegistrazioneContract {

    //Interfaccia che fa comunicare la View con il Presenter
    interface View {
        void onRegistrationSuccess(FirebaseUser firebaseUser);
        void onRegistrationFailure(String message);
    }

    //Interfaccia vera e propria del Presenter
    interface Presenter {
        void register(Fragment fragment, String email, String password);
    }

    //Interfaccia del Model
    interface Intractor {
        void performFirebaseRegistration(Fragment fragment, String email, String password);
    }

    //Interfaccia che fa comunicare il Model con il Presenter
    interface onRegistrationListener {
        void onSuccess(FirebaseUser firebaseUser);
        void onFailure(String message);
    }
}