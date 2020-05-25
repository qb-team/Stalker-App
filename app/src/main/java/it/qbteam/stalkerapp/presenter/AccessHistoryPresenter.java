package it.qbteam.stalkerapp.presenter;

import org.mockito.internal.matchers.Or;

import java.io.IOException;
import java.util.List;

import it.qbteam.stalkerapp.contract.AccessHistoryContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationAccess;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;
import it.qbteam.stalkerapp.model.service.Server;
import it.qbteam.stalkerapp.model.service.Storage;

public class AccessHistoryPresenter implements AccessHistoryContract.Presenter, AccessHistoryContract.AccessHistoryListener {

    private AccessHistoryContract.View accessHistoryView;
    private Storage storage;
    private Server server;
    public AccessHistoryPresenter(AccessHistoryContract.View accessHistoryView){
        this.accessHistoryView=accessHistoryView;
        server= new Server(null,null, this);
        storage= new Storage(null,null);

    }

    @Override
    public void getAnonymousOrganizationAccess(String exitToken, Long orgID) throws IOException {
         server.anonymousOrganizationAccess(exitToken, orgID);
    }


    @Override
    public OrganizationMovement getOrganizationMovement() throws IOException, ClassNotFoundException {
        return storage.deserializeMovementInLocal();
    }

    @Override
    public void getOrganizationName(Long orgID) {
        server.performGetOrganization(orgID);
    }

    @Override
    public void onSuccessDownloadAccess(OrganizationAccess organizationAccess) {
      accessHistoryView.onSuccessDownloadAccess(organizationAccess);
    }

    @Override
    public void onSuccessGetOrganizationName(String orgName) {
        accessHistoryView.onSuccessGetOrganizationName(orgName);
    }

}
