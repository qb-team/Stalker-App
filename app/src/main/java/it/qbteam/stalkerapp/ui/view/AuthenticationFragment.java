package it.qbteam.stalkerapp.ui.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import it.qbteam.stalkerapp.MainActivity;
import it.qbteam.stalkerapp.R;

//Schermata iniziale per gli utenti non autenticati
public class AuthenticationFragment extends Fragment {
    public final static String TAG="Main_Fragment";

    final Fragment Login = new Login();
    final Fragment Registrati = new Registration();

    Button registrati;
    Button login;
    TextView t;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.fragmentManager.beginTransaction().add(R.id.container, Login).hide(Login).commit();
        MainActivity.fragmentManager.beginTransaction().add(R.id.container, Registrati).hide(Registrati).commit();
        View view = inflater.inflate(R.layout.fragment_authentication,container,false);
        registrati = view.findViewById(R.id.buttonRegistrati);
        login = view.findViewById(R.id.buttonLogin);
        t = view.findViewById(R.id.textView);

        login.setOnClickListener(new View.OnClickListener() {//Dopo aver clickato il pulsante "Login" l'utente viene indirizzato nell'apposita schermata di login
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.beginTransaction().hide(MainActivity.fragmentManager.findFragmentByTag("Main")).show(Login).addToBackStack(null).commit();
            }
        });

        registrati.setOnClickListener(new View.OnClickListener() {//Dopo aver clickato il pulsante "Registrati" l'utente viene indirizzato nell'apposita schermata di registrazione
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.beginTransaction().hide(MainActivity.fragmentManager.findFragmentByTag("Main")).show(Registrati).addToBackStack(null).commit();
            }
        });
        return view;
    }
}

