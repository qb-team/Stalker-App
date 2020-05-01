package it.qbteam.stalkerapp.model.service;
import android.os.AsyncTask;

import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchResultEntry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import it.qbteam.stalkerapp.presenter.LDAPorganizationContract;
import it.qbteam.stalkerapp.ui.view.MyStalkersListFragment;


public class StalkerLDAP implements LDAPorganizationContract.Model {

        private LDAPorganizationContract.LDAPlistener ldaPlistener;
        private static final String TAG = "StalkerLDAP";
        private LDAPConnection connection;
        private BindResult result;
        private String bindDN;
        private String bindPassword;
        private String serverAddress;
        private int serverPort;
        private SearchResultEntry entry;
        private static StalkerLDAP instance = null;

        public StalkerLDAP(String serverAddress, int port, String binDn, String password,LDAPorganizationContract.LDAPlistener ldaPlistener) {
            this.serverAddress = serverAddress;
            this.serverPort = port;
            this.bindDN = binDn;
            this.bindPassword = password;
            this.ldaPlistener=ldaPlistener;
            this.instance=this;

        }
        public static StalkerLDAP getInstance() {
        return instance;
    }

        @Override
        public void performBind() throws LDAPException, ExecutionException, InterruptedException {
            this.connection = new LDAPConnection(serverAddress, serverPort);
            FutureTask<BindResult> bindFutureTask = new FutureTask<>(new Callable<BindResult>() {
                @Override
                public BindResult call() throws Exception {
                    return connection.bind(bindDN, bindPassword);
                }
            });
            new Thread(bindFutureTask).start();
            this.result = bindFutureTask.get();
              System.out.println("CREDENZIALI:"+result.getServerSASLCredentials()+"   "+ "RESULT"+result.toString());


        }

        @Override
        public void performSearch() throws ExecutionException, InterruptedException {
            FutureTask<SearchResultEntry> searchFutureTask = new FutureTask<>(new Callable<SearchResultEntry>() {
                @Override
                public SearchResultEntry call() throws Exception {
                    return connection.getEntry(bindDN);
                }
            });
            new Thread(searchFutureTask).start();
            this.entry = searchFutureTask.get();
            this.connection.close();
            if(this.result != null && this.entry != null)
                ldaPlistener.onSuccess("Ti sei autenticato con successo");
            else
                ldaPlistener.onFailure("Errore durante l'autenticazione");

        }


        public String getUid() {
            return entry.getAttributeValue("uid");

        }
        public String getUidNumber() {
            return entry.getAttributeValue("uidNumber");
        }

        public SearchResultEntry getSearchResultEntry(){
            return entry;
        }

        public BindResult getResult(){
            return (BindResult) this.result;
        }
    }