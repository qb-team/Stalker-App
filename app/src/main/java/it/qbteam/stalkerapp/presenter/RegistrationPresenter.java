package it.qbteam.stalkerapp.presenter;

import androidx.fragment.app.Fragment;
import it.qbteam.stalkerapp.model.RegistrationModel;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseUser;

//Presenter di Registrati
public class RegistrationPresenter implements RegistrationContract.Presenter, RegistrationContract.onRegistrationListener {
    private RegistrationContract.View mRegisterView;
    private RegistrationModel mRegistrationInteractor;

    //Costruttore che chiede come parametro un'istanza della View
    public RegistrationPresenter(RegistrationContract.View registerView){
        this.mRegisterView = registerView;
        mRegistrationInteractor = new RegistrationModel(this);
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
    public void onFailure(FirebaseException e) {
        mRegisterView.onRegistrationFailure(e);

    }
}