package com.example.stalkerapp.Presenter;

import androidx.fragment.app.Fragment;
import com.example.stalkerapp.Model.UtenteRegistrazione;
import com.google.firebase.auth.FirebaseUser;

//Presenter di Registrati
public class RegistrazionePresenter implements RegistrazioneContract.Presenter, RegistrazioneContract.onRegistrationListener {
    private RegistrazioneContract.View mRegisterView;
    private UtenteRegistrazione mRegistrationInteractor;

    //Costruttore che chiede come parametro un'istanza della View
    public RegistrazionePresenter(RegistrazioneContract.View registerView){
        this.mRegisterView = registerView;
        mRegistrationInteractor = new UtenteRegistrazione(this);
    }

    //Metodo invocato dalla View per passare le credenziali al Model per registrare un nuovo utente
    @Override
    public void register(Fragment fragment, String email, String password) {
        mRegistrationInteractor.performFirebaseRegistration(fragment,email,password);
    }

    //Metodo invocato nel Model tramite il Contract e chiama il metodo onRegistrationSuccess nella View
    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        mRegisterView.onRegistrationSuccess(firebaseUser);
    }

    //Metodo invocato nel Model tramite il Contract e chiama il metodo onRegistrationFailure nella View
    @Override
    public void onFailure(String message) {
        mRegisterView.onRegistrationFailure(message);

    }
}