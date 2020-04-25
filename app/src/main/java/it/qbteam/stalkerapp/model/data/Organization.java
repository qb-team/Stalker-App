package it.qbteam.stalkerapp.model.data;

public class Organization implements Comparable<Organization>{

    private String nome;
    private String typeAccess;

    public Organization(String nome){
        this.nome=nome;
        this.typeAccess="LDAP";
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

}
