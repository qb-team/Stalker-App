package it.qbteam.stalkerapp.presenter;

import java.util.HashMap;

import it.qbteam.stalkerapp.contract.StandardOrganizationContract;
import it.qbteam.stalkerapp.model.service.Storage;

public class StandardOrganizationPresenter implements StandardOrganizationContract.Presenter, StandardOrganizationContract.Listener {
    private StandardOrganizationContract.View StandardOrganizationView;
    private Storage storage;

    public StandardOrganizationPresenter(StandardOrganizationContract.View StandardOrganizationView){
        this.StandardOrganizationView = StandardOrganizationView;
        storage = new Storage(this,null);
    }

    @Override
    public HashMap<String, String> desAccessExitInLocal() {
        return null;
    }

    @Override
    public void onStandardOrganizationListener() {

    }
}
