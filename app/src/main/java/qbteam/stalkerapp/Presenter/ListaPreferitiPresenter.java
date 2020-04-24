package qbteam.stalkerapp.Presenter;
import androidx.fragment.app.Fragment;

import org.json.JSONException;

import qbteam.stalkerapp.Model.service.Storage;
import qbteam.stalkerapp.Organizzazioni;

import java.io.IOException;
import java.util.ArrayList;

public class ListaPreferitiPresenter implements ListaPreferitiContract.Presenter,ListaPreferitiContract.ListaPreferitiListener{


private ListaPreferitiContract.View listaPreferitiView;
private Storage storage;

public ListaPreferitiPresenter(ListaPreferitiContract.View listaPreferitiView){
    this.listaPreferitiView=listaPreferitiView;

    storage= new Storage();
}

    @Override
    public ArrayList<Organizzazioni> controlla(Fragment fragment,  String nameFile) {
       return storage.performControllaLista(fragment, nameFile);

    }


    @Override
    public ArrayList<Organizzazioni> rimuovi(String name, ArrayList<Organizzazioni> list) {
        return storage.performRimuovi(name,list);
    }

    @Override
    public void updateFile(ArrayList<Organizzazioni> list, Fragment fragment, String nameFile) throws IOException, JSONException {
   storage.performUpdateFile(list,fragment , nameFile);
    }


    @Override
    public void onFailure(String message) {

    }
}
