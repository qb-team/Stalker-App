package it.qbteam.stalkerapp.presenter;

import androidx.fragment.app.Fragment;

import it.qbteam.stalkerapp.contract.SignUpContract;
import it.qbteam.stalkerapp.model.authentication.SignUpModel;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseUser;

//Presenter di Registrati
public class SignUpPresenter implements SignUpContract.Presenter, SignUpContract.onRegistrationListener {
    private SignUpContract.View mSignUpView;
    private SignUpModel mSignUpInteractor;

    //Costruttore che chiede come parametro un'istanza della View
    public SignUpPresenter(SignUpContract.View signUpView){
        this.mSignUpView = signUpView;
        mSignUpInteractor = new SignUpModel(this);
    }

    //Metodo invocato dalla View per passare le credenziali al Model per registrare un nuovo utente
    @Override
    public void signUp(Fragment fragment, String email, String password) {
        mSignUpInteractor.performFirebaseRegistration(fragment,email,password);
    }

    //Metodo invocato nel Model tramite il Contract e chiama il metodo onRegistrationSuccess nella View
    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        mSignUpView.onSignUpSuccess(firebaseUser);
    }

    //Metodo invocato nel Model tramite il Contract e chiama il metodo onRegistrationFailure nella View
    @Override
    public void onFailure(FirebaseException e) {
        mSignUpView.onSignUpFailure(e);

    }
}