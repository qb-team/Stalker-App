package it.qbteam.stalkerapp.ui.view;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.model.backend.model.Organization;
import it.qbteam.stalkerapp.model.data.User;
import it.qbteam.stalkerapp.model.tracking.TrackingStalker;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;
import it.qbteam.stalkerapp.presenter.MyStalkersListContract;
import it.qbteam.stalkerapp.presenter.MyStalkersListPresenter;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.tools.OrganizationViewAdapter;
import it.qbteam.stalkerapp.tools.Utils;

import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyStalkersListFragment extends Fragment implements MyStalkersListContract.View, OrganizationViewAdapter.OrganizationListener, SearchView.OnQueryTextListener, OnBackPressListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private MyStalkersListPresenter myStalkersListPresenter;
    private ArrayList<Organization> organizationList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private String path;
    private static MyStalkersListFragment instance = null;
    private TrackingStalker mService = null;
    private boolean mBound = false;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TrackingStalker.LocalBinder binder = (TrackingStalker.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        path= getContext().getFilesDir() + "/Preferiti.txt";

        instance=this;

        getContext().bindService(new Intent(getContext(), TrackingStalker.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mystalker_list, container, false);
        recyclerView=view.findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        myStalkersListPresenter =new MyStalkersListPresenter(this);
        organizationList=new ArrayList<>();

        loadMyStalkerList(ActionTabFragment.getInstance().getUserToken(),ActionTabFragment.getInstance().getUID());



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("ciao:  " + mService);

    }



    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.favoriteID).setVisible(false);
        MenuItem item= menu.findItem(R.id.searchID);
        item.setVisible(true);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        super.onPrepareOptionsMenu(menu);
    }


    public static MyStalkersListFragment getInstance() {
        return instance;
    }

    public void loadMyStalkerList(String UID, String userToken) {

        myStalkersListPresenter.loadList(UID,userToken);
    }

    //  MyAdapter.OnOrganizzazioneListener DA METTERE ALTRI 2 METODI LDAP E STANDARD COME RISPOSTE
    @Override
    public void organizationClick(int position) {

        Bundle bundle=new Bundle();
        bundle.putString("name", organizationList.get(position).getName());
        bundle.putString("description", organizationList.get(position).getDescription());
        bundle.putString("image", organizationList.get(position).getImage());

        if(organizationList.get(position).getTrackingMode().getValue()=="anonymous"){
            StandardOrganizationFragment stdOrgFragment= new StandardOrganizationFragment();
            stdOrgFragment.setArguments(bundle);
            FragmentTransaction transaction= getChildFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.ListaPreferitiID, stdOrgFragment).commit();
        }
        else{

            LDAPorganizationFragment LDAPFragment= new LDAPorganizationFragment();
            LDAPFragment.setArguments(bundle);
            FragmentTransaction transaction= getChildFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.ListaPreferitiID, LDAPFragment).commit();
        }

    }

    @Override
    public void organizationLongClick(int position) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getContext())
                // set message, title, and icon
                .setTitle("Elimina organizzazione")
                .setMessage("Sei sicuro di voler eliminare l'organizzazione?")
                .setPositiveButton("Elimina", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        try {
                            removeOrganization(position);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        myQuittingDialogBox.show();
    }


    public void removeOrganization(int position) throws IOException, JSONException {
        myStalkersListPresenter.removeRest(organizationList.get(position),ActionTabFragment.getInstance().getUserToken(),ActionTabFragment.getInstance().getUID());
        myStalkersListPresenter.remove(organizationList.get(position), organizationList, path);

    }

    public void addOrganization(Organization organization) throws IOException, JSONException {
        myStalkersListPresenter.addOrganizationLocal(organization, organizationList, path);

    }


    public void alphabeticalOrder(){
        Collections.sort(organizationList);
        try {
            adapter=new OrganizationViewAdapter(organizationList,this.getContext(),this);
            recyclerView.setAdapter(adapter);
            myStalkersListPresenter.updateFile(organizationList,path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput= newText.toLowerCase();
        ArrayList<Organization> newList= new ArrayList<>();
        if(organizationList.size()!=0){
            for(int i = 0; i< organizationList.size(); i++){
                if(organizationList.get(i).getName().toLowerCase().contains(userInput))
                    newList.add(organizationList.get(i));
            }
            adapter=new OrganizationViewAdapter(newList,this.getContext(),this);
            recyclerView.setAdapter(adapter);

        }
        return false;
    }

    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }

    public ArrayList <Organization> getMyStalkerList(){
        return organizationList;

    }
    @Override
    public void onSuccessCheckFile(ArrayList<Organization> list) {
        adapter=new OrganizationViewAdapter(list,this.getContext(),this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFailureCheckFile(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessAddOrganization(String message) throws IOException, JSONException {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        adapter=new OrganizationViewAdapter(organizationList,this.getContext(),this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onFailureAddOrganization(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessRemoveOrganization(ArrayList<Organization> list) throws IOException, JSONException {
        adapter=new OrganizationViewAdapter(list,this.getContext(),this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSuccessAddOrganizationRest(String message) {

    }

    @Override
    public void onFailureAddOrganizationRest(String message) {

    }

    @Override
    public void onSuccessLoadFile(List<Organization> list) {
        if(list!=null){
            ArrayList<Organization> aux= new ArrayList<>(list);
            adapter=new OrganizationViewAdapter(aux,this.getContext(),this);
            recyclerView.setAdapter(adapter);
        }
        else
            Toast.makeText(getContext(),"Lista MyStalker ancora vuota",Toast.LENGTH_SHORT).show();
    }

    public void addOrganizationRest(Organization organization, String UID, String userToken) throws IOException, JSONException {
        myStalkersListPresenter.addOrganizationRest(organization, UID,userToken);
    }
    @Override
    public void onResume() {
        super.onResume();
        //organizationList=checkFile();

        if(organizationList!=null){
            adapter=new OrganizationViewAdapter(organizationList,this.getContext(),this);
            recyclerView.setAdapter(adapter);
        }
        else
            Toast.makeText(getActivity(),"Lista ancora vuota!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        if (mBound) {
            getContext().unbindService(mServiceConnection);
            mBound = false;
        }


        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);

        super.onStop();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        // Update the buttons state depending on whether location updates are being requested.
        if (s.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES)) {
            HomePageActivity.getInstance().setSwitchState(sharedPreferences.getBoolean(Utils.KEY_REQUESTING_LOCATION_UPDATES,
                    false));
        }
    }

    public void startTracking(){
        createListOrganization();
        System.out.println("lala:  " + mService);
        mService.requestLocationUpdates();
    }

    public void stopTracking(){
        mService.removeLocationUpdates();
    }

    private void createListOrganization(){
        try {
            mService.createOrganizationArrayList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}