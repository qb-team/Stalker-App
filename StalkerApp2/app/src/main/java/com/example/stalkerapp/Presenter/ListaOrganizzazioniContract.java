package com.example.stalkerapp.Presenter;

import androidx.fragment.app.Fragment;

import com.example.stalkerapp.Organizzazioni;

import java.util.ArrayList;

public interface ListaOrganizzazioniContract {

    interface View {

        void onLoadListFailure(String message);
    }

    //METODO DEL PRESENTER CHE VA A CHIAMARE IL METODO DELL' INTRACTOR DEL MODELLO
    interface Presenter {
        ArrayList<Organizzazioni> controlla(Fragment fragment);
        ArrayList<Organizzazioni> aggiorna(Fragment fragment, ArrayList<Organizzazioni> listaAttuale) throws InterruptedException;
    }

    //METODO DEL MODELLO
    interface Intractor {
        ArrayList<Organizzazioni> performControllaLista(Fragment fragment);
        ArrayList<Organizzazioni> performAggiornaLista(Fragment fragment, ArrayList<Organizzazioni> listaAttuale) throws InterruptedException;
    }

    interface ListaOrganizzazioniListener {

        void onFailure(String message);
    }
}
