package it.qbteam.stalkerapp.ui.view;



import androidx.fragment.app.Fragment;


import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class RootFragment extends Fragment implements OnBackPressListener {

    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }
}
