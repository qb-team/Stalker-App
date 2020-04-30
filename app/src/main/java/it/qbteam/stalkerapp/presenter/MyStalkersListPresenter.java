package it.qbteam.stalkerapp.presenter;

import org.json.JSONException;

import it.qbteam.stalkerapp.model.backend.model.Organization;
import it.qbteam.stalkerapp.model.service.Storage;

import java.io.IOException;
import java.util.ArrayList;

public class MyStalkersListPresenter implements MyStalkersListContract.Presenter, MyStalkersListContract.MyStalkerListener{

    MyStalkersListContract.View myStalkersView;
    private Storage storage;

public MyStalkersListPresenter(MyStalkersListContract.View myStalkersView){
    this.myStalkersView=myStalkersView;
    storage= new Storage(null,this);
}


    @Override
    public ArrayList<Organization> checkFile( String path) {
        return storage.performCheckFile(path);
     }

    @Override
    public void remove(String name, ArrayList<Organization> list) throws IOException, JSONException {
        storage.performRemove(name,list);
    }

    @Override
    public void updateFile(ArrayList<Organization> list, String path) throws IOException, JSONException {
        storage.performUpdateFile(list,path);
    }

    @Override
    public String getOrganizationType(Organization organization) {
        return organization.getTrackingMode().toString();
    }

    @Override
    public void findOrganization(String name, ArrayList<Organization> list) throws IOException, JSONException {
        storage.performFindOrganization(name, list);
    }

    @Override
    public void addOrganization(String name, ArrayList<Organization> list) throws IOException, JSONException {
        storage.performAddOrganization(name, list);
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
    public void onSuccessSearch(String message) {
        myStalkersView.onSuccessSearchOrganization(message);
    }

    @Override
    public void onFailureSearch(String name) throws IOException, JSONException {
        myStalkersView.onFailureSearchOrganization(name);
    }

    @Override
    public void onSuccessAdd(String message) throws IOException, JSONException {
        myStalkersView.onSuccessAddOrganization(message);
    }

    @Override
    public void onSuccesRemove(ArrayList<Organization> list) throws IOException, JSONException {
        myStalkersView.onSuccessRemoveOrganization(list);
    }


}
