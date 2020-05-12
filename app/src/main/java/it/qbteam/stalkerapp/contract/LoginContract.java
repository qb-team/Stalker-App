package it.qbteam.stalkerapp.contract;

import androidx.fragment.app.Fragment;
import com.google.firebase.FirebaseException;

//The contract interface describes the communication between view and presenter. It helps us to design in a cleaner way the interaction.
public interface LoginContract {

    interface View{
        void onLoginSuccess(String message);
        void onLoginFailure(FirebaseException e);
    }

    interface Presenter{
        void login(Fragment fragment, String email, String password);
    }
    interface Interactor{
        void performFirebaseLogin(Fragment fragment, String email, String password);
    }

    interface onLoginListener{
        void onSuccess(String message);
        void onFailure(FirebaseException e);
    }
}

