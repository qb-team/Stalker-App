package it.qbteam.stalkerapp.presenter;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import it.qbteam.stalkerapp.model.service.Storage;
import it.qbteam.stalkerapp.model.data.Organization;
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
    public void checkFile(Fragment fragment, String nameFile) {
        storage.performCheckFile(fragment, nameFile);
    }

    @Override
    public void downloadFile( Fragment fragment,  ArrayList<Organization> actualList) throws InterruptedException {
        storage.performDownloadFile(fragment,actualList);
    }

    @Override
    public void updateFile(ArrayList<Organization> list, Fragment fragment, String nameFile) throws IOException, JSONException {
        storage.performUpdateFile(list,fragment,nameFile);
    }

    @Override
    public String getOrganizationType(Organization organization) {
        return organization.getType();
    }

    @Override
    public void onSuccessFile(ArrayList<Organization> list) {
        OrganizationListView.onSuccessCheckFile(list);
    }

    @Override
    public void onFailureFile(String message) {
        OrganizationListView.onFailureCheckFile(message);
    }


}
