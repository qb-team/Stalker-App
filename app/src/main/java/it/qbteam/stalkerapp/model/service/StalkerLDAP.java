package it.qbteam.stalkerapp.model.service;

import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchResultEntry;
import org.json.JSONException;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import it.qbteam.stalkerapp.contract.LDAPorganizationContract;

public class StalkerLDAP implements LDAPorganizationContract.Interactor {

        private LDAPorganizationContract.LDAPlistener ldaPlistener;
        private static final String TAG = "StalkerLDAP";
        private LDAPConnection connection;
        private BindResult result;
        private String bindDN;
        private String bindPassword;
        private String serverAddress;
        private String orgAuthServerId;
        private int serverPort;
        private SearchResultEntry entry;

        //StalkerLDAP's constructor.
        public StalkerLDAP(String serverAddress, int port, String binDn, String password,LDAPorganizationContract.LDAPlistener ldaPlistener) {
            this.serverAddress = serverAddress;
            this.serverPort = port;
            this.bindDN = binDn;
            this.bindPassword = password;
            this.ldaPlistener = ldaPlistener;
        }

        //Uses to authenticate clients to the directory server,
        //to establish an authorization identity that will be used for subsequent operations processed on that connection,
        //and to specify the LDAP protocol version that the client will use.
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

        }

        //Uses to retrieve partial or complete copies of entries matching a given set of criteria.
        @Override
        public void performSearch() throws ExecutionException, InterruptedException, IOException, JSONException {
            FutureTask<SearchResultEntry> searchFutureTask = new FutureTask<>(new Callable<SearchResultEntry>() {
                @Override
                public SearchResultEntry call() throws Exception {
                    return connection.getEntry(bindDN);
                }
            });
            new Thread(searchFutureTask).start();
            this.entry = searchFutureTask.get();

            this.connection.close();
            if(this.result != null && this.entry != null) {
                this.orgAuthServerId = "" + this.entry.getAttribute("uidNumber").getValue();
                ldaPlistener.onSuccess("Ti sei autenticato con successo");
            }
            else {
                ldaPlistener.onFailure("Errore durante l'autenticazione");
            }
        }

        public String getBindDN(){
            return bindDN;
        }

        public String getBindPassword(){
            return bindPassword;
        }


    }