package it.qbteam.stalkerapp.presenter;

import it.qbteam.stalkerapp.contract.LoginContract;
import it.qbteam.stalkerapp.model.authentication.LogInModel;
import com.google.firebase.FirebaseException;

public class LoginPresenter implements LoginContract.Presenter, LoginContract.onLoginListener{
    private LoginContract.View mLoginView;
    private LogInModel mLoginInteractor;

    //LoginPresenter's constructor.
    public LoginPresenter(LoginContract.View mLoginView){
        this.mLoginView = mLoginView;
        mLoginInteractor = new LogInModel(this);
    }

    //Calls the the method performFirebaseLogin(fragment, email, password) of the class LoginModel.
    @Override
    public void login(String email, String password) {
        mLoginInteractor.performFirebaseLogin(email, password);
    }

    public void forgotPassword(String email){
        mLoginInteractor.performResetPassword(email);
    }

    //Comunicates the success result of login to the view.
    @Override
    public void onSuccess() {
        mLoginView.onLoginSuccess();
    }

    //Comunicates the failure result of login to the view.
    @Override
    public void onFailure(FirebaseException e) {
        mLoginView.onCredentialFailure(e);
    }

    public void sendEmailResetPasswordSuccess(){
        mLoginView.onSendEmailSuccess();
    }


}


