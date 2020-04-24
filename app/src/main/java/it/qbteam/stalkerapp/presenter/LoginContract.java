package it.qbteam.stalkerapp.presenter;

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

    //Interfaccia del Model
    interface Model{
        void performFirebaseLogin(Fragment fragment, String email, String password);
    }

    //Interfaccia che fa comunicare il Model con il Presenter
    interface onLoginListener{
        void onSuccess(String message);
        void onFailure(FirebaseException e);
    }
}

