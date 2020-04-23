package it.qbteam.stalkerapp.view.authentication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import it.qbteam.stalkerapp.AuthenticationActivity;
import it.qbteam.stalkerapp.MainActivity;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.model.data.UserFirebase;


public class LoginFragment extends Fragment implements View.OnClickListener{

    private EditText mEmail,mPassword;
    private Button mLoginConferm;

    private UserFirebase userFirebase;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.login_layout,container,false);

        mEmail = view.findViewById(R.id.EmailID);
        mPassword = view.findViewById(R.id.passwordID);
        mLoginConferm = view.findViewById(R.id.LoginConfermID);
        mLoginConferm.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.LoginConfermID:
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
        login(email, password);
    }


    //Metodo invocato dalla View per passare le credenziali al Model per fare il login all'utente

    public void login(String email, String password) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            onLoginSuccess(task.getResult().toString());
                        }
                        else {
                            onLoginFailure((FirebaseException) task.getException());
                        }
                    }
                });
    }


    public void onLoginSuccess(String message) {
        System.out.println("Ha funzionato");
        Toast.makeText(getActivity(), "Login effettuato con successo" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
        startActivity(intent);
    }

    //Se il login non ha avuto esito positivo l'utente viene notificato
    public void onLoginFailure(FirebaseException e) {
        System.out.println("Ha fallisce");
        if (e instanceof FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(getActivity(), "Le credenziali non sono state inserite correttamente" , Toast.LENGTH_LONG).show();
        }
        if (e instanceof FirebaseNetworkException){ //Credo che sia quello in caso l'utente esista già --> registrazione
            Toast.makeText(getActivity(), "Non sei connesso a internet" , Toast.LENGTH_LONG).show();
        }
        if (e instanceof FirebaseAuthInvalidUserException) {
            Toast.makeText(getActivity(), "L'e-mail non esiste" , Toast.LENGTH_LONG).show();
        }
    }

}
