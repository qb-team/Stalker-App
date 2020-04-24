package qbteam.stalkerapp.Presenter;

import androidx.fragment.app.Fragment;

import org.json.JSONException;

import qbteam.stalkerapp.Model.service.Storage;
import qbteam.stalkerapp.Organizzazioni;

import java.io.IOException;
import java.util.ArrayList;

public class ListaOrganizzazioniPresenter implements ListaOrganizzazioniContract.Presenter {

    private ListaOrganizzazioniContract.View listaOrganizzazioniView;
    private Storage storage;

    public ListaOrganizzazioniPresenter(ListaOrganizzazioniContract.View listaOrganizzazioniView){
        this.listaOrganizzazioniView=listaOrganizzazioniView;

        storage=new Storage();
    }
    @Override
    public ArrayList<Organizzazioni> controlla(Fragment fragment,  String nameFile) {
        return storage.performControllaLista(fragment, nameFile);
    }

    @Override
    public void scarica( Fragment fragment,  ArrayList<Organizzazioni> listaAttuale) throws InterruptedException {
        storage.performScaricaLista(fragment,listaAttuale);
    }

    @Override
    public void updateFile(ArrayList<Organizzazioni> list, Fragment fragment, String nameFile) throws IOException, JSONException {
        storage.performUpdateFile(list,fragment,nameFile);
    }

   /* @Override
    public void onFailure(String message) {
        listaOrganizzazioniView.onLoadListFailure(message);
    }*/
}
