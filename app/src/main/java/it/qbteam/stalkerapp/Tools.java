package it.qbteam.stalkerapp;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

public class Tools {

    public static boolean isUserLogged(Context context) {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }



}
