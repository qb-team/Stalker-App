package it.qbteam.stalkerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import it.qbteam.stalkerapp.ui.view.AuthenticationFragment;
import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


//Activity principale che ti porta nella seconda activity (HomePage) se si è autenticati, altrimenti ti indirizza al MainFragment
public class MainActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onStart() {
        super.onStart();

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null ) {
            goToHomePage();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {// Inizializzazione dei comandi supportati da Firebase
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, new AuthenticationFragment(), "Main").commit();


    }
    public void goToHomePage(){

        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



}
