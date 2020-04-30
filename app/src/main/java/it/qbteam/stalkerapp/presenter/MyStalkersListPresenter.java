package it.qbteam.stalkerapp.presenter;

import org.json.JSONException;

import it.qbteam.stalkerapp.model.backend.model.Organization;
import it.qbteam.stalkerapp.model.data.User;
import it.qbteam.stalkerapp.model.service.Rest;
import it.qbteam.stalkerapp.model.service.Storage;

import java.io.IOException;
import java.util.ArrayList;

public class MyStalkersListPresenter implements MyStalkersListContract.Presenter, MyStalkersListContract.MyStalkerListener{

    MyStalkersListContract.View myStalkersView;
    private Storage storage;
    private Rest rest;

    public MyStalkersListPresenter(MyStalkersListContract.View myStalkersView){
    this.myStalkersView=myStalkersView;
    storage= new Storage(null,this);
    rest = new Rest(this);
}


    @Override
    public ArrayList<Organization> checkFile( String path) {
        return storage.performCheckFile(path);
     }

    @Override
    public void remove(Organization organization, ArrayList<Organization> list, String path) throws IOException, JSONException {
        storage.performRemove(organization, list, path);
    }


    @Override
    public String getOrganizationType(Organization organization) {
        return organization.getTrackingMode().toString();
    }

    @Override
    public void updateFile(ArrayList<Organization> list, String path) throws IOException, JSONException {
        storage.saveInLocalFile(list,path);
    }


    @Override
    public void addOrganizationLocal(Organization organization, ArrayList<Organization> list, String path) throws IOException, JSONException {
        storage.performAddOrganizationLocal(organization, list,path);
    }

    @Override
    public void addOrganizationRest(Organization organization, User user) throws IOException, JSONException {
        rest.performAddOrganizationRest(organization, user);
    }

    @Override
    public void onSuccessFile(ArrayList<Organization> list) {
        myStalkersView.onSuccessCheckFile(list);
    }

    @Override
    public void onFailureFile(String message) {
        myStalkersView.onFailureCheckFile(message);
    }


    @Override
    public void onSuccessAdd(String message) throws IOException, JSONException {
        myStalkersView.onSuccessAddOrganization(message);
    }

    @Override
    public void onFailureAdd(String message) {
        myStalkersView.onFailureAddOrganization(message);
    }

    @Override
    public void onSuccesRemove(ArrayList<Organization> list) throws IOException, JSONException {
        myStalkersView.onSuccessRemoveOrganization(list);
    }

    @Override
    public void onSuccessAddRest(String message) {
        myStalkersView.onSuccessAddOrganizationRest(message);
    }

    @Override
    public void onFailureAddRest(String message) {
        myStalkersView.onFailureAddOrganizationRest(message);
    }


}
