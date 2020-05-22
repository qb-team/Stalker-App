package it.qbteam.stalkerapp.contract;

import java.util.HashMap;

public interface StandardOrganizationContract {
    interface View {

    }

    interface Presenter {
        HashMap<String, String> desAccessExitInLocal();
    }

    interface Interactor {

    }

    interface Listener {
        void onStandardOrganizationListener();
    }
}
