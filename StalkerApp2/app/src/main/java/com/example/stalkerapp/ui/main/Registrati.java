package com.example.stalkerapp.ui.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stalkerapp.HomePage;
import com.example.stalkerapp.MainActivity;
import com.example.stalkerapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registrati extends Fragment {
    public final static String TAG="Registrati_Fragment";
    private MainViewModel mViewModel;
    EditText mEmail, mPassword, mConfPassword;
    String userID;
    Button mRegisterBtn;
    FirebaseAuth fAuth;
    Button cond;
    CheckBox mCondizioniDuso;
    private FirebaseFirestore fStore;

    public Registrati() {

        // Required empty public constructor
        //
        }



    public static Registrati newInstance() {
        return new Registrati();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();



        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_registrati,container,false);
        mEmail = view.findViewById(R.id.EmailID);
        mPassword = view.findViewById(R.id.PasswordID);
        mConfPassword = view.findViewById(R.id.ConfPasswordID);
        mRegisterBtn= view.findViewById(R.id.RegistartiID);
        cond= view.findViewById(R.id.condizioniID);
        mCondizioniDuso = view.findViewById(R.id.condizionidusoID);

        cond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });
        mRegisterBtn.setOnClickListener(new View.OnClickListener() { // Inizio Funzionalità pulsante Registrati (quando lo clicchi)
            @Override
            public void onClick(View v) {

                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confPassword = mConfPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Inserire l'Email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Inserire la Password");
                    return;
                }

                if (!calculate(password)) {
                    Toast.makeText(getContext(), "La Password deve contenere almeno 6 caratteri, una lettere maiuscola e minuscola, un numero, uno special char", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!(password.equals(confPassword))) {
                    mConfPassword.setError("Le Password non coincidono");
                    return;
                }
                if (mCondizioniDuso.isChecked()) {
                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Ti sei registrato e hai effettuato il login", Toast.LENGTH_SHORT).show();
                                userID = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("email", email);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: Registrazione effettuata " + userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: Si è verificato un errore" + e.toString());
                                    }
                                });
                                Intent intent = new Intent(getActivity(), HomePage.class);
                                startActivity(intent);


                            } else {
                                Toast.makeText(getContext(), "Errore! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else  Toast.makeText(getContext(), "Per poterti registrare devi accettare le condizioni d'uso", Toast.LENGTH_SHORT).show();
            }
    });


        return view;
    }
    public boolean calculate(String password) {

        // boolean indicating if password has an upper case
        boolean upper = false;
        // boolean indicating if password has a lower case
        boolean lower = false;
        // boolean indicating if password has at least one digit
        boolean digit = false;
        // boolean indicating if password has a leat one special char
        boolean specialChar = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (!specialChar  &&  !Character.isLetterOrDigit(c)) {

                specialChar = true;
            } else {
                if (!digit  &&  Character.isDigit(c)) {

                    digit = true;
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

        if(upper && lower && digit && specialChar && password.length()>=6)
            return true;
        else
            return false;
    }


}
