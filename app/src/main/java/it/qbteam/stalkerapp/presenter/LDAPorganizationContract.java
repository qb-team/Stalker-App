package it.qbteam.stalkerapp.presenter;

import com.unboundid.ldap.sdk.LDAPException;
import java.util.concurrent.ExecutionException;
import it.qbteam.stalkerapp.model.service.StalkerLDAP;

public interface LDAPorganizationContract {
    interface View {

        void onSuccessLdap(String message);
        void onFailureLdap(String message);
    }

    //METODO DEL PRESENTER CHE VA A CHIAMARE IL METODO DELL' Model DEL MODELLO
    interface Presenter {
         void setLDAP(String host, int port, String bindDN, String password);
         void bind() throws InterruptedException, LDAPException, ExecutionException;
         void search() throws ExecutionException, InterruptedException;
         StalkerLDAP getLDAP();

    }

    //METODO DEL MODELLO
    interface Model {
         void performBind() throws LDAPException, ExecutionException, InterruptedException;
         void performSearch() throws ExecutionException, InterruptedException;

    }

    interface LDAPlistener {

        void onSuccess(String message);
        void onFailure(String message);

    }
}
