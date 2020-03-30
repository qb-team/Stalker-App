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

    final Fragment Login = new Login();
    final Fragment Registrati = new Registrati();

    Button registrati;
    Button login;
    TextView t;


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.fragmentManager.beginTransaction().add(R.id.container, Login).hide(Login).commit();
        MainActivity.fragmentManager.beginTransaction().add(R.id.container, Registrati).hide(Registrati).commit();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        container.removeAllViews();
        View view=inflater.inflate(R.layout.fragment_main,container,false);
        registrati= view.findViewById(R.id.buttonRegistrati);
        login= view.findViewById(R.id.buttonLogin);
        t=view.findViewById(R.id.textView);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.fragmentManager.beginTransaction().hide(MainActivity.fragmentManager.findFragmentByTag("Main")).show(Login).addToBackStack(null).commit();
            }
        });
        registrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.fragmentManager.beginTransaction().hide(MainActivity.fragmentManager.findFragmentByTag("Main")).show(Registrati).addToBackStack(null).commit();
                
            }
        });
        return view;
    }


}

