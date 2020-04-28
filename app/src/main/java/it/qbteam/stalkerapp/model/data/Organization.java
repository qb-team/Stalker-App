package it.qbteam.stalkerapp.model.data;

import androidx.fragment.app.Fragment;

import it.qbteam.stalkerapp.ui.view.AbstractOrganizationFragment;
import it.qbteam.stalkerapp.ui.view.LDAPorganizationFragment;
import it.qbteam.stalkerapp.ui.view.StandardOrganizationFragment;

public class Organization implements Comparable<Organization>{

    private String nome;
    private String typeAccess;
    private AbstractOrganizationFragment fragment;

    public Organization(String nome){
        this.nome=nome;
        this.typeAccess="LDAP";
        fragment=new StandardOrganizationFragment();
    }

    public String getNome(){
        return nome;
    }
    public String getType()  { return typeAccess;}
    public boolean equals(Organization o) {
        if (o instanceof Organization) {
            Organization aux = (Organization) o;
            return aux.getNome() == this.nome;
        }
        return false;
    }
    public int compareTo(Organization o)
    {
        return nome.compareTo(o.getNome());
    }

    public AbstractOrganizationFragment getFragment(){
        return fragment;
    }
}
