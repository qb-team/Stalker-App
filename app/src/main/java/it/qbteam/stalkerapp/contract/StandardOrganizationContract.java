package it.qbteam.stalkerapp.contract;

import java.io.IOException;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;

public interface StandardOrganizationContract {
    interface View {

    }

    interface Presenter {
        OrganizationMovement getLastAccess(Long orgID) throws IOException, ClassNotFoundException;
    }

    interface Interactor {

    }

    interface Listener {

    }
}
