package it.qbteam.stalkerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import it.qbteam.stalkerapp.ui.view.AuthenticationFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    //Method that is invoked when the application is opened, checks if the user is authenticated and if so, the goToHomePage () method is invoked.
    @Override
    protected void onStart() {
        super.onStart();
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null ) {
            goToHomePage();
        }
    }

    //Creates Activity and manages the fragments connected to it.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.containerID, new AuthenticationFragment(), "Main").commit();

    }

    //Moves the user to HomePageActivity.
    public void goToHomePage(){
        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



}
