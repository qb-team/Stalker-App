package it.qbteam.stalkerapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import it.qbteam.stalkerapp.view.authentication.AuthenticationFragment;


public class AuthenticationActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onStart() {
        super.onStart();
        fragmentManager=getSupportFragmentManager();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() != null){             // Se sei autenticato si passa alla lista delle organizzazioni (Main Activity)
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_activity_layout);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AuthenticationFragment fragment = new AuthenticationFragment();
        transaction.replace(R.id.AuthenticatorContainerID, fragment);
        transaction.commit();

    }
}