package qbteam.stalkerapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import qbteam.stalkerapp.HomePage;
import qbteam.stalkerapp.MainActivity;
import qbteam.stalkerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import java.io.IOException;
import java.util.Collections;

public class Settings extends RootFragment {
    public final static String TAG="Settings_FRAGMENT";
    private static Settings instance = null;
    Button logout;
    Button tracciamento;
    Button alfabetico;






    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("Creazione Settings");
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        logout = view.findViewById(R.id.LogoutID);
        tracciamento = view.findViewById(R.id.TracciamentoID);
        alfabetico = view.findViewById(R.id.VisualizzazioneID);


        tracciamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Organizzazione.getInstance().mostraCoordinate();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {  // Inizio Funzionalità pulsante Login (quando lo clicchi)
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();   //logout
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }

        });

//        alfabetico.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                HomeFragment.getInstance().OrdinamentoAlfabetico();
//            }
//        });


        return view;
    }

}