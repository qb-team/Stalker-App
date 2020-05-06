package it.qbteam.stalkerapp.presenter;

import org.json.JSONException;

import it.qbteam.stalkerapp.model.backend.modelBackend.Organization;
import it.qbteam.stalkerapp.model.service.REST;
import it.qbteam.stalkerapp.model.service.Storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyStalkersListPresenter implements MyStalkersListContract.Presenter, MyStalkersListContract.MyStalkerListener{

    MyStalkersListContract.View myStalkersView;
    private Storage storage;
    private REST rest;

    public MyStalkersListPresenter(MyStalkersListContract.View myStalkersView){
    this.myStalkersView=myStalkersView;
    storage= new Storage(null,this);
    rest = new REST(this, null);
    }


    @Override
    public void updateFile(ArrayList<Organization> list, String path) throws IOException, JSONException {
        storage.performUpdateFile(list,path);
    }

    @Override
    public ArrayList<Organization> checkLocalFile(String path) {
        return storage.performCheckFileLocal(path);
    }

    @Override
    public void addOrganizationLocal(Organization organization, ArrayList<Organization> list, String path) throws IOException, JSONException {
        storage.performAddOrganizationLocal(organization, list,path);
    }

    @Override
    public void addOrganizationREST(Organization organization,String UID, String userToken) {
        rest.performAddOrganizationREST(organization,UID,userToken);
    }

    @Override
    public void onSuccessAdd(ArrayList<Organization>list,String message) throws IOException, JSONException {
        myStalkersView.onSuccessAddOrganization(list,message);
    }

    @Override
    public void onFailureAdd(String message) {
        myStalkersView.onFailureAddOrganization(message);
    }

    @Override
    public void removeOrganizationLocal(Organization organization, ArrayList<Organization> list, String path) throws IOException, JSONException {
        storage.performRemoveLocal(organization, list, path);
    }

    @Override
    public void removeOrganizationREST(Organization organization, String UID, String userToken) throws IOException, ClassNotFoundException {
        rest.performRemoveOrganizationREST(organization, UID,userToken);
    }

    @Override
    public void onSuccesRemove(ArrayList<Organization> list) throws IOException, JSONException {
        myStalkersView.onSuccessRemoveOrganization(list);
    }

    @Override
    public void downloadListREST(String UID, String userToken) {
        rest.performLoadListREST(UID, userToken);
    }

    @Override
    public void onSuccessLoad(List<Organization> list) throws IOException, JSONException {
        myStalkersView.onSuccessLoadMyStalkerList(list);
    }

}
