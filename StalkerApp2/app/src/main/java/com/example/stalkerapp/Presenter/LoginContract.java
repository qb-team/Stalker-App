package com.example.stalkerapp.Presenter;

import androidx.fragment.app.Fragment;
public interface LoginContract {

    //Interfaccia che fa comunicare la View con il Presenter
    interface View{
        void onLoginSuccess(String message);
        void onLoginFailure(String message);
    }

    //Interfaccia vera e propria del Presenter
    interface Presenter{
        void login(Fragment fragment, String email, String password);
    }

    //Interfaccia del Model
    interface Intractor{
        void performFirebaseLogin(Fragment fragment, String email, String password);
    }

    //Interfaccia che fa comunicare il Model con il Presenter
    interface onLoginListener{
        void onSuccess(String message);
        void onFailure(String message);
    }
}

