package it.qbteam.stalkerapp.ui.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.presenter.SignUpContract;
import it.qbteam.stalkerapp.presenter.SignUpPresenter;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

//Parte visiva (View) di SignUp
public class SignUpFragment extends Fragment implements View.OnClickListener, SignUpContract.View, OnBackPressListener {
    public final static String TAG="Registrati_Fragment";


    EditText emailEditText, passwordEditText, confPasswordEditText;
    Button signUpButton;
    Button conditionUseButton;

    private SignUpPresenter signUpPresenter;
    ProgressDialog progressDialog;
    CheckBox conditionUseCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_registrati,container,false);
        emailEditText = view.findViewById(R.id.emailID);
        passwordEditText = view.findViewById(R.id.passwordID);
        confPasswordEditText = view.findViewById(R.id.confPasswordID);
        conditionUseCheckBox = view.findViewById(R.id.conditionUseID);
        signUpButton= view.findViewById(R.id.signUpButtonID);
        conditionUseButton= view.findViewById(R.id.conditionButtonID);
        signUpPresenter=new SignUpPresenter(this);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Stiamo registrando il tuo account sul Database");
        signUpButton.setOnClickListener(this);
        conditionUseButton.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signUpButtonID:
                checkSignUpDetails();
                break;
            case R.id.conditionButtonID:
                showCondizioniDuso();
                break;
        }
    }

    //Controlla le credenziali inserite dall'utente nella vista della registrazione
    private void checkSignUpDetails() {
        if(!TextUtils.isEmpty(emailEditText.getText().toString()) && !TextUtils.isEmpty(passwordEditText.getText().toString())&&calculate(passwordEditText.getText().toString())){
            controllSignUp(emailEditText.getText().toString(), passwordEditText.getText().toString());
        }else{
            if(!calculate(passwordEditText.getText().toString()))
                Toast.makeText(getContext(), "Inserire una password che comprenda: una lettera maiuscola e minuscola,un numero, un carattere speciale e una lunghezza minima di 6 caratteri", Toast.LENGTH_LONG).show();
            if(TextUtils.isEmpty(emailEditText.getText().toString())){
                emailEditText.setError("Inserisci una email valida");
            }if(TextUtils.isEmpty(passwordEditText.getText().toString())){
                passwordEditText.setError("Inserisci una password valida");
            }
        }
    }

    //Controllo se l'utente ha spuntato il checkbox delle condizioni d'uso e avvia il metodo "signUp" nel Presenter
    private void controllSignUp(String email, String password) {
        if(conditionUseCheckBox.isChecked()) {
            progressDialog.show();
            signUpPresenter.signUp(this, email, password);
        }
        else Toast.makeText(getContext(), "Per poterti registrare devi accettare le condizioni d'uso", Toast.LENGTH_SHORT).show();
    }

    //Popup che mostra il contenuto testuale delle condizioni d'uso
    private void showCondizioniDuso(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Condizioni d'uso")
                .setMessage("I Contenuti dei Servizi sono destinati esclusivamente ad un utilizzo personale. Ogni diverso utilizzo è vietato in ogni forma. L’Istituto detiene tutti i diritti di sfruttamento dei marchi utilizzati in collegamento ai Servizi. I Servizi e i suoi Contenuti sono protetti dalle norme sul diritto d’autore vigente in Italia e dalle norme internazionali sul diritto d’autore. L’Utente non è autorizzato a modificare, pubblicare, trasmettere, condividere, cedere in uso a qualsiasi titolo, riprodurre (oltre i limiti di seguito precisati), tradurre, rielaborare, distribuire, eseguire, dare accesso o sfruttare commercialmente in qualsiasi modo i Servizi e i loro Contenuti (incluso il software) anche solo parzialmente. L’Istituto non assume alcuna responsabilità in relazione a danni o limitazioni d’uso di siti internet, computer o altri strumenti che abbiano utilizzato i Servizi e i loro Contenuti.")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        builder.show();
    }

    //Se la registrazione ha avuto esito positivo l'utente viene notificato e indirizzato nella schermata di HomePage dedicata agli utenti autenticati
    @Override
    public void onSignUpSuccess(FirebaseUser firebaseUser) {
        progressDialog.dismiss();

        Toast.makeText(getActivity(), "Registrazione effettuato con successo" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //Se la registrazione non ha avuto esito positivo l'utente viene notificato
    @Override
    public void onSignUpFailure(FirebaseException e) {
        progressDialog.dismiss();
        if (e instanceof FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(getActivity(), "Le credenziali non sono state inserite correttamente" , Toast.LENGTH_LONG).show();
        }
        if (e instanceof FirebaseAuthUserCollisionException){ //Credo che sia quello in caso l'utente esista già --> registrazione
            Toast.makeText(getActivity(), "L'e-mail è già presente nel sistema" , Toast.LENGTH_LONG).show();
        }
    }

    //Controllo che la password sia sicura
    public boolean calculate(String password) {
        boolean upper = false;
        boolean lower = false;
        boolean number = false;
        boolean specialChar = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (!specialChar  &&  !Character.isLetterOrDigit(c)) {
                specialChar = true;
            } else {
                if (!number  &&  Character.isDigit(c)) {
                    number = true;
                } else {
                    if (!upper || !lower) {
                        if (Character.isUpperCase(c)) {
                            upper = true;
                        } else {
                            lower = true;
                        }

                        if (upper && lower) {

                        }
                    }
                }
            }
        }

        if(upper && lower && number && specialChar && password.length()>=6)
            return true;
        else
            return false;
    }

    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }
}
