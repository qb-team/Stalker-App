package com.example.stalkerapp.Presenter;

import androidx.fragment.app.Fragment;
public interface LoginContract {

        interface View{
            void onLoginSuccess(String message);
            void onLoginFailure(String message);
        }

        interface Presenter{
            void login(Fragment fragment, String email, String password);
        }

        interface Intractor{
            void performFirebaseLogin(Fragment fragment, String email, String password);
        }

        interface onLoginListener{
            void onSuccess(String message);
            void onFailure(String message);
        }
    }

