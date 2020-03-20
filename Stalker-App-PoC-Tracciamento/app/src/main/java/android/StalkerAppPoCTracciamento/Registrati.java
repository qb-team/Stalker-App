package android.StalkerAppPoCTracciamento;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class Registrati extends AppCompatActivity {
    EditText mEmail, mPassword, mConfPassword;
    Button mRegisterBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    boolean verifica =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //Inizio onCreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrati);

        mEmail =(EditText) findViewById(R.id.EmailID);
        mPassword = (EditText)findViewById(R.id.PasswordID);
        mConfPassword = (EditText)findViewById(R.id.ConfPasswordID);
        mRegisterBtn= (Button) findViewById(R.id.ContinuaID);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        mRegisterBtn.setOnClickListener(new View.OnClickListener() { // Inizio Funzionalità pulsante Registrati (quando lo clicchi)
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confPassword = mConfPassword.getText().toString().trim();

                // Inizio Messaggi Errore compilazione
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Inserire l'Email");
                    return;
                }

                if(checkEmail(mEmail)) {
                    Toast.makeText(getApplicationContext(), "Utente già esistente", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Inserire la Password");
                    return;
                }

                if(!calculate(password)){
                    //mPassword.setError("La Password deve contenere almeno una lettere maiuscola e minuscola, un numero, uno special char");
                    Toast.makeText(getApplicationContext(),"La Password deve contenere almeno 6 caratteri, una lettere maiuscola e minuscola, un numero, uno special char",Toast.LENGTH_LONG).show();
                    return;
                }

                if(!(password.equals(confPassword))){
                    mConfPassword.setError("Le Password non coincidono");
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), CondizioniDuso.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                startActivity(intent);
                // Fine Messaggi Errore compilazione

            }
        }); // Fine Funzionalità pulsante Registrati

    }//Fine onCreate


    public boolean checkEmail(View v) {

        fAuth.fetchSignInMethodsForEmail(mEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                        if (!isNewUser)
                            verifica=true;

                    }
                });
        return verifica;
    }

    // Funzionalità per il backbutton (tasto per andare indietro)
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Sicuro di voler uscire?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            setResult(123);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean calculate(String password) {
        int score = 0;
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
                score++;
                specialChar = true;
            } else {
                if (!digit  &&  Character.isDigit(c)) {
                    score++;
                    digit = true;
                } else {
                    if (!upper || !lower) {
                        if (Character.isUpperCase(c)) {
                            upper = true;
                        } else {
                            lower = true;
                        }

                        if (upper && lower) {
                            score++;
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