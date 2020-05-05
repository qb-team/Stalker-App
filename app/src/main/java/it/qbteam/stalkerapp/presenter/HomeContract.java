package it.qbteam.stalkerapp.presenter;

import org.json.JSONException;

import it.qbteam.stalkerapp.model.backend.model.Organization;
import it.qbteam.stalkerapp.model.data.User;

import java.io.IOException;
import java.util.ArrayList;

public interface HomeContract {

    //interfaccia view
    interface View {
        void onSuccessDownloadList(String message);
        void onFailureDownloadList(String message);
        void onFailureCheckFile(String message);
    }

    //interfaccia presenter
    interface Presenter {
        ArrayList<Organization> checkLocalFile(String path);
        void downloadHomeListRest(String path, String UID, String userToken) throws InterruptedException, IOException;
        void updateFile(ArrayList<Organization> list, String path) throws IOException, JSONException;

    }

    //interfaccia model
    interface Model {
        ArrayList<Organization> performCheckFileLocal(String path);
        void performUpdateFile(ArrayList<Organization> list, String path) throws IOException, JSONException;
    }

    interface HomeListener {
        void onSuccessDownload(String message);
        void onFailureDownload(String message);
        void onFailureCheck(String message);
    }
}
