package com.example.stalkerapp.Presenter;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public interface ListaPreferitiContract {

    //METODO DEL PRESENTER CHE VA A CHIAMARE IL METODO DELL' INTRACTOR DEL MODELLO
    interface Presenter {
        ArrayList<String> controlla(Fragment fragment);
    }

    //METODO DEL MODELLO
    interface Intractor {
        ArrayList<String> performControllaLista(Fragment fragment);
    }


}
