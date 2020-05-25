package it.qbteam.stalkerapp.contract;

import java.io.IOException;
import java.util.List;

import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationAccess;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;

public interface AccessHistoryContract {
    interface View {
        void onSuccessDownloadAccess(OrganizationAccess organizationAccess);
        void onSuccessGetOrganizationName(String orgName);

    }

    interface Presenter {

        void getAnonymousOrganizationAccess(String exitToken, Long orgID) throws IOException, ClassNotFoundException;
        OrganizationMovement getOrganizationMovement() throws IOException, ClassNotFoundException;
        void getOrganizationName(Long orgID);



    }

    interface Interactor {

    }

    interface AccessHistoryListener {
        void onSuccessDownloadAccess(OrganizationAccess organizationAccess);
        void onSuccessGetOrganizationName(String orgName);

    }
}
