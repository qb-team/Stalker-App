package com.example.stalkerapp.ui.main;



import androidx.fragment.app.Fragment;


import com.example.stalkerapp.BackPressImplementation;
import com.example.stalkerapp.OnBackPressListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class RootFragment extends Fragment implements OnBackPressListener {

    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }
}
