package it.qbteam.stalkerapp.presenter;

import org.json.JSONException;

import it.qbteam.stalkerapp.model.backend.model.Organization;
import it.qbteam.stalkerapp.model.data.User;

import java.io.IOException;
import java.util.ArrayList;

public interface HomeContract {

    //interfaccia view
    interface View {
        void onSuccessDownloadFile(String message);
        void onFailureDownloadFile(String message);
        void onFailureCheckFile(String message);
    }

    //interfaccia presenter
    interface Presenter {
        ArrayList<Organization> checkFile(String path);
        void downloadFile(String path, User user) throws InterruptedException, IOException;
        void updateFile(ArrayList<Organization> list, String path) throws IOException, JSONException;
        String getOrganizationType(Organization organization);

    }

    //interfaccia model
    interface Model {
        ArrayList<Organization> performCheckFile(String path);
        void performDownloadFile(String path, User user) throws InterruptedException, IOException;
    }

    interface HomeListener {
        void onSuccessDownload(String message);
        void onFailureDownload(String message);
        void onFailureCheck(String message);
    }
}
