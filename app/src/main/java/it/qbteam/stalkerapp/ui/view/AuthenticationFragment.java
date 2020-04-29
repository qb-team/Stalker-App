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

    final Fragment loginFragment = new LoginFragment();
    final Fragment signUpFragment = new SignUpFragment();

    Button signUpButton;
    Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.fragmentManager.beginTransaction().add(R.id.containerID, loginFragment).hide(loginFragment).commit();
        MainActivity.fragmentManager.beginTransaction().add(R.id.containerID, signUpFragment).hide(signUpFragment).commit();
        View view = inflater.inflate(R.layout.fragment_authentication,container,false);
        signUpButton = view.findViewById(R.id.buttonSignUpID);
        loginButton = view.findViewById(R.id.buttonLoginID);

        loginButton.setOnClickListener(new View.OnClickListener() {//Dopo aver clickato il pulsante "loginFragment" l'utente viene indirizzato nell'apposita schermata di loginButton
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.beginTransaction().hide(MainActivity.fragmentManager.findFragmentByTag("Main")).show(loginFragment).addToBackStack(null).commit();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {//Dopo aver clickato il pulsante "signUpFragment" l'utente viene indirizzato nell'apposita schermata di registrazione
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.beginTransaction().hide(MainActivity.fragmentManager.findFragmentByTag("Main")).show(signUpFragment).addToBackStack(null).commit();
            }
        });
        return view;
    }
}

