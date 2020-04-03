package com.example.stalkerapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.stalkerapp.HomePage;
import com.example.stalkerapp.MainActivity;
import com.example.stalkerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import java.io.IOException;

public class Settings extends RootFragment {
    public final static String TAG="Settings_FRAGMENT";

    Button logout;
    Button scarica;
    Button tracciamento;


    public Settings() {
        //require empty constructor
    }

    public static Settings newInstance() {
        return new Settings();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        logout = view.findViewById(R.id.LogoutID);
        scarica = view.findViewById(R.id.ScaricaID);
        tracciamento = view.findViewById(R.id.TracciamentoID);


        scarica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    HomeFragment.getInstance().StampaAschermo();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }



        });
        tracciamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Organizzazione.getInstance().mostraCoordinate();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {  // Inizio Funzionalità pulsante Login (quando lo clicchi)
            @Override
            public void onClick(View v) {
                MainActivity.getInstance().clearStack();
                getActivity().finish();
                FirebaseAuth.getInstance().signOut();//logout
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }

        });
        return view;
    }
}