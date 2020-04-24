package qbteam.stalkerapp.presenter;

import androidx.fragment.app.Fragment;

import org.json.JSONException;

import qbteam.stalkerapp.model.data.Organization;

import java.io.IOException;
import java.util.ArrayList;

public interface ListaOrganizzazioniContract {

    interface View {

        void onLoadListFailure(String message);

    }

    //METODO DEL PRESENTER CHE VA A CHIAMARE IL METODO DELL' Model DEL MODELLO
    interface Presenter {
        ArrayList<Organization> controlla(Fragment fragment, String nameFile);
        void scarica(Fragment fragment, ArrayList<Organization> listaAttuale) throws InterruptedException;
        void updateFile(ArrayList<Organization> list, Fragment fragment, String nameFile) throws IOException, JSONException;
    }

    //METODO DEL MODELLO
    interface Model {
        ArrayList<Organization>  performControllaLista(Fragment fragment, String nameFile);
        void performScaricaLista(Fragment fragment, ArrayList<Organization> listaAttuale) throws InterruptedException;
        void performUpdateFile(ArrayList<Organization> list, Fragment fragment, String nameFile) throws JSONException, IOException;
    }

    interface ListaOrganizzazioniListener {

        void onFailure(String message);
    }
}
