package android.StalkerAppPoCTracciamento;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;



public class MainActivity extends AppCompatActivity {

    private Button buttonL, buttonR;

    FirebaseAuth fAuth;         //  Punto d'acceso per l'autenticazione a Firebase
    FirebaseFirestore fStore;   //  Store di Firebase


    @Override
    protected void onCreate(Bundle savedInstanceState) { // Inizio onCreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() != null){     // Verifica se sei autenticato
            startActivity(new Intent(getApplicationContext(),ListaOrganizzazioni.class));
            finish();
        }
        buttonL = (Button) findViewById(R.id.buttonLogin);
        buttonL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityLogin();
            }
        });
        buttonR = (Button) findViewById(R.id.buttonRegistrati);
        buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityRegistrati();
            }
        });
    } // Fine onCreate

    public void openActivityLogin() {    // Apri interfaccia Login
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void openActivityRegistrati() {      // Apri interfaccia Registrati
        Intent intent = new Intent(this, Registrati.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {   // Funzionalità per il backbutton (tasto per andare indietro)
        // AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).
                setTitle("Attenzione").setMessage("Sei sicuro di uscire ?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dialog.dismiss();
            }
        }).
                setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).show();
    }

}

