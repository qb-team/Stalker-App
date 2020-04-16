package com.example.stalkerapp.ui.main;

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
import com.example.stalkerapp.HomePage;
import com.example.stalkerapp.Presenter.LoginContract;
import com.example.stalkerapp.Presenter.LoginPresenter;
import com.example.stalkerapp.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

//Parte visiva (View) di Login
public class Login extends Fragment implements LoginContract.View, View.OnClickListener {
    public final static String TAG="Login_Fragment";
    private LoginPresenter loginPresenter;
    ProgressDialog mProgressDialog;
    private EditText mEmail,mPassword;
    private Button mLoginBtn;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Costruzione della vista
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login,container,false);
        mEmail = view.findViewById(R.id.Emailtext);
        mPassword = view.findViewById(R.id.passwordtext);
        mLoginBtn = view.findViewById(R.id.buttonAccesso);
        mLoginBtn.setOnClickListener(this);
        loginPresenter = new LoginPresenter(this);
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Aspetta il completamento del login");
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonAccesso:
                checkLoginDetails();
                break;
        }
    }

    //Controlla le credenziali inserite dall'utente nella vista del Login
    private void checkLoginDetails() {
        if(!TextUtils.isEmpty(mEmail.getText().toString()) && !TextUtils.isEmpty(mPassword.getText().toString())){
            initLogin(mEmail.getText().toString(), mPassword.getText().toString());
        }else{
            if(TextUtils.isEmpty(mEmail.getText().toString())){
                mEmail.setError("Inserisci una email valida");
            }if(TextUtils.isEmpty(mPassword.getText().toString())){
                mPassword.setError("Inserisci una password valida");
            }
        }
    }

    //Avvia il metodo del Login nel Presenter
    private void initLogin(String email, String password) {
        mProgressDialog.show();
        loginPresenter.login(this, email, password);
    }

    //Se il login ha avuto esito positivo l'utente viene notificato e indirizzato nella schermata di HomePage dedicata agli utenti autenticati
    @Override
    public void onLoginSuccess(String message) {
        mProgressDialog.dismiss();
        Toast.makeText(getActivity(), "Login effettuato con successo" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), HomePage.class);
        startActivity(intent);
    }

    //Se il login non ha avuto esito positivo l'utente viene notificato
    @Override
    public void onLoginFailure(FirebaseException e) {
        mProgressDialog.dismiss();
        if (e instanceof FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(getActivity(), "Le credenziali non sono state inserite correttamente" , Toast.LENGTH_LONG).show();
        }
        if (e instanceof FirebaseAuthUserCollisionException){ //Credo che sia quello in caso l'utente esista già --> registrazione
            Toast.makeText(getActivity(), "Non so bene cosa fa" , Toast.LENGTH_LONG).show();
        }
        if (e instanceof FirebaseAuthInvalidUserException) {
            Toast.makeText(getActivity(), "L'e-mail non esiste" , Toast.LENGTH_LONG).show();
        }
    }
}