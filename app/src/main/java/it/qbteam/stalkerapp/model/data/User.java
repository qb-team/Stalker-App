package it.qbteam.stalkerapp.model.data;

public class User {

    private String token;
    private String uid;

    public User() {

    }

    //User's constructor.
    public User(String token, String uid) {
        this.token=token;
        this.uid=uid;
    }

    //Returns the user's id.
    public String getUid(){ return this.uid; }

    //Returns the user's token.
    public String getToken(){
        return this.token;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setToken(String token) {
        this.token = token;
    }


}


