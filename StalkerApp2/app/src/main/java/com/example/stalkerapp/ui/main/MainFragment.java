package com.example.stalkerapp.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.stalkerapp.MainActivity;
import com.example.stalkerapp.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    public final static String TAG="Main_Fragment";

    Button registrati;
    Button login;
    TextView t;


    public MainFragment() {
        // Required empty public constructor
    }



    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

                MainActivity.fragmentManager.beginTransaction().replace(R.id.mainFragment,new Login(),null).addToBackStack(null).commit();
            }
        });
        registrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.fragmentManager.beginTransaction().replace(R.id.mainFragment,new Registrati(),null).addToBackStack(null).commit();
            }
        });
        return view;
    }


}

