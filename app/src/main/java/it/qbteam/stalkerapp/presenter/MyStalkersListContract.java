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

        void onSuccessAddOrganization(ArrayList<Organization> list,String message) throws IOException, JSONException;
        void onFailureAddOrganization(String message);
        void onSuccessRemoveOrganization(ArrayList<Organization> list) throws IOException, JSONException;
        void onSuccessLoadMyStalkerList(List<Organization> list) throws IOException, JSONException;

    }

    //METODO DEL PRESENTER CHE VA A CHIAMARE IL METODO DELL' Model DEL MODELLO
    interface Presenter {
        void updateFile(ArrayList<Organization> list, String path) throws IOException, JSONException;
        ArrayList<Organization> checkLocalFile(String path);
        void addOrganizationLocal(Organization organization , ArrayList<Organization> list, String path) throws IOException, JSONException;
        void addOrganizationREST(Organization organization, String UID, String userToken) throws IOException, JSONException;
        void removeOrganizationLocal(Organization organization, ArrayList<Organization> list, String path) throws IOException, JSONException;
        void removeOrganizationREST(Organization organization,String UID, String userToken);
        void downloadListREST(String UID, String userToken);
    }

    //METODO DEL MODELLO
    interface Model {
        void performUpdateFile(ArrayList<Organization> list, String path) throws IOException, JSONException;
        void performRemoveLocal(Organization organization, ArrayList<Organization> list, String path) throws IOException, JSONException;
        void performAddOrganizationLocal(Organization organization, ArrayList<Organization> list,String path) throws IOException, JSONException;
    }

    interface MyStalkerListener {
        //QUESTI DUE METODI FANNO RIFERIMENTO AL METODO PERFORM_CHECK_FILE DA FINIRE
        void onSuccessAdd(ArrayList<Organization> list,String message) throws IOException, JSONException;
        void onFailureAdd(String message);
        void onSuccesRemove(ArrayList<Organization> list) throws IOException, JSONException;
        void onSuccessLoad(List<Organization> list) throws IOException, JSONException;



    }
}
