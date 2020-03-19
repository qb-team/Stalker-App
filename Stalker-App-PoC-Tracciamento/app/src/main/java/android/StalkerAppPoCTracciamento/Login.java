package android.StalkerAppPoCTracciamento;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mLoginBtn;
    FirebaseAuth fAuth; //  Punto d'acceso per l'autenticazione a Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Inizio onCreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Iniziallizzazione
        mEmail = findViewById(R.id.Emailtext);
        mPassword = findViewById(R.id.passwordtext);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.buttonAccesso);


        mLoginBtn.setOnClickListener(new View.OnClickListener() {  // Inizio Funzionalità pulsante Login (quando lo clicchi)
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                // Inizio Messaggi Errore compilazione
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Inserire l'Email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Inserire la Password");
                    return;
                }
                // Fine Messaggi Errore compilazione

                // Metodo per l'autenticazione dell'utente
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Hai eseguito il login con successo", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), ListaOrganizzazioni.class));
                        } else {
                            Toast.makeText(Login.this, "Errore! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });         // Fine Funzionalità pulsante Login

    }   //  Fine onCreate

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

}

