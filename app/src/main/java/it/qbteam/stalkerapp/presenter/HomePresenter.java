package it.qbteam.stalkerapp.presenter;

import org.json.JSONException;
import it.qbteam.stalkerapp.contract.HomeContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.model.service.Server;
import it.qbteam.stalkerapp.model.service.Storage;
import java.io.IOException;
import java.util.List;

public class HomePresenter implements HomeContract.Presenter, HomeContract.HomeListener {

    private HomeContract.View homeView;
    private Storage storage;
    private Server server;

    //HomePresenter's constructor.
    public HomePresenter(HomeContract.View homeView){
        this.homeView=homeView;
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

   /* @Override
    public void createAllFile() throws IOException {
        storage.performCreateAllFile();
    }*/

    //Calls the the method performDownloadFileServer(path,userToken) of the class Server(persistent layer of the server).
    @Override
    public void downloadOrganizationListServer(String path, String userToken)  {
        server.performDownloadOrganizationListServer(path,userToken);
    }


    //Comunicates the success result of the list download to the view.
    @Override
    public void onSuccessDownload(String message) {
        homeView.onSuccessDownloadList(message);
    }

    //Comunicates the failure result of the list download to the view.
    @Override
    public void onFailureDownload(String message) {
        homeView.onFailureDownloadList(message);
    }

    //Communicates the failure to check the existence of the list in the file system.
    @Override
    public void onFailureCheck(String message) {
        homeView.onFailureCheckFile(message);
    }
}
