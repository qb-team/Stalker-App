package qbteam.stalkerapp.Presenter;

import androidx.fragment.app.Fragment;
import qbteam.stalkerapp.Model.LoginModel;
import com.google.firebase.FirebaseException;

//Presenter di Login
public class LoginPresenter implements LoginContract.Presenter, LoginContract.onLoginListener{
    private LoginContract.View mLoginView;
    private LoginModel mLoginInteractor;

    //Costruttore che chiede come parametro un'istanza della vista
    public LoginPresenter(LoginContract.View mLoginView){
        this.mLoginView = mLoginView;
        mLoginInteractor = new LoginModel(this);
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
    public void onFailure(FirebaseException e) {
        mLoginView.onLoginFailure(e);
    }
}


