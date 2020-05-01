package it.qbteam.stalkerapp.presenter;

import com.unboundid.ldap.sdk.LDAPException;
import java.util.concurrent.ExecutionException;
import it.qbteam.stalkerapp.model.service.StalkerLDAP;

public class LDAPorganizationPresenter implements LDAPorganizationContract.Presenter, LDAPorganizationContract.LDAPlistener{

    private StalkerLDAP stalkerLDAP;
   private LDAPorganizationContract.View ldapView;

   public LDAPorganizationPresenter(LDAPorganizationContract.View ldapView){
       this.ldapView=ldapView;
   }
    @Override
    public void bind() throws InterruptedException, LDAPException, ExecutionException {
        stalkerLDAP.performBind();
    }

    @Override
    public void search() throws ExecutionException, InterruptedException {
        stalkerLDAP.performSearch();
    }

    @Override
    public StalkerLDAP getLDAP() {
        return stalkerLDAP.getInstance();
    }

    @Override
    public void setLDAP(String host, int port, String bindDN, String password) {
        this.stalkerLDAP=new StalkerLDAP(host,  port, bindDN, password,this);
    }

    @Override
    public void onSuccess(String message) {
        ldapView.onSuccessLdap(message);
    }

    @Override
    public void onFailure(String message) {
        ldapView.onFailureLdap(message);
    }
}
