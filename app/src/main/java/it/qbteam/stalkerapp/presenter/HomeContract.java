package it.qbteam.stalkerapp.presenter;

import androidx.fragment.app.Fragment;
import org.json.JSONException;
import it.qbteam.stalkerapp.model.data.Organization;
import java.io.IOException;
import java.util.ArrayList;

public interface HomeContract {

    //interfaccia view
    interface View {

        void onSuccessCheckFile(ArrayList<Organization> list);
        void onFailureCheckFile(String message);

    }

    //interfaccia presenter
    interface Presenter {

        void checkFile(Fragment fragment, String nameFile);
        void downloadFile(Fragment fragment, ArrayList<Organization> listaAttuale) throws InterruptedException;
        void updateFile(ArrayList<Organization> list, Fragment fragment, String nameFile) throws IOException, JSONException;
        String getOrganizationType(Organization organization);

    }

    //interfaccia model
    interface Model {

        void performCheckFile(Fragment fragment, String nameFile);
        void performDownloadFile(Fragment fragment, ArrayList<Organization> listaAttuale) throws InterruptedException;
        void performUpdateFile(ArrayList<Organization> list, Fragment fragment, String nameFile) throws JSONException, IOException;

    }

    interface HomeListener {

        void onSuccessFile(ArrayList<Organization> list);
        void onFailureFile(String message);

    }
}
