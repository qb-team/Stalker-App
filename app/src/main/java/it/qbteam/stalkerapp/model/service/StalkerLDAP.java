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

public class StalkerLDAP extends AsyncTask<Void,Void,String> implements LDAPorganizationContract.Model {
    private LDAPConnection ldapConnection;
    private BindResult bindResult;
    private String host;
    private String bindDN;
    private String password;
    private int port;
    private SearchResultEntry entry;

    public StalkerLDAP(String host, int port, String bindDN, String password) {

        this.host = host;
        this.port = port;
        this.bindDN = bindDN;
        this.password = password;

    }
    public String getHost(){
        return this.host;
    }
    public int getPort(){
        return this.port;
    }
    public String getBindDN(){
        return this.bindDN;
    }
    public String getPassword(){
        return this.password;
    }

    public void performBind() throws LDAPException, ExecutionException, InterruptedException {

    }


    public void performSearch() throws ExecutionException, InterruptedException {
        System.out.println("performSearch");
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


    @Override
    protected String doInBackground(Void... voids) {
        System.out.println("performBind" + this.host + this.port);

        try {
            this.ldapConnection = new LDAPConnection(host, port);
        } catch (LDAPException e) {
            e.printStackTrace();
        }
        FutureTask<BindResult> bindFutureTask = new FutureTask<>(new Callable<BindResult>() {
            @Override
            public BindResult call() throws Exception {
                return ldapConnection.bind(bindDN, password);
            }
        });

        new Thread(bindFutureTask).start();
        try {
            this.bindResult = bindFutureTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
return"ok";
    }
}