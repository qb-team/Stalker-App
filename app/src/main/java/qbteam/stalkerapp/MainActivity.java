package qbteam.stalkerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import qbteam.stalkerapp.ui.main.MainFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


//Activity principale che ti porta nella seconda activity (HomePage) se si è autenticati, altrimenti ti indirizza al MainFragment
public class MainActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {// Inizializzazione dei comandi supportati da Firebase
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        fragmentManager=getSupportFragmentManager();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() != null){ // Verifica se sei autenticato
            Intent intent = new Intent(getApplicationContext(),HomePage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //finish();
        }
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.container, new MainFragment(), "Main").commit();
        }

    }



}
