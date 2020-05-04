package it.qbteam.stalkerapp.presenter;

import androidx.fragment.app.Fragment;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.qbteam.stalkerapp.model.backend.model.Organization;
import it.qbteam.stalkerapp.model.data.User;


public interface MyStalkersListContract {

    interface View {
        void onSuccessCheckFile(ArrayList<Organization> list);
        void onFailureCheckFile(String message);
        void onSuccessAddOrganization(String message) throws IOException, JSONException;
        void onFailureAddOrganization(String message);
        void onSuccessRemoveOrganization(ArrayList<Organization> list) throws IOException, JSONException;
        void onSuccessAddOrganizationRest(String message);
        void onFailureAddOrganizationRest(String message);
        void onSuccessLoadFile(List<Organization> list);
    }

    //METODO DEL PRESENTER CHE VA A CHIAMARE IL METODO DELL' Model DEL MODELLO
    interface Presenter {
        ArrayList<Organization> checkFile(String path);
        void remove(Organization organization, ArrayList<Organization> list, String path) throws IOException, JSONException;
        String getOrganizationType(Organization organization);
        void addOrganizationLocal(Organization organization , ArrayList<Organization> list, String path) throws IOException, JSONException;
        void addOrganizationRest(Organization organization, String UID, String userToken) throws IOException, JSONException;
        void updateFile(ArrayList<Organization> list, String path) throws IOException, JSONException;
        void removeRest(Organization organization,String UID, String userToken);
        void loadList(String UID, String userToken);
    }

    //METODO DEL MODELLO
    interface Model {
       // void performCheckFile(String path);
        void performRemove(Organization organization, ArrayList<Organization> list, String path) throws IOException, JSONException;
        void performAddOrganizationLocal(Organization organization, ArrayList<Organization> list,String path) throws IOException, JSONException;
    }

    interface MyStalkerListener {
        //QUESTI DUE METODI FANNO RIFERIMENTO AL METODO PERFORM_CHECK_FILE DA FINIRE
        void onFailureFile(String message);
        void onSuccessAdd(String message) throws IOException, JSONException;
        void onFailureAdd(String message);
        void onSuccesRemove(ArrayList<Organization> list) throws IOException, JSONException;
        void onSuccessLoad(List<Organization> list);


    }
}
