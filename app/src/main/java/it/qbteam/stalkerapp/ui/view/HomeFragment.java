package it.qbteam.stalkerapp.ui.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayout;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import org.json.JSONException;
import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;
import it.qbteam.stalkerapp.contract.HomeContract;
import it.qbteam.stalkerapp.presenter.HomePresenter;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.tools.OrganizationViewAdapter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.View, OrganizationViewAdapter.OrganizationListener, SearchView.OnQueryTextListener, OnBackPressListener {

    private HomePresenter OrganizationListPresenter;
    private List<Organization> organizationList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private String path;
    private FragmentListener fragmentListener;
    private ArrayList filterItem;

    //Interfate to communicate with MyStalkerListFragment through the HomePageActivity.
    public interface FragmentListener {
        void sendOrganization(Organization organization) throws IOException, JSONException;
        void disableScroll(boolean enable);
    }

    // This method insures that the Activity has actually implemented our
    // listener and that it isn't null.
   @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            fragmentListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " FragmentListener");
        }
    }


    //Creation of the fragment as a component and instantiation of the path of the file "/Organizzazioni.txt".
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        OrganizationListPresenter = new HomePresenter(this);
        try {
            OrganizationListPresenter.createAllFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        path = getContext().getFilesDir() + "/Organizzazioni.txt";
    }

    //Creation of the graphic part displayed by the user.
    @SneakyThrows
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizations_list, container, false);
        SwipeRefreshLayout refresh = view.findViewById(R.id.swiperefreshID);
        recyclerView = view.findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        //Refresh to upload the organization list (swipe down).
        refresh.setOnRefreshListener(() -> {
            downloadList();
            refresh.setRefreshing(false);
        });

        checkFile();
        return view;

    }

    //It takes care of loading the list of organizations and uploading them directly from FileSystem.
    public void checkFile()  {
        organizationList=OrganizationListPresenter.checkLocalFile(path);

        if(organizationList != null){
            adapter = new OrganizationViewAdapter(organizationList, this.getContext(),this);
            recyclerView.setAdapter(adapter);
        }
        else
            Toast.makeText(getActivity(),"Devi ancora scaricare la lista", Toast.LENGTH_SHORT).show();
    }

    //It takes care of managing a possible error while reading from FileSystem and it makes the user see the error during loading.
    @Override
    public void onFailureCheckFile(String message) {
        AlertDialog download = new AlertDialog.Builder(getContext())
                .setTitle("Scarica lista delle organizzazioni")
                .setMessage("La tua lista delle organizzazioni è ancora vuota, vuoi scaricarla?")
                .setPositiveButton("Scarica", (dialog, which) -> {
                    downloadList();

                    dialog.dismiss();
                })
                .setNegativeButton("Annulla", (dialog, which) -> {

                })
                .create();
        download.show();
    }

    //It takes care of downloading the list from the Server and it saves it on FileSystem.
    public void downloadList() {
        if(HomePageActivity.getUserToken()!=null)
        OrganizationListPresenter.downloadHomeListServer(path,HomePageActivity.getUserToken());
    }

    //It notifies the user of the correct download of the list from the Server.
    @Override
    public void onSuccessDownloadList(String message) {
        checkFile();
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    //It notifies the user of the false download of the list from the Server.
    @Override
    public void onFailureDownloadList(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    //Initializing and displaying of the fragment of the organization to the user, this method is invoked following a user click.
    @Override
    public void organizationClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("name", organizationList.get(position).getName());
        bundle.putString("description", organizationList.get(position).getDescription());
        bundle.putLong("orgID",organizationList.get(position).getId());
        bundle.putString("image", organizationList.get(position).getImage());
        bundle.putString("serverURL", organizationList.get(position).getAuthenticationServerURL());
        bundle.putString("creationDate",organizationList.get(position).getCreationDate().toString());

        if(organizationList.get(position).getTrackingMode().toString()=="anonymous"){
            StandardOrganizationFragment stdOrgFragment= new StandardOrganizationFragment();
            stdOrgFragment.setArguments(bundle);
            FragmentTransaction transaction= getChildFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.HomeFragmentID, stdOrgFragment).commit();
            fragmentListener.disableScroll(false);
        }
        else {
            LDAPorganizationFragment LDAPFragment= new LDAPorganizationFragment();
            LDAPFragment.setArguments(bundle);
            FragmentTransaction transaction= getChildFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.HomeFragmentID, LDAPFragment).commit();

            fragmentListener.disableScroll(false);
        }
    }

    //Notification received through a dialog, additional information of the organization selected by the user afterwards and a long click.
    @Override
    public void organizationLongClick(int position) {
        Dialog myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout.dialog_organizzazione);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView dialog_nomeOrganizzazione = myDialog.findViewById(R.id.dialog_nomeOrganizzazione);
        ImageView image = myDialog.findViewById(R.id.imageID);
        UrlImageViewHelper.setUrlDrawable(image,organizationList.get(position).getImage());
        dialog_nomeOrganizzazione.setText(organizationList.get(position).getName());
        myDialog.show();
        Button moreInfo=myDialog.findViewById(R.id.Button_moreInfo);
        Button aggPref=myDialog.findViewById(R.id.Button_aggiungiPreferiti);

        if(organizationList.get(position).getTrackingMode().toString() != "anonymous") {
            aggPref.setVisibility(View.INVISIBLE);
        }

        moreInfo.setOnClickListener(v -> {
            organizationClick(position);
            myDialog.dismiss();
        });

        //Try to add the organization locally and on the server.
        aggPref.setOnClickListener(v -> {

            try {

                fragmentListener.sendOrganization(organizationList.get(position));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            myDialog.dismiss();
        });
    }

    //It sorts the list in the HomeFragment view in alphabetical order.
    public void alphabeticalOrder(){
        Collections.sort(organizationList);
        try {
            adapter = new OrganizationViewAdapter(organizationList,this.getContext(),this);
            recyclerView.setAdapter(adapter);
            OrganizationListPresenter.updateFile(organizationList,path);
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    //It hides to menu actionTab the option "Aggiungi a MyStalkers".
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem item= menu.findItem(R.id.searchID);
        item.setVisible(true);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFilter();
            }
        });

        super.onPrepareOptionsMenu(menu);
    }

    private void searchFilter() {
        AlertDialog.Builder searchFilter = new AlertDialog.Builder(getContext());
        searchFilter.setTitle("Cerca per: ");
        String[] items = {"Nome","Città","Nazione"};
        boolean[] checkedItems = {false, false, false};
        searchFilter.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                switch (which) {
                    case 0:
                        if(isChecked) {
                            Toast.makeText(getContext(), "Clicked on Nome", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                        break;
                    case 1:
                        if(isChecked) {
                            Toast.makeText(getContext(), "Clicked on Città", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                        break;
                    case 2:
                        if(isChecked) {
                            Toast.makeText(getContext(), "Clicked on Data Nazione", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                        break;
                }
            }
        });
        AlertDialog alert = searchFilter.create();
        //alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    //Display the list of organizations on the screen following user inputs in the search menu.
    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput= newText.toLowerCase();
        List<Organization> newList= new ArrayList<>();

        if(organizationList.size() != 0){
            for(int i = 0; i< organizationList.size(); i++){
                if(organizationList.get(i).getName().toLowerCase().contains(userInput))
                    newList.add(organizationList.get(i));
            }
            adapter=new OrganizationViewAdapter(newList,this.getContext(),this);
            recyclerView.setAdapter(adapter);
        }
        return false;
    }

    //Management of the back button.
    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }

}



