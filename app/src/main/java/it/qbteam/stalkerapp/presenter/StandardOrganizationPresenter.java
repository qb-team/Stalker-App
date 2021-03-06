package it.qbteam.stalkerapp.presenter;

import java.io.IOException;
import it.qbteam.stalkerapp.contract.StandardOrganizationContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;
import it.qbteam.stalkerapp.model.service.Storage;

public class StandardOrganizationPresenter implements StandardOrganizationContract.Presenter {
    private Storage storage;

    public StandardOrganizationPresenter(){
        storage= new Storage(null,null);

    }

    @Override
    public OrganizationMovement getLastAccess(Long orgID) throws IOException, ClassNotFoundException {
        return storage.performGetLastAccess(orgID);
    }
}
