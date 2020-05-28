package it.qbteam.stalkerapp.contract;

import java.io.IOException;
import java.util.List;

import it.qbteam.stalkerapp.model.backend.dataBackend.PlaceAccess;

public interface PlaceAccessContract {
    interface View {
        void onSuccessGetPlaceAccessInLocal(List<PlaceAccess> organizationAccessList);


    }

    interface Presenter {

        void getPlaceAccessList() throws IOException, ClassNotFoundException;



    }


    interface PlaceAccessListener {
        void onSuccessGetPlaceAccess(List<PlaceAccess> organizationAccessList);

    }
}
