package it.qbteam.stalkerapp.presenter;
import androidx.fragment.app.Fragment;

import org.json.JSONException;

import it.qbteam.stalkerapp.model.service.Storage;
import it.qbteam.stalkerapp.model.data.Organization;

import java.io.IOException;
import java.util.ArrayList;

public class MyStalkersListPresenter implements MyStalkersListContract.Presenter, MyStalkersListContract.ListaPreferitiListener{


private MyStalkersListContract.View listaPreferitiView;
private Storage storage;

public MyStalkersListPresenter(MyStalkersListContract.View listaPreferitiView){
    this.listaPreferitiView=listaPreferitiView;

    storage= new Storage();
}

    @Override
    public ArrayList<Organization> controlla(Fragment fragment, String nameFile) {
       return storage.performControllaLista(fragment, nameFile);

    }


    @Override
    public ArrayList<Organization> rimuovi(String name, ArrayList<Organization> list) {
        return storage.performRimuovi(name,list);
    }

    @Override
    public void updateFile(ArrayList<Organization> list, Fragment fragment, String nameFile) throws IOException, JSONException {
   storage.performUpdateFile(list,fragment , nameFile);
    }

    @Override
    public String getOrganizationType(Organization organization) {
        return organization.getType();
    }


    @Override
    public void onFailure(String message) {

    }
}
