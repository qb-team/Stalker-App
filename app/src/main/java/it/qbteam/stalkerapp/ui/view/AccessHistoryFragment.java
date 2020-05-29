package it.qbteam.stalkerapp.ui.view;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.contract.AccessHistoryContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationAccess;
import it.qbteam.stalkerapp.presenter.AccessHistoryPresenter;
import it.qbteam.stalkerapp.tools.AccessHistoryViewAdapter;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;
//import lombok.SneakyThrows;

/**
 * A simple {@link Fragment} subclass.
 */

public class AccessHistoryFragment extends Fragment implements AccessHistoryContract.View, SearchView.OnQueryTextListener, AccessHistoryViewAdapter.OrganizationAccessListener, OnBackPressListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FloatingActionButton buttonDelete;
    private AccessHistoryPresenter accessHistoryPresenter;
    private List<OrganizationAccess> accessList;
    private AccessHistoryFragmentListener accessHistoryFragmentListener;

    //Interfate to communicate with MyStalkerListFragment through the HomePageActivity.
    public interface AccessHistoryFragmentListener {
        void disableScroll(boolean enable);
    }

    // This method insures that the Activity has actually implemented our
    // listener and that it isn't null.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AccessHistoryFragmentListener) {
            accessHistoryFragmentListener = (AccessHistoryFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " AccessHistoryFragmentListener");
        }
    }

    public AccessHistoryFragment() {
        // Required empty public constructor
    }

    //Creation of the fragment as a component.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
       // recyclerView.setAdapter(adapter );
        accessHistoryPresenter= new AccessHistoryPresenter(this);
        try {
            printAccess();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        buttonDelete= view.findViewById(R.id.accessID);
        buttonDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    accessHistoryPresenter.deleteOrganizationAccess();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        return view;
    }

    public void printAccess() throws IOException, ClassNotFoundException {
        accessHistoryPresenter.getOrganizationAccess();
    }

    @Override
    public void onSuccessGetOrganizationAccessInLocal(List<OrganizationAccess> organizationAccessList) {
        if(organizationAccessList!=null){
            accessList=organizationAccessList;
            adapter = new AccessHistoryViewAdapter(organizationAccessList, getActivity(), this);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void onSuccessDeleteOrganizationAccess() {
        adapter = new AccessHistoryViewAdapter(null, getActivity(), this);
        recyclerView.setAdapter(adapter);
        accessList=null;
    }

    //It hides to menu actionTab the option "Aggiungi a MyStalkers".
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem item= menu.findItem(R.id.searchID);
        item.setVisible(true);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput= newText.toLowerCase();
        List<OrganizationAccess> newList= new ArrayList<>();
        if(accessList!=null){
            for(int i = 0; i< accessList.size(); i++){
                if(accessList.get(i).getOrgName().toLowerCase().contains(userInput))
                    newList.add(accessList.get(i));
            }
            adapter=new AccessHistoryViewAdapter(newList,getActivity(),this);
            recyclerView.setAdapter(adapter);
        }
        return false;
    }

    @Override
    public void organizationClick(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("name", accessList.get(position).getOrgName());
        bundle.putLong("orgID", accessList.get(position).getOrganizationId());
        PlaceAccessFragment placeAccessFragment= new PlaceAccessFragment();
        placeAccessFragment.setArguments(bundle);
        FragmentTransaction transaction= getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.AccessHistoryID, placeAccessFragment).commit();
        HomePageActivity.getTabLayout().setVisibility(View.GONE);
        accessHistoryFragmentListener.disableScroll(false);
    }


    @Override
    public void organizationLongClick(int position) {

    }

    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }
}
