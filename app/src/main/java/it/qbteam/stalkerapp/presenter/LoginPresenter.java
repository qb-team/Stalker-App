package it.qbteam.stalkerapp.presenter;

import androidx.fragment.app.Fragment;

import it.qbteam.stalkerapp.contract.LoginContract;
import it.qbteam.stalkerapp.model.authentication.LoginModel;
import com.google.firebase.FirebaseException;

public class LoginPresenter implements LoginContract.Presenter, LoginContract.onLoginListener{
    private LoginContract.View mLoginView;
    private LoginModel mLoginInteractor;

    //LoginPresenter costructor.
    public LoginPresenter(LoginContract.View mLoginView){
        this.mLoginView = mLoginView;
        mLoginInteractor = new LoginModel(this);
    }

    //Calls the the method performFirebaseLogin(fragment, email, password) of the class LoginModel.
    @Override
    public void login(Fragment fragment, String email, String password) {
        mLoginInteractor.performFirebaseLogin(fragment, email, password);
    }

    //Comunicates the success result of login to the view.
    @Override
    public void onSuccess(String message) {
        mLoginView.onLoginSuccess(message);
    }

    //Comunicates the failure result of login to the view.
    @Override
    public void onFailure(FirebaseException e) {
        mLoginView.onLoginFailure(e);
    }
}


