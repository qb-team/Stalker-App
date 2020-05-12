package it.qbteam.stalkerapp.contract;

import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;

//The contract interface describes the communication between view and presenter. It helps us to design in a cleaner way the interaction.
public interface MyStalkersListContract {

    interface View {
        void onSuccessAddOrganization(ArrayList<Organization> list,String message) throws IOException, JSONException;
        void onFailureAddOrganization(String message);
        void onSuccessRemoveOrganization(ArrayList<Organization> list) throws IOException, JSONException;
        void onSuccessLoadMyStalkerList(List<Organization> list) throws IOException, JSONException;
    }

    interface Presenter {
        void updateFile(ArrayList<Organization> list, String path) throws IOException, JSONException;
        ArrayList<Organization> checkLocalFile(String path);
        void addOrganizationLocal(Organization organization , ArrayList<Organization> list, String path) throws IOException, JSONException;
        void addOrganizationServer(Organization organization, String UID, String userToken) throws IOException, JSONException;
        void removeOrganizationLocal(Organization organization, ArrayList<Organization> list, String path) throws IOException, JSONException;
        void removeOrganizationServer(Organization organization,String UID, String userToken) throws IOException, ClassNotFoundException;
        void downloadListServer(String UID, String userToken);
    }

    interface Interactor {
        void performUpdateFile(ArrayList<Organization> list, String path) throws IOException, JSONException;
        void performRemoveLocal(Organization organization, ArrayList<Organization> list, String path) throws IOException, JSONException, ClassNotFoundException;
        void performAddOrganizationLocal(Organization organization, ArrayList<Organization> list,String path) throws IOException, JSONException;
    }

    interface MyStalkerListener {
        void onSuccessAdd(ArrayList<Organization> list,String message) throws IOException, JSONException;
        void onFailureAdd(String message);
        void onSuccesRemove(ArrayList<Organization> list) throws IOException, JSONException;
        void onSuccessLoad(List<Organization> list) throws IOException, JSONException;
    }
}
