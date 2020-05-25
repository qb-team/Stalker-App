package it.qbteam.stalkerapp.contract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationAccess;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;
import it.qbteam.stalkerapp.model.backend.dataBackend.PlaceAccess;

public interface StandardOrganizationContract {
    interface View {

    }

    interface Presenter {
        void anonymousOrganizationAccess(String exitToken, Long orgID) throws IOException, ClassNotFoundException;
        OrganizationMovement getOrganizationMovement() throws IOException, ClassNotFoundException;
    }

    interface Interactor {

    }

    interface Listener {

    }
}
