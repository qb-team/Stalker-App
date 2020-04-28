package it.qbteam.stalkerapp.presenter;

import androidx.fragment.app.Fragment;
import org.json.JSONException;
import it.qbteam.stalkerapp.model.service.Storage;
import it.qbteam.stalkerapp.model.data.Organization;
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
    public void  checkFile(Fragment fragment, String nameFile) {
        storage.performCheckFile(fragment, nameFile);
     }

    @Override
    public ArrayList<Organization> rimuovi(String name, ArrayList<Organization> list) {
        return storage.performRimuovi(name,list);
    }

    @Override
    public void updateFile(ArrayList<Organization> list, Fragment fragment, String nameFile) throws IOException, JSONException {
   storage.performUpdateFile(list,fragment , nameFile);
    }

    @Override
    public String getOrganizationType(Organization organization) {
        return organization.getType();
    }


    @Override
    public void onSuccessFile(ArrayList<Organization> list) {
        myStalkersView.onSuccessCheckFile(list);
    }

    @Override
    public void onFailureFile(String message) {
        myStalkersView.onFailureCheckFile(message);
    }
}
