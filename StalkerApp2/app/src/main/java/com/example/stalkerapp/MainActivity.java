package com.example.stalkerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.stalkerapp.ui.main.Login;
import com.example.stalkerapp.ui.main.MainFragment;
import com.example.stalkerapp.ui.main.Registrati;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
public static FragmentManager fragmentManager;
    FirebaseAuth fAuth;         //  Punto d'acceso per l'autenticazione a Firebase
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        fragmentManager=getSupportFragmentManager();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() != null){     // Verifica se sei autenticato
            startActivity(new Intent(getApplicationContext(),HomePage.class));
            finish();
        }
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.container,
                    new MainFragment()).commit();
        }

    }



}
