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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.model.backend.modelBackend.Organization;
import it.qbteam.stalkerapp.model.data.User;
import it.qbteam.stalkerapp.model.service.Storage;
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
        organizationList = new ArrayList<>();
        path = getContext().getFilesDir() + "/Preferiti.txt";
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            mUser.getIdToken(true)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            user = new User(task.getResult().getToken(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                            System.out.println("TOKEN CREATO:" + task.getResult().getToken() + "UID CREATO:" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                            loadMyStalkerList(user.getUid(), user.getToken());
                        } else {
                            // Handle error -> task.getException();
                        }
                    });
        }
        instance = this;

        getContext().bindService(new Intent(getContext(), TrackingStalker.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mystalker_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        myStalkersListPresenter = new MyStalkersListPresenter(this);
        return view;
    }


    public static MyStalkersListFragment getInstance() {
        return instance;
    }

    //Click listener
    @Override
    public void organizationClick(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("name", organizationList.get(position).getName());
        bundle.putString("description", organizationList.get(position).getDescription());
        bundle.putLong("orgID", organizationList.get(position).getId());
        bundle.putString("image", organizationList.get(position).getImage());
        bundle.putString("serverURL", organizationList.get(position).getAuthenticationServerURL());
        bundle.putString("creationDate", organizationList.get(position).getCreationDate().toString());

        if (organizationList.get(position).getTrackingMode().getValue() == "anonymous") {
            StandardOrganizationFragment stdOrgFragment = new StandardOrganizationFragment();
            stdOrgFragment.setArguments(bundle);
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.ListaPreferitiID, stdOrgFragment).commit();
        } else {

            LDAPorganizationFragment LDAPFragment = new LDAPorganizationFragment();
            LDAPFragment.setArguments(bundle);
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
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
                            if (Storage.deserializeMovementInLocal()!=null&&organizationList.get(position).getId().equals(Storage.deserializeMovementInLocal().getOrganizationId())){
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Condizioni di eliminazione organizzazione")
                                        .setMessage("Attualmente sei tracciato in questa organizzazione, prima di eliminarla devi uscire dall'organizzazione")
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });
                                builder.show();
                            }
                              else removeOrganization(position);
                        } catch (IOException | JSONException | ClassNotFoundException e) {
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

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.favoriteID).setVisible(false);
        MenuItem item = menu.findItem(R.id.searchID);
        item.setVisible(true);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        super.onPrepareOptionsMenu(menu);
    }

    //End click listener
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        ArrayList<Organization> newList = new ArrayList<>();
        if (organizationList.size() != 0) {
            for (int i = 0; i < organizationList.size(); i++) {
                if (organizationList.get(i).getName().toLowerCase().contains(userInput))
                    newList.add(organizationList.get(i));
            }
            adapter = new OrganizationViewAdapter(newList, this.getContext(), this);
            recyclerView.setAdapter(adapter);

        }
        return false;
    }


    public void addOrganization(Organization organization) throws IOException, JSONException {
        myStalkersListPresenter.addOrganizationLocal(organization, organizationList, path);
        myStalkersListPresenter.addOrganizationREST(organization, user.getUid(), user.getToken());
    }

    //Organizzazione aggiunta correttamente ai preferiti
    @Override
    public void onSuccessAddOrganization(ArrayList<Organization> list, String message) throws IOException, JSONException {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        adapter = new OrganizationViewAdapter(list, this.getContext(), this);
        recyclerView.setAdapter(adapter);
        myStalkersListPresenter.updateFile(list, path);
    }

    //Organizzazione già presente nei preferiti
    @Override
    public void onFailureAddOrganization(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
    //da migliorare, perchè se elimino una organizzazione mentro sono dentro e il gps mi sta tracciando, non mi dice che sono uscito perche token sono diversi
    public void removeOrganization(int position) throws IOException, JSONException, ClassNotFoundException {

        myStalkersListPresenter.removeOrganizationREST(organizationList.get(position), user.getUid(), user.getToken());
        myStalkersListPresenter.removeOrganizationLocal(organizationList.get(position), organizationList, path);

    }

    //Organizzazione rimossa con successo
    @Override
    public void onSuccessRemoveOrganization(ArrayList<Organization> list) throws IOException, JSONException {
        adapter = new OrganizationViewAdapter(list, this.getContext(), this);
        recyclerView.setAdapter(adapter);
        myStalkersListPresenter.updateFile(list, path);
    }

    public void loadMyStalkerList(String UID, String userToken) {

        myStalkersListPresenter.downloadListREST(UID, userToken);
    }
    //Viene usato dal StalkerTracker per verificare se la lista è aggiornata oppure no
     public ArrayList<Organization> checkForUpdate(){
        return myStalkersListPresenter.checkLocalFile(path);
     }
    //Lista organizzazioni preferiti caricate da server con successo

    @Override
    public void onSuccessLoadMyStalkerList(List<Organization> list) throws IOException, JSONException {
        if (list != null) {
            //Converto la list in ArrayList
            ArrayList<Organization> aux = new ArrayList<>(list);
            //Assegno la lista appena scaricata dal server
            organizationList.addAll(aux);
            adapter = new OrganizationViewAdapter(organizationList, this.getContext(), this);
            recyclerView.setAdapter(adapter);
            myStalkersListPresenter.updateFile(organizationList, path);

        } else
            Toast.makeText(getContext(), "Lista MyStalker ancora vuota", Toast.LENGTH_SHORT).show();
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
    public void startTracking() throws IOException {

        Storage.deleteMovement();
        mService.requestLocationUpdates();
    }


    //Metodo che fa fermare il tracciamneto sulle organizzazioni presenti nei preferiti
    public void stopTracking() {
        mService.removeLocationUpdates();

    }

    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }

}