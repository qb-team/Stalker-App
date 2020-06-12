package it.qbteam.stalkerapp.contract;

import com.google.firebase.FirebaseException;

//The contract interface describes the communication between view and presenter. It helps us to design in a cleaner way the interaction.
public interface LoginContract {

    interface View{
        void onLoginSuccess();
        void onCredentialFailure(FirebaseException e);
        void onSendEmailSuccess();
    }

    interface Presenter{
        void login(String email, String password);
        void forgotPassword(String email);
    }
    interface Interactor{
        void performFirebaseLogin(String email, String password);
        void performResetEmail(String email);
    }

    interface onLoginListener{
        void onSuccess();
        void onFailure(FirebaseException e);
        void sendEmailResetPasswordSuccess();
    }
}

