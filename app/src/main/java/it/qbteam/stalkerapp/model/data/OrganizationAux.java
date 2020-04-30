package it.qbteam.stalkerapp.model.data;

import it.qbteam.stalkerapp.ui.view.AbstractOrganizationFragment;
import it.qbteam.stalkerapp.ui.view.StandardOrganizationFragment;

public class OrganizationAux implements Comparable<OrganizationAux>{

    private String nome;
    private String typeAccess;
    private AbstractOrganizationFragment fragment;

    public OrganizationAux(String nome){
        this.nome=nome;
        this.typeAccess="LDAP";
        fragment=new StandardOrganizationFragment();
    }

    public String getNome(){
        return nome;
    }
    public String getType()  { return typeAccess;}
    public boolean equals(OrganizationAux o) {
        if (o instanceof OrganizationAux) {
            OrganizationAux aux = (OrganizationAux) o;
            return aux.getNome() == this.nome;
        }
        return false;
    }
    public int compareTo(OrganizationAux o)
    {
        return nome.compareTo(o.getNome());
    }

    public AbstractOrganizationFragment getFragment(){
        return fragment;
    }
}
