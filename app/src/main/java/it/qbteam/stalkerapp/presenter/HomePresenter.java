package it.qbteam.stalkerapp.presenter;

import org.json.JSONException;

import it.qbteam.stalkerapp.model.backend.model.Organization;
import it.qbteam.stalkerapp.model.service.REST;
import it.qbteam.stalkerapp.model.service.Storage;

import java.io.IOException;
import java.util.ArrayList;

public class HomePresenter implements HomeContract.Presenter, HomeContract.HomeListener {

    private HomeContract.View OrganizationListView;
    private Storage storage;
    private REST rest;

    public HomePresenter(HomeContract.View OrganizationListView){
        this.OrganizationListView=OrganizationListView;
        storage=new Storage(this,null);
        rest=new REST(null, this);
    }
    @Override
    public ArrayList<Organization> checkLocalFile(String path) {
        return storage.performCheckFileLocal(path);
    }

    @Override
    public void updateFile(ArrayList<Organization> list, String path) throws IOException, JSONException {
        storage.performUpdateFile(list,path);
    }

    @Override
    public void downloadHomeListRest(String path, String userToken)  {
        rest.performDownloadFileREST(path,userToken);
    }

    @Override
    public void onSuccessDownload(String message) {
        OrganizationListView.onSuccessDownloadList(message);
    }

    @Override
    public void onFailureDownload(String message) {
        OrganizationListView.onFailureDownloadList(message);
    }

    @Override
    public void onFailureCheck(String message) {
        OrganizationListView.onFailureCheckFile(message);
    }
}
