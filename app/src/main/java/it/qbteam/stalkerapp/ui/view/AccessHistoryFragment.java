package it.qbteam.stalkerapp.ui.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.contract.AccessHistoryContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationAccess;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;
import it.qbteam.stalkerapp.presenter.AccessHistoryPresenter;
import it.qbteam.stalkerapp.presenter.HomePresenter;
import it.qbteam.stalkerapp.tools.AccessHistoryViewAdapter;
import it.qbteam.stalkerapp.tools.OrganizationViewAdapter;
import lombok.SneakyThrows;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccessHistoryFragment extends Fragment implements AccessHistoryContract.View, AccessHistoryViewAdapter.OrganizationAccessListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout refresh;
    private AccessHistoryPresenter accessHistoryPresenter;
    private  List<OrganizationAccess> organizationAccessList;
    public AccessHistoryFragment() {
        // Required empty public constructor
    }
    //Creation of the fragment as a component and instantiation of the path of the file "/Organizzazioni.txt".
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_access_history, container, false);
        refresh = view.findViewById(R.id.downloadAccessListID);
        recyclerView = view.findViewById(R.id.recyclerAccessViewID);
        recyclerView.setHasFixedSize(true);
        accessHistoryPresenter= new AccessHistoryPresenter(this);
        organizationAccessList= new ArrayList<>();


        //Refresh to download the organizations' access list (swipe down).
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SneakyThrows
            @Override
            public void onRefresh() {
                downloadAccess();
                refresh.setRefreshing(false);

            }
        });
        return view;
    }
  public void downloadAccess() throws IOException, ClassNotFoundException {

      OrganizationMovement om=accessHistoryPresenter.getOrganizationMovement();
      accessHistoryPresenter.getAnonymousOrganizationAccess(om.getExitToken(),om.getOrganizationId());


  }

    @Override
    public void onSuccessDownloadAccess(List<OrganizationAccess> list) {
        adapter = new AccessHistoryViewAdapter(list, this.getContext(),this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void organizationClick(int position) {

    }

    @Override
    public void organizationLongClick(int position) {

    }
}
