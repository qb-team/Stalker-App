package it.qbteam.stalkerapp.presenter;

import androidx.fragment.app.Fragment;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;

import it.qbteam.stalkerapp.model.data.Organization;


public interface MyStalkersListContract {
    interface View {
        void onSuccessCheckFile(ArrayList<Organization> list);
        void onFailureCheckFile(String message);
        void onSuccessSearchOrganization(String message);
        void onFailureSearchOrganization(Organization organization) throws IOException, JSONException;
        void onSuccessAddOrganization(String message) throws IOException, JSONException;
        void onFailureAddOrganization(String message);
    }

    //METODO DEL PRESENTER CHE VA A CHIAMARE IL METODO DELL' Model DEL MODELLO
    interface Presenter {
        void checkFile(Fragment fragment, String nameFile);
        ArrayList<Organization> remove(String name, ArrayList<Organization> list);
        void updateFile(ArrayList<Organization> list, Fragment fragment, String nameFile) throws IOException, JSONException;
        String getOrganizationType(Organization organization);
        void findOrganization(Organization organization, ArrayList<Organization> list) throws IOException, JSONException;
        void addOrganization(Organization organization, ArrayList<Organization> list) throws IOException, JSONException;
    }

    //METODO DEL MODELLO
    interface Model {
        void performCheckFile(Fragment fragment, String nameFile);
        ArrayList<Organization> performRemove(String name, ArrayList<Organization> list);
        void performUpdateFile(ArrayList<Organization> list, Fragment fragment, String nameFile) throws JSONException, IOException;
        void performFindOrganization(Organization organization, ArrayList<Organization> list) throws IOException, JSONException;
        void performAddOrganization(Organization organization, ArrayList<Organization> list) throws IOException, JSONException;
    }

    interface MyStalkerListener {
        void onSuccessFile(ArrayList<Organization> list);
        void onFailureFile(String message);
        void onSuccessSearch(String message);
        void onFailureSearch(Organization organization) throws IOException, JSONException;
        void onSuccessAdd(String message) throws IOException, JSONException;
        void onFailureAdd(String message);

    }
}
