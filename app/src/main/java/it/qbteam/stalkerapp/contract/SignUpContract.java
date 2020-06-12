package it.qbteam.stalkerapp.contract;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseUser;

//The contract interface describes the communication between view and presenter. It helps us to design in a cleaner way the interaction.
public interface SignUpContract {

    interface View {
        void onSignUpSuccess(FirebaseUser firebaseUser);
        void onSignUpFailure(FirebaseException e);
    }

    interface Presenter {
        void signUp(String email, String password);
    }

    interface Interactor {
        void performFirebaseRegistration(String email, String password);
    }

    interface onRegistrationListener {
        void onSuccess(FirebaseUser firebaseUser);
        void onFailure(FirebaseException e);
    }
}