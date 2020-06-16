package it.qbteam.stalkerapp.presenter;

import com.unboundid.ldap.sdk.LDAPException;
import org.json.JSONException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import it.qbteam.stalkerapp.contract.LDAPorganizationContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;
import it.qbteam.stalkerapp.model.service.StalkerLDAP;
import it.qbteam.stalkerapp.model.service.Storage;

public class LDAPorganizationPresenter implements LDAPorganizationContract.Presenter, LDAPorganizationContract.LDAPlistener{

    private StalkerLDAP stalkerLDAP;
    private LDAPorganizationContract.View ldapView;
    private Storage storage;

    //LDAPorganizationPresenter's constructor.
    public LDAPorganizationPresenter(LDAPorganizationContract.View ldapView){
       this.ldapView = ldapView;
       storage = new Storage(null,null);
    }

    //Calls the the method performBind() of the class StalkerLDAP(persistent layer of the LDAP server).
    @Override
    public void bind() throws InterruptedException, LDAPException, ExecutionException {
        stalkerLDAP.performBind();
    }

    //Calls the the method performSearch() of the class StalkerLDAP(persistent layer of the LDAP server).
    @Override
    public void search() throws ExecutionException, InterruptedException, IOException, JSONException {
        stalkerLDAP.performSearch();
    }

    //Builds the instance of the class StalkerLDAP.
    @Override
    public void setLDAP(String host, int port, String bindDN, String password) {
        this.stalkerLDAP = new StalkerLDAP(host,  port, bindDN, password,this);
    }

    //Comunicates the success result of the ldap comunication to the view.
    @Override
    public void onSuccess(String message) throws IOException, JSONException {
        ldapView.onSuccessLdap(message);
    }

    //Comunicates the failure result of the ldap comunication to the view.
    @Override
    public void onFailure(String message) {
        ldapView.onFailureLdap(message);
    }


    @Override
    public OrganizationMovement getLastAccess(Long orgID) throws IOException, ClassNotFoundException {
        return storage.performGetLastAccess(orgID);
    }
}