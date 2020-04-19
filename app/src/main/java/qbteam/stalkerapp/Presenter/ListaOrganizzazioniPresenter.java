package qbteam.stalkerapp.Presenter;

import androidx.fragment.app.Fragment;

import qbteam.stalkerapp.Model.ListaOrganizzazioniModel;
import qbteam.stalkerapp.Organizzazioni;

import java.util.ArrayList;

public class ListaOrganizzazioniPresenter implements ListaOrganizzazioniContract.Presenter, ListaOrganizzazioniContract.ListaOrganizzazioniListener {
    private ListaOrganizzazioniModel listaOrganizzazioniModel;
    private ListaOrganizzazioniContract.View listaOrganizzazioniView;

    public ListaOrganizzazioniPresenter(ListaOrganizzazioniContract.View listaOrganizzazioniView){
        this.listaOrganizzazioniView=listaOrganizzazioniView;
        listaOrganizzazioniModel= new ListaOrganizzazioniModel(this);
    }
    @Override
    public ArrayList<Organizzazioni> controlla(Fragment fragment) {
        return listaOrganizzazioniModel.performControllaLista(fragment);
    }

    @Override
    public void scarica( Fragment fragment,  ArrayList<Organizzazioni> listaAttuale) throws InterruptedException {
        listaOrganizzazioniModel.performScaricaLista(fragment,listaAttuale);
    }

    @Override
    public void onFailure(String message) {
        listaOrganizzazioniView.onLoadListFailure(message);
    }
}
