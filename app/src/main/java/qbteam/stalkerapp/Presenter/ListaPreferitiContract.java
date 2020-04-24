package qbteam.stalkerapp.Presenter;
import androidx.fragment.app.Fragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import qbteam.stalkerapp.Organizzazioni;


public interface ListaPreferitiContract {
    interface View {

        void onLoadListFailure(String message);
    }

    //METODO DEL PRESENTER CHE VA A CHIAMARE IL METODO DELL' INTRACTOR DEL MODELLO
    interface Presenter {
        ArrayList<Organizzazioni> controlla(Fragment fragment,String nameFile);
        ArrayList<Organizzazioni> rimuovi(String name,ArrayList<Organizzazioni> list);
        void updateFile(ArrayList<Organizzazioni> list, Fragment fragment, String nameFile) throws IOException, JSONException;
    }

    //METODO DEL MODELLO
    interface Intractor {
        ArrayList<Organizzazioni> performControllaLista(Fragment fragment, String nameFile);
        ArrayList<Organizzazioni> performRimuovi(String name, ArrayList<Organizzazioni> list);
        void performUpdateFile(ArrayList<Organizzazioni> list, Fragment fragment, String nameFile) throws JSONException, IOException;
    }
    interface ListaPreferitiListener {

        void onFailure(String message);
    }

}
