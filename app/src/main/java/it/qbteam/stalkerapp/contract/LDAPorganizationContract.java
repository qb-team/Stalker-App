package it.qbteam.stalkerapp.contract;

import com.unboundid.ldap.sdk.LDAPException;
import org.json.JSONException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

//The contract interface describes the communication between view and presenter. It helps us to design in a cleaner way the interaction.
public interface LDAPorganizationContract {

    interface View {

        void onSuccessLdap(String message) throws IOException, JSONException;
        void onFailureLdap(String message);
    }

    interface Presenter {
         void setLDAP(String host, int port, String bindDN, String password);
         void bind() throws InterruptedException, LDAPException, ExecutionException;
         void search() throws ExecutionException, InterruptedException, IOException, JSONException;
    }

    interface Interactor {
        void performBind() throws LDAPException, ExecutionException, InterruptedException;
        void performSearch() throws ExecutionException, InterruptedException, IOException, JSONException;
    }

    interface LDAPlistener {
        void onSuccess(String message) throws IOException, JSONException;
        void onFailure(String message);
    }
}
