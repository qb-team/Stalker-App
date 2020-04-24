package it.qbteam.stalkerapp.presenter;

import androidx.fragment.app.Fragment;

import org.json.JSONException;

import it.qbteam.stalkerapp.model.service.Storage;
import it.qbteam.stalkerapp.model.data.Organization;

import java.io.IOException;
import java.util.ArrayList;

public class OrganizationsListPresenter implements OrganizationsListContract.Presenter {

    private OrganizationsListContract.View listaOrganizzazioniView;
    private Storage storage;

    public OrganizationsListPresenter(OrganizationsListContract.View listaOrganizzazioniView){
        this.listaOrganizzazioniView=listaOrganizzazioniView;

        storage=new Storage();
    }
    @Override
    public ArrayList<Organization> controlla(Fragment fragment, String nameFile) {
        return storage.performControllaLista(fragment, nameFile);
    }

    @Override
    public void scarica( Fragment fragment,  ArrayList<Organization> listaAttuale) throws InterruptedException {
        storage.performScaricaLista(fragment,listaAttuale);
    }

    @Override
    public void updateFile(ArrayList<Organization> list, Fragment fragment, String nameFile) throws IOException, JSONException {
        storage.performUpdateFile(list,fragment,nameFile);
    }

   /* @Override
    public void onFailure(String message) {
        listaOrganizzazioniView.onLoadListFailure(message);
    }*/
}
