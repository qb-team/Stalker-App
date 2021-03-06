package it.qbteam.stalkerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import it.qbteam.stalkerapp.ui.view.LogInFragment;

public class MainActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager;

    //Creates Activity and manages the fragments connected to it.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.containerID, new LogInFragment()).commit();
    }

}
