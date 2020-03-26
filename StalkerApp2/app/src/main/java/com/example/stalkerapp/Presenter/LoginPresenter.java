package com.example.stalkerapp.Presenter;

import androidx.fragment.app.Fragment;

import com.example.stalkerapp.Model.Utente;

public class LoginPresenter implements LoginContract.Presenter, LoginContract.onLoginListener{
    private LoginContract.View mLoginView;
    private Utente mLoginInteractor;

    public LoginPresenter(LoginContract.View mLoginView){
        this.mLoginView = mLoginView;
        mLoginInteractor = new Utente(this);
    }
    @Override
    public void login(Fragment fragment, String email, String password) {
        mLoginInteractor.performFirebaseLogin(fragment, email, password);
    }

    @Override
    public void onSuccess(String message) {
        mLoginView.onLoginSuccess(message);
    }

    @Override
    public void onFailure(String message) {
        mLoginView.onLoginFailure(message);
    }
}


