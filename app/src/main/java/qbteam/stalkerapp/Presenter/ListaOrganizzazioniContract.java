package qbteam.stalkerapp.Presenter;

import androidx.fragment.app.Fragment;

import qbteam.stalkerapp.Organizzazioni;

import java.util.ArrayList;

public interface ListaOrganizzazioniContract {

    interface View {

        void onLoadListFailure(String message);
    }

    //METODO DEL PRESENTER CHE VA A CHIAMARE IL METODO DELL' INTRACTOR DEL MODELLO
    interface Presenter {
        ArrayList<Organizzazioni> controlla(Fragment fragment);
        void scarica(Fragment fragment, ArrayList<Organizzazioni> listaAttuale) throws InterruptedException;
    }

    //METODO DEL MODELLO
    interface Intractor {
        ArrayList<Organizzazioni> performControllaLista(Fragment fragment);
        void performScaricaLista(Fragment fragment, ArrayList<Organizzazioni> listaAttuale) throws InterruptedException;
    }

    interface ListaOrganizzazioniListener {

        void onFailure(String message);
    }
}
