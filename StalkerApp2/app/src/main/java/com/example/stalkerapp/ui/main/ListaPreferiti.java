package com.example.stalkerapp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.stalkerapp.HomePage;
import com.example.stalkerapp.R;

public class ListaPreferiti extends Fragment {
    public final static String TAG="Preferiti_FRAGMENT";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        HomePage.getInstance().getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        return inflater.inflate(R.layout.fragment_lista_preferiti, container, false);
    }
}