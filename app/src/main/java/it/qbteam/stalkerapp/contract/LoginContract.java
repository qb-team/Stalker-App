package it.qbteam.stalkerapp.contract;

import androidx.fragment.app.Fragment;
import com.google.firebase.FirebaseException;

public interface LoginContract {

    //Interfaccia che fa comunicare la View con il Presenter
    interface View{
        void onLoginSuccess(String message);
        void onLoginFailure(FirebaseException e);
    }

    //Interfaccia vera e propria del Presenter
    interface Presenter{
        void login(Fragment fragment, String email, String password);
    }
    //Interfaccia del Interactor
    interface Interactor{
        void performFirebaseLogin(Fragment fragment, String email, String password);
    }

    //Interfaccia che fa comunicare il Interactor con il Presenter
    interface onLoginListener{
        void onSuccess(String message);
        void onFailure(FirebaseException e);
    }
}

