package com.example.stalkerapp.ui.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.example.stalkerapp.HomePage;
import com.example.stalkerapp.MainActivity;
import com.example.stalkerapp.Presenter.RegistrazioneContract;
import com.example.stalkerapp.Presenter.RegistrazionePresenter;
import com.example.stalkerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Registrati extends Fragment implements View.OnClickListener, RegistrazioneContract.View{
    public final static String TAG="Registrati_Fragment";
    private MainViewModel mViewModel;
    EditText mEmail, mPassword, mConfPassword;
    String userID;
    Button mRegisterBtn;
    FirebaseAuth fAuth;
    Button cond;
    private RegistrazionePresenter mRegisterPresenter;
    ProgressDialog mPrgressDialog;
    CheckBox mCondizioniDuso;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_registrati,container,false);
        mEmail = view.findViewById(R.id.EmailID);
        mPassword = view.findViewById(R.id.PasswordID);
        mConfPassword = view.findViewById(R.id.ConfPasswordID);
        mCondizioniDuso = view.findViewById(R.id.condizionidusoID);
        mRegisterBtn= view.findViewById(R.id.RegistartiID);
        cond= view.findViewById(R.id.condizioniID);


        mRegisterPresenter=new RegistrazionePresenter(this);
        mPrgressDialog = new ProgressDialog(getContext());
        mPrgressDialog.setMessage("Stiamo registrando il tuo account sul Database");

        mRegisterBtn.setOnClickListener(this);
        cond.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.RegistartiID:
                checkRegistrationDetails();
                break;
            case R.id.condizioniID:
                showCondizioniDuso();
                break;
        }
    }
    private void checkRegistrationDetails() {
        if(!TextUtils.isEmpty(mEmail.getText().toString()) && !TextUtils.isEmpty(mPassword.getText().toString())&&calculate(mPassword.getText().toString())){
            initRegistrati(mEmail.getText().toString(), mPassword.getText().toString());
        }else{
            if(!calculate(mPassword.getText().toString()))
                Toast.makeText(getContext(), "Inserire una password che comprenda: una lettera maiuscola e minuscola,un numero, un carattere speciale e una lunghezza minima di 6 caratteri", Toast.LENGTH_LONG).show();
            if(TextUtils.isEmpty(mEmail.getText().toString())){
                mEmail.setError("Inserisci una email valida");
            }if(TextUtils.isEmpty(mPassword.getText().toString())){
                mPassword.setError("Inserisci una password valida");
            }
        }
    }
    private void initRegistrati(String email, String password) {
        if(mCondizioniDuso.isChecked()) {
            mPrgressDialog.show();
            mRegisterPresenter.register(this, email, password);
        }
        else Toast.makeText(getContext(), "Per poterti registrare devi accettare le condizioni d'uso", Toast.LENGTH_SHORT).show();
    }
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
    @Override
    public void onRegistrationSuccess(FirebaseUser firebaseUser) {
        mPrgressDialog.dismiss();
        Toast.makeText(getActivity(), "Registarzione effettuato con successo" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), HomePage.class);
        startActivity(intent);

    }

    @Override
    public void onRegistrationFailure(String message) {
        mPrgressDialog.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

   public boolean calculate(String password) {


        boolean upper = false;

        boolean lower = false;

        boolean numeri = false;

        boolean specialChar = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (!specialChar  &&  !Character.isLetterOrDigit(c)) {

                specialChar = true;
            } else {
                if (!numeri  &&  Character.isDigit(c)) {

                    numeri = true;
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

        if(upper && lower && numeri && specialChar && password.length()>=6)
            return true;
        else
            return false;
    }


}
