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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.model.data.User;
import it.qbteam.stalkerapp.model.service.Server;
import it.qbteam.stalkerapp.model.service.Storage;
import it.qbteam.stalkerapp.model.tracking.TrackingStalker;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;
import it.qbteam.stalkerapp.contract.MyStalkersListContract;
import it.qbteam.stalkerapp.presenter.MyStalkersListPresenter;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.tools.OrganizationViewAdapter;
import it.qbteam.stalkerapp.tools.Utils;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyStalkersListFragment extends Fragment implements MyStalkersListContract.View, OrganizationViewAdapter.OrganizationListener, SearchView.OnQueryTextListener, OnBackPressListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static TrackingStalker mService;
    private MyStalkersListPresenter myStalkersListPresenter;
    private List<Organization> organizationList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private String path;
    private static MyStalkersListFragment instance = null;
    private User user;
    private boolean mBound = false;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        //Internal class method `ServiceConnection` which allows you to establish a connection with the` Bind Service`.
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TrackingStalker.LocalBinder binder = (TrackingStalker.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        //Method of the internal class `ServiceConnection` which allows you to disconnect the connection with the` Bind Service`.
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    //Creation of the fragment as a component.
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

    //Creation of the graphic part displayed by the user.
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mystalker_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        myStalkersListPresenter = new MyStalkersListPresenter(this);
        return view;
    }

    //Initializes and allows the user to view the fragment of the organization following a quick click.
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

    //Notifies the user through a dialog box, the possibility of deleting the organization selected by the user after a long click.
    @Override
    public void organizationLongClick(int position) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getContext())
                // set message, title, and icon
                .setTitle("Elimina organizzazione")
                .setMessage("Sei sicuro di voler eliminare l'organizzazione?")
                .setPositiveButton("Elimina", (dialog, whichButton) -> {
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

                    }
                    catch (IOException | JSONException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                })
                .setNegativeButton("Annulla", (dialog, which) -> dialog.dismiss())
                .create();
        myQuittingDialogBox.show();
    }

    //Hide the 'add to favorites' option from the application's action tab menu and make the search command visible.
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.favoriteID).setVisible(false);
        MenuItem item = menu.findItem(R.id.searchID);
        item.setVisible(true);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        super.onPrepareOptionsMenu(menu);
    }

    //End click listener.
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    //Display the list of organizations on the screen following the inputs entered by the user in the search menu.
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

    //Add the organization received as input to both the FileSystem and the Server.
    public void addOrganization(Organization organization) throws IOException, JSONException {
        myStalkersListPresenter.addOrganizationLocal(organization, organizationList, path);
        myStalkersListPresenter.addOrganizationServer(organization, user.getUid(), user.getToken());


    }
    public void downloadPlaceServer(Organization organization){
        Server.performDownloadPlaceServer(organization,user.getToken());

    }

    //Notifies the user of the success of the organization's add operation.
    @Override
    public void onSuccessAddOrganization(List<Organization> list, String message) throws IOException, JSONException {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        adapter = new OrganizationViewAdapter(list, this.getContext(), this);
        recyclerView.setAdapter(adapter);
        myStalkersListPresenter.updateFile(list, path);
    }

    //Notifies the user that the organization's addition operation has failed.
    @Override
    public void onFailureAddOrganization(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    //Removes an organization from both the FileSystem and the Server.
    public void removeOrganization(int position) throws IOException, JSONException, ClassNotFoundException {
        myStalkersListPresenter.removeOrganizationServer(organizationList.get(position), user.getUid(), user.getToken());
        myStalkersListPresenter.removeOrganizationLocal(organizationList.get(position), organizationList, path);
    }

    //Notifies the user of the success of an organization's removal operation.
    @Override
    public void onSuccessRemoveOrganization(List<Organization> list) throws IOException, JSONException {
        adapter = new OrganizationViewAdapter(list, this.getContext(), this);
        recyclerView.setAdapter(adapter);
        myStalkersListPresenter.updateFile(list, path);
    }

    //Downloads from the Server the list of organizations previously added by the user.
    public void loadMyStalkerList(String UID, String userToken) {
        myStalkersListPresenter.downloadListServer(UID, userToken);
    }

    //Keeps track of any changes made by the user of his list of organizations in the `MyStalkerListFragment` view.
     public List<Organization> checkForUpdate(){
        return myStalkersListPresenter.checkLocalFile(path);
     }

    //It notifies the user of the successful download of his list of organizations included in `MyStalkersList` and shows them on the screen.
    @Override
    public void onSuccessLoadMyStalkerList(List<Organization> list) throws IOException, JSONException {

        if (list != null) {
            ArrayList<Organization> aux = new ArrayList<>(list);
            //Assegno la lista appena scaricata dal server
            organizationList.addAll(aux);
            adapter = new OrganizationViewAdapter(organizationList, this.getContext(), this);
            recyclerView.setAdapter(adapter);
            myStalkersListPresenter.updateFile(organizationList, path);

        }

        else
            Toast.makeText(getContext(), "Lista MyStalker ancora vuota", Toast.LENGTH_SHORT).show();
    }

    //This method is invoked when the main Activity comes is paused and its return is expected in a short time.
    @Override
    public void onPause() {
        super.onPause();
    }

    //This method is invoked when the main Activity is no longer visible to the user, that is, when the latter has decided to close the application.
    @Override
    public void onStop() {

        if (mBound) {
            getContext().unbindService(mServiceConnection);
            mBound = false;
        }

        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    //Method that is called when a shared resource between two views is modified, added or removed.
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        if (s.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES)) {
            HomePageActivity homePageActivity= new HomePageActivity();
            homePageActivity.setSwitchState(sharedPreferences.getBoolean(Utils.KEY_REQUESTING_LOCATION_UPDATES,
                    false));
        }

    }

    //Manage the start of tracking by referring to the organizations chosen and entered by the user in the `MyStalkersList` view.
    public void startTracking() throws IOException {
        Storage.deleteMovement();
        mService.requestLocationUpdates();
    }


    //Manage the end of the tracking by referring to the organizations chosen and entered by the user in the `MyStalkersList` view.
    public void stopTracking() {
        mService.removeLocationUpdates();
    }

    //Returns the user to the previous Activity or Fragment.
    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }

}