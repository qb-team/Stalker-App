package it.qbteam.stalkerapp.contract;

import java.io.IOException;
import java.util.List;

import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationAccess;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;

public interface AccessHistoryContract {
    interface View {
        void onSuccessGetOrganizationAccessInLocal(List<OrganizationAccess> organizationAccessList);

    }

    interface Presenter {

     void getOrganizationAccess() throws IOException, ClassNotFoundException;


    }

    interface Interactor {

    }

    interface AccessHistoryListener {
        void onSuccessGetOrganizationAccess(List<OrganizationAccess> organizationAccessList);

    }
}
