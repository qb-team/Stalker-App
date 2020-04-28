package it.qbteam.stalkerapp.presenter;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import it.qbteam.stalkerapp.model.service.Storage;
import it.qbteam.stalkerapp.model.data.Organization;
import java.io.IOException;
import java.util.ArrayList;

public class HomePresenter implements HomeContract.Presenter, HomeContract.HomeListener {

    private HomeContract.View listaOrganizzazioniView;
    private Storage storage;

    public HomePresenter(HomeContract.View listaOrganizzazioniView){
        this.listaOrganizzazioniView=listaOrganizzazioniView;

        storage=new Storage(this,null);
    }
    @Override
    public void controlla(Fragment fragment, String nameFile) {
        storage.performControllaLista(fragment, nameFile);
    }

    @Override
    public void scarica( Fragment fragment,  ArrayList<Organization> listaAttuale) throws InterruptedException {
        storage.performScaricaLista(fragment,listaAttuale);
    }

    @Override
    public void updateFile(ArrayList<Organization> list, Fragment fragment, String nameFile) throws IOException, JSONException {
        storage.performUpdateFile(list,fragment,nameFile);
    }

    @Override
    public String getOrganizationType(Organization organization) {
        return organization.getType();
    }

    @Override
    public void onSuccessFile(ArrayList<Organization> list) {
        listaOrganizzazioniView.onSuccessCheckFile(list);
    }

    @Override
    public void onFailureFile(String message) {
        listaOrganizzazioniView.onFailureCheckFile(message);
    }


}
