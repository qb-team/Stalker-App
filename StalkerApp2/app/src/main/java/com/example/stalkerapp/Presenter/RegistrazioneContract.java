package com.example.stalkerapp.Presenter;

import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseUser;

public interface RegistrazioneContract {
    interface View {
        void onRegistrationSuccess(FirebaseUser firebaseUser);

        void onRegistrationFailure(String message);
    }

    interface Presenter {
        void register(Fragment fragment, String email, String password);
    }

    interface Intractor {
        void performFirebaseRegistration(Fragment fragment, String email, String password);
    }

    interface onRegistrationListener {
        void onSuccess(FirebaseUser firebaseUser);

        void onFailure(String message);
    }
}