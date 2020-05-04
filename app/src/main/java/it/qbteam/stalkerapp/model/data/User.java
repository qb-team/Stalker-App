package it.qbteam.stalkerapp.model.data;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class User {
    String token;
    String uid;
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


