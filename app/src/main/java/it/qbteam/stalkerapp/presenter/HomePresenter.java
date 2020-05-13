package it.qbteam.stalkerapp.presenter;

import org.json.JSONException;

import it.qbteam.stalkerapp.contract.HomeContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.model.service.Server;
import it.qbteam.stalkerapp.model.service.Storage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomePresenter implements HomeContract.Presenter, HomeContract.HomeListener {

    private HomeContract.View OrganizationListView;
    private Storage storage;
    private Server server;

    //HomePresenter's constructor.
    public HomePresenter(HomeContract.View OrganizationListView){
        this.OrganizationListView=OrganizationListView;
        storage = new Storage(this,null);
        server = new Server(null, this);
    }

    //Calls the the method performCheckFileLocal(path) of the class Storage(persistent layer of the file system).
    @Override
    public List<Organization> checkLocalFile(String path) {
        return storage.performCheckFileLocal(path);
    }

    //Calls the the method performUpdateFile(list,path) of the class Storage(persistent layer of the file system).
    @Override
    public void updateFile(List<Organization> list, String path) throws IOException, JSONException {
        storage.performUpdateFile(list,path);
    }

    //Calls the the method performDownloadFileServer(path,userToken) of the class Server(persistent layer of the server).
    @Override
    public void downloadHomeListServer(String path, String userToken)  {
        server.performDownloadFileServer(path,userToken);
    }

    //Comunicates the success result of the list download to the view.
    @Override
    public void onSuccessDownload(String message) {
        OrganizationListView.onSuccessDownloadList(message);
    }

    //Comunicates the failure result of the list download to the view.
    @Override
    public void onFailureDownload(String message) {
        OrganizationListView.onFailureDownloadList(message);
    }

    //Communicates the failure to check the existence of the list in the file system.
    @Override
    public void onFailureCheck(String message) {
        OrganizationListView.onFailureCheckFile(message);
    }
}
