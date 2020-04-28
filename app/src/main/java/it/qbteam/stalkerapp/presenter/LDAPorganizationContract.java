package it.qbteam.stalkerapp.presenter;

import com.unboundid.ldap.sdk.LDAPException;
import java.util.concurrent.ExecutionException;
import it.qbteam.stalkerapp.model.service.StalkerLDAP;

public interface LDAPorganizationContract {
    interface View {

        void onLoadListFailure(String message);

    }

    //METODO DEL PRESENTER CHE VA A CHIAMARE IL METODO DELL' Model DEL MODELLO
    interface Presenter {
        public void setLDAP(String host, int port, String bindDN, String password);
        public void bind() throws InterruptedException, LDAPException, ExecutionException;
        public void search() throws ExecutionException, InterruptedException;
        StalkerLDAP getLDAP();
    }

    //METODO DEL MODELLO
    interface Model {
        public void performBind() throws LDAPException, ExecutionException, InterruptedException;
        public void performSearch() throws ExecutionException, InterruptedException;

    }

    interface OrganizationListListener {

        void onFailure(String message);
    }
}
