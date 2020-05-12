package it.qbteam.stalkerapp.ui.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.contract.LoginContract;
import it.qbteam.stalkerapp.presenter.LoginPresenter;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

    public class LoginFragment extends Fragment implements LoginContract.View, View.OnClickListener, OnBackPressListener {
    public final static String TAG="Login_Fragment";
    private LoginPresenter loginPresenter;
    ProgressDialog progressDialog;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;

    //Creation of the fragment as a component.
    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Creation of the graphic part displayed by the user.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login,container,false);
        emailEditText = view.findViewById(R.id.Emailtext);
        passwordEditText = view.findViewById(R.id.passwordtextID);
        loginButton = view.findViewById(R.id.loginButtonID);
        loginButton.setOnClickListener(this);
        loginPresenter = new LoginPresenter(this);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Aspetta il completamento del login");
        return view;
    }

    //When the user click on the "Login" button, this method is invoked and it calls the `checkLoginDetails()` method.
    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.loginButtonID) {
            checkLoginDetails();
        }

    }

    //Check if the user has written their credentials and send them to the `initLogin (email: String, password: String)` method, otherwise it signals the absence of them.
    void checkLoginDetails() {

        if(!TextUtils.isEmpty(emailEditText.getText().toString()) && !TextUtils.isEmpty(passwordEditText.getText().toString())) {
            checkLogin(emailEditText.getText().toString(), passwordEditText.getText().toString());
        }

        else {

            if(TextUtils.isEmpty(emailEditText.getText().toString())) {
                emailEditText.setError("Inserisci una email valida");
            }

            if(TextUtils.isEmpty(passwordEditText.getText().toString())) {
                passwordEditText.setError("Inserisci una password valida");
            }

        }
    }

    //Start the login method in the presenter.
    private void checkLogin(String email, String password) {
        progressDialog.show();
        loginPresenter.login(this, email, password);
    }

    //If the login was successful, the user is notified and directed to the HomePage screen dedicated to authenticated users.
    @Override
    public void onLoginSuccess(String message) {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), "Login effettuato con successo" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //If the login wasn't successful, the user is notified.
    @Override
    public void onLoginFailure(FirebaseException e) {
        progressDialog.dismiss();

        if(e instanceof FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(getActivity(), "Le credenziali non sono state inserite correttamente" , Toast.LENGTH_LONG).show();
        }

        if(e instanceof FirebaseNetworkException){ //Credo che sia quello in caso l'utente esista già --> registrazione
            Toast.makeText(getActivity(), "Non sei connesso a internet" , Toast.LENGTH_LONG).show();
        }

        if(e instanceof FirebaseAuthInvalidUserException) {
            Toast.makeText(getActivity(), "L'e-mail non esiste" , Toast.LENGTH_LONG).show();
        }

    }

    //Manages the back button.
    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }
}