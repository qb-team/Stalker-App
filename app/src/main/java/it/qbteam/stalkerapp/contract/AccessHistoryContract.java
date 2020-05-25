package it.qbteam.stalkerapp.contract;

import org.mockito.internal.matchers.Or;

import java.io.IOException;
import java.util.List;

import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationAccess;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;

public interface AccessHistoryContract {
    interface View {
        void onSuccessGetOrganizationAccessInLocal(List<OrganizationAccess> organizationAccessList);
        void onSuccessDeleteOrganizationAccess();

    }

    interface Presenter {

     void getOrganizationAccess() throws IOException, ClassNotFoundException;
     List<OrganizationAccess> getOrganizationAccessList() throws IOException, ClassNotFoundException;
     void deleteOrganizationAccess() throws IOException;


    }

    interface Interactor {

    }

    interface AccessHistoryListener {
        void onSuccessGetOrganizationAccess(List<OrganizationAccess> organizationAccessList);
        void onSuccessDelete();

    }
}
