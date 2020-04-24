package qbteam.stalkerapp;

public class Organizzazioni implements Comparable<Organizzazioni>{
    private String nome;

    public Organizzazioni(String nome){
        this.nome=nome;
    }

    public String getNome(){
        return nome;
    }

    public boolean equals(Organizzazioni o) {
        if (o instanceof Organizzazioni) {
            Organizzazioni aux = (Organizzazioni) o;
            return aux.getNome() == this.nome;
        }
        return false;
    }
    public int compareTo(Organizzazioni o)
    {
        return nome.compareTo(o.getNome());
    }

}
