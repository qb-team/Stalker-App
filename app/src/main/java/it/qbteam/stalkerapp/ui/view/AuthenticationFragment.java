package it.qbteam.stalkerapp.ui.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import it.qbteam.stalkerapp.MainActivity;
import it.qbteam.stalkerapp.R;

//Schermata iniziale per gli utenti non autenticati
public class AuthenticationFragment extends Fragment {

    public final static String TAG = "Main_Fragment";
    final Fragment loginFragment = new LoginFragment();
    final Fragment signUpFragment = new SignUpFragment();
    Button signUpButton;
    Button loginButton;

    //Creation of the fragment as a component.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Creation of the graphic part displayed by the user.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.fragmentManager.beginTransaction().add(R.id.containerID, loginFragment).hide(loginFragment).commit();
        MainActivity.fragmentManager.beginTransaction().add(R.id.containerID, signUpFragment).hide(signUpFragment).commit();
        View view = inflater.inflate(R.layout.fragment_authentication,container,false);
        signUpButton = view.findViewById(R.id.buttonSignUpID);
        loginButton = view.findViewById(R.id.buttonLoginID);

        //Click on Login button and than it will show the loginFragment
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.beginTransaction().hide(MainActivity.fragmentManager.findFragmentByTag("Main")).show(loginFragment).addToBackStack(null).commit();
            }
        });

        //Click on SignUp button and than it will show the signUpFragment
        signUpButton.setOnClickListener(v -> MainActivity.fragmentManager.beginTransaction().hide(MainActivity.fragmentManager.findFragmentByTag("Main")).show(signUpFragment).addToBackStack(null).commit());
        return view;
    }
}

