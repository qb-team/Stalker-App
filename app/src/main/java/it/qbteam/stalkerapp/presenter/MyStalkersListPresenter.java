package it.qbteam.stalkerapp.presenter;

import org.json.JSONException;

import it.qbteam.stalkerapp.contract.MyStalkersListContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;
import it.qbteam.stalkerapp.model.service.Server;
import it.qbteam.stalkerapp.model.service.Storage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyStalkersListPresenter implements MyStalkersListContract.Presenter, MyStalkersListContract.MyStalkerListener{

    private MyStalkersListContract.View myStalkersView;
    private Storage storage;
    private Server server;

    //MyStalkersListPresenter's constructor.
    public MyStalkersListPresenter(MyStalkersListContract.View myStalkersView){
    this.myStalkersView = myStalkersView;
    storage= new Storage(null,this);
    server = new Server(this, null);
    }

    //Calls the the method performUpdateFile(list,path) of the class Storage(persistent layer that comunicates with FileSystem).
    @Override
    public void updateFile(List<Organization> list, String path) throws IOException, JSONException {
        storage.performUpdateFile(list,path);
    }

    @Override
    public OrganizationMovement getOrganizationMovement() throws IOException, ClassNotFoundException {
        return storage.deserializeMovementInLocal();
    }


    //Calls the the method performCheckFileLocal(path) of the class Storage(persistent layer that comunicates with FileSystem).
    @Override
    public void addOrganizationLocal(Organization organization, List<Organization> list, String path) throws IOException, JSONException {
        storage.performAddOrganizationLocal(organization, list,path);
    }

    //Calls the the method performCheckFileLocal(path) of the class Server(persistent layer that comunicates with Server).
    @Override
    public void addOrganizationServer(Organization organization,String UID, String userToken) {
        server.performAddOrganizationServer(organization,UID,userToken);
    }

    @Override
    public void trackingError(String message) {
        myStalkersView.onTrackingError(message);
    }

    //Comunicates the success result of adding an organization to the view.
    @Override
    public void onSuccessAdd(List<Organization>list,String message) throws IOException, JSONException {
        myStalkersView.onSuccessAddOrganization(list,message);
    }

    //Comunicates the failure result of add an organization to the view.
    @Override
    public void onFailureAdd(String message) {
        myStalkersView.onFailureAddOrganization(message);
    }

    //Calls the the method performRemoveLocal(organization, list, path) of the class Storage(persistent layer that comunicates with FileSystem).
    @Override
    public void removeOrganizationLocal(Organization organization, List<Organization> list, String path) throws IOException, JSONException {
        storage.performRemoveLocal(organization, list, path);
    }

    //Calls the the method performRemoveOrganizationServer(organization, UID,userToken) of the class Server(persistent layer that comunicates with Server).
    @Override
    public void removeOrganizationServer(Organization organization, String UID, String userToken) throws IOException, ClassNotFoundException {
        server.performRemoveOrganizationServer(organization, UID,userToken);
    }

    //Comunicates the failure result of remove an organization to the view.
    @Override
    public void onSuccesRemove(List<Organization> list) throws IOException, JSONException {
        myStalkersView.onSuccessRemoveOrganization(list);
    }

    //Calls the the method performLoadListServer(UID, userToken) of the class Server(persistent layer that comunicates with Server).
    @Override
    public void downloadListServer(String UID, String userToken) {
        server.performLoadListServer(UID, userToken);
    }

    //Comunicates the success result of download organization list to the view.
    @Override
    public void onSuccessLoad(List<Organization> list) throws IOException, JSONException {
        myStalkersView.onSuccessLoadMyStalkerList(list);
    }

}
