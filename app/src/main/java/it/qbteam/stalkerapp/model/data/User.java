package it.qbteam.stalkerapp.model.data;

public class User {
    private String token;
    private String uid;
    public User(String token, String uid) {
        this.token=token;
        this.uid=uid;
    }

    public String getUid(){
        return this.uid;
    }
    public String getToken(){
        return this.token;
    }




}


