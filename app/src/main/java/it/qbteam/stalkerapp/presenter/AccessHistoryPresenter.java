package it.qbteam.stalkerapp.presenter;


import java.io.IOException;
import java.util.List;

import it.qbteam.stalkerapp.contract.AccessHistoryContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationAccess;
import it.qbteam.stalkerapp.model.service.Server;
import it.qbteam.stalkerapp.model.service.Storage;

public class AccessHistoryPresenter implements AccessHistoryContract.Presenter, AccessHistoryContract.AccessHistoryListener {

    private AccessHistoryContract.View accessHistoryView;
    private Storage storage;
    private Server server;
    public AccessHistoryPresenter(AccessHistoryContract.View accessHistoryView){
            this.accessHistoryView=accessHistoryView;
            server= new Server(null,null, this);
            storage= new Storage(null,null, this);

    }


    @Override
    public void getOrganizationAccess() throws IOException, ClassNotFoundException {
            storage.deserializeOrganizationAccessInLocal();
    }

    @Override
    public List<OrganizationAccess> getOrganizationAccessList() throws IOException, ClassNotFoundException {
            return storage.performGetAccessList();
    }


    @Override
    public void onSuccessGetOrganizationAccess(List<OrganizationAccess> organizationAccessList) {
            accessHistoryView.onSuccessGetOrganizationAccessInLocal(organizationAccessList);
    }

    @Override
    public void deleteOrganizationAccess() throws IOException {
            storage.performDeleteOrganizationAccess();
    }

    @Override
    public void onSuccessDelete() {
            accessHistoryView.onSuccessDeleteOrganizationAccess();
    }
}
