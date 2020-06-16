package it.qbteam.stalkerapp.contract;

import org.json.JSONException;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import java.io.IOException;
import java.util.List;

//The contract interface describes the communication between view and presenter. It helps us to design in a cleaner way the interaction.
public interface HomeContract {

    interface View {

        void onSuccessDownloadList(String message);
        void onFailureDownloadList(String message);
        void onFailureCheckFile(String message);
    }

    interface Presenter {
        List<Organization> checkLocalFile(String path);
        void downloadOrganizationListServer(String path, String userToken) throws InterruptedException, IOException;
        void updateFile(List<Organization> list, String path) throws IOException, JSONException;

    }

    interface Interactor {
        List<Organization> performCheckFileLocal(String path);
        void performUpdateFile(List<Organization> list, String path) throws IOException, JSONException;
    }

    interface HomeListener {
        void onSuccessDownload(String message);
        void onFailureDownload(String message);
        void onFailureCheck(String message);
    }
}
