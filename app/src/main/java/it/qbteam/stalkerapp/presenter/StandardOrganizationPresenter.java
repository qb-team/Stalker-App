package it.qbteam.stalkerapp.presenter;


import java.io.IOException;
import java.util.List;

import it.qbteam.stalkerapp.contract.StandardOrganizationContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;
import it.qbteam.stalkerapp.model.service.Server;
import it.qbteam.stalkerapp.model.service.Storage;

public class StandardOrganizationPresenter implements StandardOrganizationContract.Presenter {
    private StandardOrganizationContract.View StandardOrganizationView;
    private Server server;
    private Storage storage;

    public StandardOrganizationPresenter(StandardOrganizationContract.View StandardOrganizationView){
        this.StandardOrganizationView = StandardOrganizationView;
        server= new Server(null,null);
        storage= new Storage(null,null);


    }

    @Override
    public void anonymousOrganizationAccess(String exitToken, Long orgID)  {
        server.anonymousOrganizationAccess(exitToken, orgID);
    }


    @Override
    public OrganizationMovement getOrganizationMovement() throws IOException, ClassNotFoundException {
        return storage.deserializeMovementInLocal();
    }
}
