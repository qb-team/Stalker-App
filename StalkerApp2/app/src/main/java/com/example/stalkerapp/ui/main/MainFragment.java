package com.example.stalkerapp.ui.main;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.stalkerapp.MainActivity;
import com.example.stalkerapp.R;



public class MainFragment extends Fragment {
    public final static String TAG="Main_Fragment";

    Button registrati;
    Button login;
    TextView t;
    private static MainFragment instance = null;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment getInstance() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.fragment_main,container,false);
        registrati= view.findViewById(R.id.buttonRegistrati);
        login= view.findViewById(R.id.buttonLogin);
        t=view.findViewById(R.id.textView);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.fragmentManager.findFragmentByTag("Login_Fragment") != null)
                    MainActivity.fragmentManager.beginTransaction().show(MainActivity.fragmentManager.findFragmentByTag("Login_Fragment")).addToBackStack(null).commit();
                else MainActivity.fragmentManager.beginTransaction().add(R.id.container, new Login(), "Login_Fragment").addToBackStack(null).commit();
                if(MainActivity.fragmentManager.findFragmentByTag("Registrati_Fragment") != null)
                MainActivity.fragmentManager.beginTransaction().hide(MainActivity.fragmentManager.findFragmentByTag("Registrati_Fragment")).commit();
                if(MainActivity.fragmentManager.findFragmentByTag("Main_Fragment") != null)
                    MainActivity.fragmentManager.beginTransaction().hide(MainActivity.fragmentManager.findFragmentByTag("Main_Fragment")).commit();
            }
        });
        registrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MainActivity.fragmentManager.findFragmentByTag("Registrati_Fragment") != null)
                    MainActivity.fragmentManager.beginTransaction().show(MainActivity.fragmentManager.findFragmentByTag("Registrati_Fragment")).addToBackStack(null).commit();
                else MainActivity.fragmentManager.beginTransaction().add(R.id.container, new Registrati(), "Registrati_Fragment").addToBackStack(null).commit();
                if(MainActivity.fragmentManager.findFragmentByTag("Login_Fragment") != null)
                MainActivity.fragmentManager.beginTransaction().hide(MainActivity.fragmentManager.findFragmentByTag("Login_Fragment")).commit();
                if(MainActivity.fragmentManager.findFragmentByTag("Main_Fragment") != null)
                    MainActivity.fragmentManager.beginTransaction().hide(MainActivity.fragmentManager.findFragmentByTag("Main_Fragment")).commit();

            }
        });
        return view;
    }


}

