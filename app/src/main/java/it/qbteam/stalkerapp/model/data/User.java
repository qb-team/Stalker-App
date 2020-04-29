package it.qbteam.stalkerapp.model.data;

public class User {
    String token;
    public User(String t){
        this.token=t;
    }
    public String getToken(){
        return this.token;
    }
}
