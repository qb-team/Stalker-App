package it.qbteam.stalkerapp.model.service;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchResultEntry;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import it.qbteam.stalkerapp.presenter.LDAPorganizationContract;

public class StalkerLDAP implements LDAPorganizationContract.Model {
    private LDAPConnection ldapConnection;
    private BindResult bindResult;
    private String host;
    private String bindDN;
    private String password;
    private int port;
    private SearchResultEntry entry;

    public StalkerLDAP(String host, int port, String bindDN, String password)  {
        this.host=host;
        this.port=port;
        this.bindDN=bindDN;
        this.password=password;

    }
    public void performBind() throws LDAPException, ExecutionException, InterruptedException {
        this.ldapConnection=new LDAPConnection(host,port);
        FutureTask<BindResult> bindFutureTask = new FutureTask<>(new Callable<BindResult>() {
            @Override
            public BindResult call() throws Exception {
                return ldapConnection.bind(bindDN, password);
            }
        });
        new Thread(bindFutureTask).start();
        this.bindResult = bindFutureTask.get();
    }



    public void performSearch() throws ExecutionException, InterruptedException {
        FutureTask<SearchResultEntry> searchFutureTask = new FutureTask<>(new Callable<SearchResultEntry>() {
            @Override
            public SearchResultEntry call() throws Exception {
                return ldapConnection.getEntry(bindDN);
            }
        });
        new Thread(searchFutureTask).start();
        this.entry = searchFutureTask.get();
        this.ldapConnection.close();

    }


}