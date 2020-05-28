package it.qbteam.stalkerapp.ui.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.contract.PlaceAccessContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.PlaceAccess;
import it.qbteam.stalkerapp.presenter.PlaceAccessPresenter;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;
import lombok.SneakyThrows;


public class PlaceAccessFragment extends Fragment implements OnBackPressListener, PlaceAccessContract.View {
    private Bundle bundle;
    private PlaceAccessPresenter placeAccessPresenter;
    private  TextView placeList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @SneakyThrows
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_place_access, container, false);
        placeAccessPresenter= new PlaceAccessPresenter(this);
        bundle=this.getArguments();
        TextView nameOrg=view.findViewById(R.id.nomeOrgID);
        nameOrg.setText(bundle.getString("name"));
        placeList= view.findViewById(R.id.placeListID);
        placeAccessPresenter.getPlaceAccessList();
        return view;

    }

    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }

    @Override
    public void onSuccessGetPlaceAccessInLocal(List<PlaceAccess> organizationAccessList) {
        placeList.setText(organizationAccessList.toString());
    }
}
