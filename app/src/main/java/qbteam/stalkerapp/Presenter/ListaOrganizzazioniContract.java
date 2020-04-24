package qbteam.stalkerapp.Presenter;

import androidx.fragment.app.Fragment;

import org.json.JSONException;

import qbteam.stalkerapp.Organizzazioni;

import java.io.IOException;
import java.util.ArrayList;

public interface ListaOrganizzazioniContract {

    interface View {

        void onLoadListFailure(String message);

    }

    //METODO DEL PRESENTER CHE VA A CHIAMARE IL METODO DELL' INTRACTOR DEL MODELLO
    interface Presenter {
        ArrayList<Organizzazioni> controlla(Fragment fragment,  String nameFile);
        void scarica(Fragment fragment, ArrayList<Organizzazioni> listaAttuale) throws InterruptedException;
        void updateFile(ArrayList<Organizzazioni> list, Fragment fragment,String nameFile) throws IOException, JSONException;
    }

    //METODO DEL MODELLO
    interface Intractor {
        ArrayList<Organizzazioni>  performControllaLista(Fragment fragment, String nameFile);
        void performScaricaLista(Fragment fragment, ArrayList<Organizzazioni> listaAttuale) throws InterruptedException;
        void performUpdateFile(ArrayList<Organizzazioni> list, Fragment fragment,String nameFile) throws JSONException, IOException;
    }

    interface ListaOrganizzazioniListener {

        void onFailure(String message);
    }
}
