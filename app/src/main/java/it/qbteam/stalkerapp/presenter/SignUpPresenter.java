package it.qbteam.stalkerapp.presenter;

import androidx.fragment.app.Fragment;
import it.qbteam.stalkerapp.contract.SignUpContract;
import it.qbteam.stalkerapp.model.authentication.SignUpModel;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseUser;


public class SignUpPresenter implements SignUpContract.Presenter, SignUpContract.onRegistrationListener {
    private SignUpContract.View mSignUpView;
    private SignUpModel mSignUpInteractor;

    //SignUpPresenter's constructor
    public SignUpPresenter(SignUpContract.View signUpView){
        this.mSignUpView = signUpView;
        mSignUpInteractor = new SignUpModel(this);
    }

    //Calls the the method performFirebaseRegistration(fragment,email,password) of the class SignUpModel.
    @Override
    public void signUp(Fragment fragment, String email, String password) {
        mSignUpInteractor.performFirebaseRegistration(fragment,email,password);
    }

    //Comunicates the success result of sign up to the view.
    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        mSignUpView.onSignUpSuccess(firebaseUser);
    }

    //Comunicates the failure result of sign up to the view.
    @Override
    public void onFailure(FirebaseException e) {
        mSignUpView.onSignUpFailure(e);

    }
}