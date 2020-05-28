package it.qbteam.stalkerapp.presenter;

import java.io.IOException;
import java.util.List;

import it.qbteam.stalkerapp.contract.PlaceAccessContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.PlaceAccess;
import it.qbteam.stalkerapp.model.service.Storage;

public class PlaceAccessPresenter implements PlaceAccessContract.Presenter, PlaceAccessContract.PlaceAccessListener {
    private PlaceAccessContract.View placeAccessView;
    private Storage storage;

    public PlaceAccessPresenter(PlaceAccessContract.View placeAccessView){
        this.placeAccessView=placeAccessView;
        storage= new Storage(null,null, null, this);

    }
    @Override
    public void getPlaceAccessList() throws IOException, ClassNotFoundException {
        storage.deserializePlaceAccessInLocal();
    }

    @Override
    public void onSuccessGetPlaceAccess(List<PlaceAccess> organizationAccessList) {
         placeAccessView.onSuccessGetPlaceAccessInLocal(organizationAccessList);
    }
}
