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
    private User user;
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
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        organizationList=new ArrayList<>();
        path= getContext().getFilesDir() + "/Preferiti.txt";
        if (FirebaseAuth.getInstance().getCurrentUser() != null ) {
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            mUser.getIdToken(true)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            user=new User(task.getResult().getToken(),FirebaseAuth.getInstance().getCurrentUser().getUid());
                            System.out.println("TOKEN CREATO:"+task.getResult().getToken()+"UID CREATO:"+FirebaseAuth.getInstance().getCurrentUser().getUid());
                            loadMyStalkerList(user.getUid(),user.getToken());
                        } else {
                            // Handle error -> task.getException();
                        }
                    });
        }
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


        return view;
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
        myStalkersListPresenter.removeOrganizationRest(organizationList.get(position),user.getUid(),user.getToken());
        myStalkersListPresenter.remove(organizationList.get(position), organizationList, path);

    }

    public void addOrganization(Organization organization) throws IOException, JSONException {
        myStalkersListPresenter.addOrganizationLocal(organization, organizationList, path);

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


    //Organizzazione aggiunta correttamente ai preferiti
    @Override
    public void onSuccessAddOrganization(String message)  {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        adapter=new OrganizationViewAdapter(organizationList,this.getContext(),this);
        recyclerView.setAdapter(adapter);


    }

    //Organizzazione già presente nei preferiti
    @Override
    public void onFailureAddOrganization(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    //Organizzazione rimossa con successo
    @Override
    public void onSuccessRemoveOrganization(ArrayList<Organization> list)  {
        adapter=new OrganizationViewAdapter(list,this.getContext(),this);
        recyclerView.setAdapter(adapter);
    }


    //Lista organizzazioni preferiti caricate da server con successo
    @Override
    public void onSuccessLoadFile(List<Organization> list) throws IOException, JSONException {
        if(list!=null){
            ArrayList<Organization> aux= new ArrayList<>(list);
            adapter=new OrganizationViewAdapter(aux,this.getContext(),this);
            recyclerView.setAdapter(adapter);
            myStalkersListPresenter.updateFile(aux,path);
            organizationList=aux;

        }
        else
            Toast.makeText(getContext(),"Lista MyStalker ancora vuota",Toast.LENGTH_SHORT).show();
    }

    //Organizzazione aggiunta sul server
    public void addOrganizationRest(Organization organization, String UID, String userToken)  {
        myStalkersListPresenter.addOrganizationRest(organization, UID,userToken);
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
    //Metodo che fa iniziare il tracciamento sulle organizzaioni presenti dei preferiti
    public void startTracking(){
        createListOrganization();
        System.out.println("lala:  " + mService);
        mService.requestLocationUpdates();
    }
    //Metodo che fa fermare il tracciamneto sulle organizzazioni presenti nei preferiti
    public void stopTracking(){
        mService.removeLocationUpdates();
    }

    private void createListOrganization(){
        try {
            mService.createOrganizationArrayList(organizationList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}