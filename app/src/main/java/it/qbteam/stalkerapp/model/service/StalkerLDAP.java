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


    public class StalkerLDAP implements LDAPorganizationContract.Model {
        private static final String TAG = "com.vartmp7.stalker.component.StalkerLDAP";

        private LDAPConnection connection;
        private BindResult result;

        private String bindDN;
        private String bindPassword;
        private String serverAddress;
        private int serverPort;

        private SearchResultEntry entry;

        public StalkerLDAP(String serverAddress, int port, String binDn, String password) {
            this.serverAddress = serverAddress;
            this.serverPort = port;
            this.bindDN = binDn;
            this.bindPassword = password;

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
        }
    }