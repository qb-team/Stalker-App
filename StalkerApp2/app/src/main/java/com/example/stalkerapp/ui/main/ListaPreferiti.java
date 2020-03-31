package com.example.stalkerapp.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.stalkerapp.HomePage;
import com.example.stalkerapp.R;

import java.util.ArrayList;

public class ListaPreferiti extends Fragment {
    ArrayAdapter<String> adapter;
    ArrayList<String> listaOrganizzazioni=new ArrayList<>();
    ListView listaOrg;
    private static ListaPreferiti instance = null;

    public final static String TAG="Preferiti_FRAGMENT";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;



    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fragment_lista_preferiti, container, false);

        System.out.println("1");
        listaOrg = view.findViewById(R.id.ListaOrg);

        return view;

    }
    public static ListaPreferiti getInstance() {
        return instance;
    }
    public void aggiungiPreferiti(String organizazzione){
   System.out.println("2");
        listaOrganizzazioni.add(organizazzione);
        adapter=new ArrayAdapter<>(getContext(),R.layout.row,R.id.textView2,listaOrganizzazioni);
        listaOrg.setAdapter(adapter);



    }
    public void stampaPreferiti(){
        adapter=new ArrayAdapter<>(getContext(),R.layout.row,R.id.textView2,listaOrganizzazioni);
        listaOrg.setAdapter(adapter);
    }
}