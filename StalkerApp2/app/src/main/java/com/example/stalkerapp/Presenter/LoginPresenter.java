package com.example.stalkerapp.Presenter;

import androidx.fragment.app.Fragment;
import com.example.stalkerapp.Model.Utente;

//Presenter di Login
public class LoginPresenter implements LoginContract.Presenter, LoginContract.onLoginListener{
    private LoginContract.View mLoginView;
    private Utente mLoginInteractor;

    //Costruttore che chiede come parametro un'istanza della vista
    public LoginPresenter(LoginContract.View mLoginView){
        this.mLoginView = mLoginView;
        mLoginInteractor = new Utente(this);
    }

    //Metodo invocato dalla View per passare le credenziali al Model per fare il login all'utente
    @Override
    public void login(Fragment fragment, String email, String password) {
        mLoginInteractor.performFirebaseLogin(fragment, email, password);
    }

    //Metodo invocato nel Model tramite il Contract e chiama il metodo onLoginSuccess nella View
    @Override
    public void onSuccess(String message) {
        mLoginView.onLoginSuccess(message);
    }

    //Metodo invocato nel Model tramite il Contract e chiama il metodo onLoginFailure nella View
    @Override
    public void onFailure(String message) {
        mLoginView.onLoginFailure(message);
    }
}


