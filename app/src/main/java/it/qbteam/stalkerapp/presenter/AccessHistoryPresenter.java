package it.qbteam.stalkerapp.presenter;

import java.io.IOException;
import java.util.List;

import it.qbteam.stalkerapp.contract.AccessHistoryContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationAccess;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;
import it.qbteam.stalkerapp.model.service.Server;
import it.qbteam.stalkerapp.model.service.Storage;

public class AccessHistoryPresenter implements AccessHistoryContract.Presenter, AccessHistoryContract.AccessHistoryListener {

    private Server server;
    private AccessHistoryContract.View accessHistoryView;
    private Storage storage;
    public AccessHistoryPresenter(AccessHistoryContract.View accessHistoryView){
        this.accessHistoryView=accessHistoryView;
        server= new Server(null,null);

    }

    @Override
    public List<OrganizationAccess> getAnonymousOrganizationAccess(String exitToken, Long orgID) throws IOException {
        return server.getAnonymousOrganizationAccess(exitToken, orgID);
    }


    @Override
    public OrganizationMovement getOrganizationMovement() throws IOException, ClassNotFoundException {
        return storage.deserializeMovementInLocal();
    }
    @Override
    public void onSuccessDownloadAccess(List<OrganizationAccess> list) {

    }

}
