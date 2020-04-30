package it.qbteam.stalkerapp.presenter;

import androidx.fragment.app.Fragment;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;

import it.qbteam.stalkerapp.model.backend.model.Organization;
import it.qbteam.stalkerapp.model.data.User;


public interface MyStalkersListContract {
    interface View {
        void onSuccessCheckFile(ArrayList<Organization> list);
        void onFailureCheckFile(String message);
        void onSuccessSearchOrganization(String message);
        void onFailureSearchOrganization(String name) throws IOException, JSONException;
        void onSuccessAddOrganization(String message) throws IOException, JSONException;
        void onSuccessRemoveOrganization(ArrayList<Organization> list) throws IOException, JSONException;
        void onSuccessAddOrganizationRest(String message);
        void onFailureAddOrganizationRest(String message);
    }

    //METODO DEL PRESENTER CHE VA A CHIAMARE IL METODO DELL' Model DEL MODELLO
    interface Presenter {
        ArrayList<Organization> checkFile(String path);
        void remove(String name, ArrayList<Organization> list) throws IOException, JSONException;
        void updateFile(ArrayList<Organization> list,String path) throws IOException, JSONException;
        String getOrganizationType(Organization organization);
        void findOrganization(String name, ArrayList<Organization> list) throws IOException, JSONException;
        void addOrganization(String name , ArrayList<Organization> list) throws IOException, JSONException;
        void addOrganizationRest(Organization organization, User user) throws IOException, JSONException;
    }

    //METODO DEL MODELLO
    interface Model {
        ArrayList<Organization> performCheckFile(String path);
        void performRemove(String name, ArrayList<Organization> list) throws IOException, JSONException;
        void performUpdateFile(ArrayList<Organization> list, String path) throws JSONException, IOException;
        void performFindOrganization(String name, ArrayList<Organization> list) throws IOException, JSONException;
        void performAddOrganization(String name, ArrayList<Organization> list) throws IOException, JSONException;
    }

    interface MyStalkerListener {
        //QUESTI DUE METODI FANNO RIFERIMENTO AL METODO PERFORM_CHECK_FILE DA FINIRE
        void onSuccessFile(ArrayList<Organization> list);
        void onFailureFile(String message);
        void onSuccessSearch(String message);
        void onFailureSearch(String name) throws IOException, JSONException;
        void onSuccessAdd(String message) throws IOException, JSONException;
        void onSuccesRemove(ArrayList<Organization> list) throws IOException, JSONException;
        void onSuccessAddRest(String message);
        void onFailureAddRest(String message);

    }
}
