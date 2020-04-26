package it.qbteam.stalkerapp.model.service;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchResultEntry;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class StalkerLDAP {
    private LDAPConnection ldapConnection;
    private BindResult bindResult;
    private String host;
    private String bindDN;
    private String password;
    private int port;

}