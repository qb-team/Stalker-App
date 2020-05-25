package it.qbteam.stalkerapp.ui.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.contract.AccessHistoryContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationAccess;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;
import it.qbteam.stalkerapp.presenter.AccessHistoryPresenter;
import it.qbteam.stalkerapp.presenter.HomePresenter;
import it.qbteam.stalkerapp.tools.AccessHistoryViewAdapter;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;
import it.qbteam.stalkerapp.tools.OrganizationViewAdapter;
import lombok.SneakyThrows;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccessHistoryFragment extends Fragment implements AccessHistoryContract.View, AccessHistoryViewAdapter.OrganizationAccessListener, OnBackPressListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FloatingActionButton buttonAccess;
    private AccessHistoryPresenter accessHistoryPresenter;
    private  List<OrganizationAccess> organizationAccessList1;

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
        recyclerView = view.findViewById(R.id.recyclerAccessViewID);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter( adapter );
        accessHistoryPresenter= new AccessHistoryPresenter(this);
        organizationAccessList1= new ArrayList<>();
        adapter = new AccessHistoryViewAdapter(organizationAccessList1,getActivity(),this);
        recyclerView.setAdapter(adapter);
        buttonAccess= view.findViewById(R.id.accessID);
        buttonAccess.setOnClickListener(new View.OnClickListener() {
            @SneakyThrows
            @Override
            public void onClick(View v) {

                accessHistoryPresenter.getOrganizationAccess();

            }
        });

        return view;
    }



    @Override
    public void onSuccessGetOrganizationAccessInLocal(List<OrganizationAccess> organizationAccessList) {
        if(organizationAccessList!=null){
            adapter = new AccessHistoryViewAdapter(organizationAccessList, getActivity(), this);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void organizationClick(int position) {

    }

    @Override
    public void organizationLongClick(int position) {

    }

    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }


}
