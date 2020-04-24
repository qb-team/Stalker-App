package qbteam.stalkerapp.model.data;

public class Organization implements Comparable<Organization>{

    private String nome;

    public Organization(String nome){
        this.nome=nome;
    }

    public String getNome(){
        return nome;
    }

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
