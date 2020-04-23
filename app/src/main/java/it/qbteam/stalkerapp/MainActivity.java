package it.qbteam.stalkerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import it.qbteam.stalkerapp.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //public static FragmentManager fragmentManager;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button logout = findViewById(R.id.logoutID);
        logout.setOnClickListener(this);


        //fragmentManager=getSupportFragmentManager();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (!Tools.isUserLogged(this))
            goToLoginActivity();
        else
        {
            //vengono mostrare organizzazioni
        }
    }



    public void goToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logoutID:
                FirebaseAuth.getInstance().signOut();   //logout
                goToLoginActivity();
                break;
        }
    }
}
