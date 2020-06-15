package it.qbteam.stalkerapp.contract;

import java.io.IOException;
import java.util.List;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationAccess;

public interface AccessHistoryContract {
    interface View {
        void onSuccessGetOrganizationAccessInLocal(List<OrganizationAccess> organizationAccessList);
        void onSuccessDeleteOrganizationAccess();
        void onFailureGetOrganizationAccessInLocal();

    }

    interface Presenter {

     void getOrganizationAccess() throws IOException, ClassNotFoundException;
     void deleteOrganizationAccess(List<OrganizationAccess> accessList) throws IOException, ClassNotFoundException;


    }

    interface AccessHistoryListener {
        void onFailureGetOrganizationAccess();
        void onSuccessGetOrganizationAccess(List<OrganizationAccess> organizationAccessList);
        void onSuccessDelete();

    }
}
