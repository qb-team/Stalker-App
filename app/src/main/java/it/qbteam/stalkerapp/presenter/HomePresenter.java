package it.qbteam.stalkerapp.presenter;

import org.json.JSONException;

import it.qbteam.stalkerapp.model.backend.model.Organization;
import it.qbteam.stalkerapp.model.data.User;
import it.qbteam.stalkerapp.model.service.Storage;

import java.io.IOException;
import java.util.ArrayList;

public class HomePresenter implements HomeContract.Presenter, HomeContract.HomeListener {

    private HomeContract.View OrganizationListView;
    private Storage storage;

    public HomePresenter(HomeContract.View OrganizationListView){
        this.OrganizationListView=OrganizationListView;

        storage=new Storage(this,null);
    }
    @Override
    public ArrayList<Organization> checkFile(String path) {
        return storage.performCheckFile(path);
    }

    @Override
    public void downloadFile(String path, User user) throws InterruptedException, IOException {
        storage.performDownloadFile(path, user);
    }

    @Override
    public void updateFile(ArrayList<Organization> list, String path) throws IOException, JSONException {
        storage.saveInLocalFile(list,path);
    }

    @Override
    public String getOrganizationType(Organization organization) {
        return organization.getTrackingMode().toString();
    }

    @Override
    public void onSuccessFile(ArrayList<Organization> list) {
        OrganizationListView.onSuccessCheckFile(list);
    }

    @Override
    public void onFailureFile(String message) {
        OrganizationListView.onFailureCheckFile(message);
    }

    @Override
    public void onSuccessDownload(ArrayList<Organization> list) {
        OrganizationListView.onSuccessDownloadFile(list);
    }

    @Override
    public void onFailureDownload(String message) {
        OrganizationListView.onFailureDownloadFile(message);

    }


}
