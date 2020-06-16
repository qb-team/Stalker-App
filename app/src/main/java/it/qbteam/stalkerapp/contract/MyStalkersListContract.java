package it.qbteam.stalkerapp.contract;

import org.json.JSONException;
import java.io.IOException;
import java.util.List;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;

//The contract interface describes the communication between view and presenter. It helps us to design in a cleaner way the interaction.
public interface MyStalkersListContract {

    interface View {
        void onSuccessAddOrganization(List<Organization> list,String message) throws IOException, JSONException;
        void onFailureAddOrganization(String message);
        void onSuccessRemoveOrganization(List<Organization> list) throws IOException, JSONException;
        void onSuccessLoadMyStalkerList(List<Organization> list) throws IOException, JSONException;
        void onFailureLoadMyStalkerList();
    }

    interface Presenter {
        void updateFile(List<Organization> list, String path) throws IOException, JSONException;
        void addOrganizationLocal(Organization organization , List<Organization> list, String path) throws IOException, JSONException;
        void addOrganizationServer(Organization organization, String UID, String userToken) throws IOException, JSONException;
        void removeOrganizationLocal(Organization organization, List<Organization> list, String path) throws IOException, JSONException;
        void removeOrganizationServer(Organization organization,String UID, String userToken) throws IOException, ClassNotFoundException;
        void loadFavoriteListServer(String UID, String userToken);

    }

    interface Interactor {
        void performUpdateFile(List<Organization> list, String path) throws IOException, JSONException;
        void performRemoveLocal(Organization organization, List<Organization> list, String path) throws IOException, JSONException, ClassNotFoundException;
        void performAddOrganizationLocal(Organization organization, List<Organization> list,String path) throws IOException, JSONException;
    }

    interface MyStalkerListener {
        void onSuccessAdd(List<Organization> list,String message) throws IOException, JSONException;
        void onFailureAdd(String message);
        void onSuccesRemove(List<Organization> list) throws IOException, JSONException;
        void onSuccessLoad(List<Organization> list) throws IOException, JSONException;
        void onFailureLoad();
    }
}
