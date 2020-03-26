package com.example.stalkerapp.Presenter;

import androidx.fragment.app.Fragment;

import com.example.stalkerapp.Model.UtenteRegistrazione;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Ashish on 27-09-2017.
 */
import com.example.stalkerapp.Model.Utente;

public class RegistrazionePresenter implements RegistrazioneContract.Presenter, RegistrazioneContract.onRegistrationListener {
    private RegistrazioneContract.View mRegisterView;
    private UtenteRegistrazione mRegistrationInteractor;

    public RegistrazionePresenter(RegistrazioneContract.View registerView){
        this.mRegisterView = registerView;
        mRegistrationInteractor = new UtenteRegistrazione(this);
    }
    @Override
    public void register(Fragment fragment, String email, String password) {
        mRegistrationInteractor.performFirebaseRegistration(fragment,email,password);
    }

    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        mRegisterView.onRegistrationSuccess(firebaseUser);
    }

    @Override
    public void onFailure(String message) {
        mRegisterView.onRegistrationFailure(message);

    }
}