package it.qbteam.stalkerapp.presenter;
import androidx.fragment.app.Fragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import it.qbteam.stalkerapp.model.data.Organization;


public interface MyStalkersListContract {
    interface View {

        void onLoadListFailure(String message);
    }

    //METODO DEL PRESENTER CHE VA A CHIAMARE IL METODO DELL' Model DEL MODELLO
    interface Presenter {
        ArrayList<Organization> controlla(Fragment fragment, String nameFile);
        ArrayList<Organization> rimuovi(String name, ArrayList<Organization> list);
        void updateFile(ArrayList<Organization> list, Fragment fragment, String nameFile) throws IOException, JSONException;
        String getOrganizationType(Organization organization);
    }

    //METODO DEL MODELLO
    interface Model {
        ArrayList<Organization> performControllaLista(Fragment fragment, String nameFile);
        ArrayList<Organization> performRimuovi(String name, ArrayList<Organization> list);
        void performUpdateFile(ArrayList<Organization> list, Fragment fragment, String nameFile) throws JSONException, IOException;
    }
    interface ListaPreferitiListener {

        void onFailure(String message);
    }

}
