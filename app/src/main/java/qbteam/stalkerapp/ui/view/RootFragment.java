package qbteam.stalkerapp.ui.view;



import androidx.fragment.app.Fragment;


import qbteam.stalkerapp.BackPressImplementation;
import qbteam.stalkerapp.OnBackPressListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class RootFragment extends Fragment implements OnBackPressListener {

    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }
}
