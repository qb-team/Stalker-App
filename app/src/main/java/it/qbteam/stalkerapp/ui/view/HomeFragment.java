package it.qbteam.stalkerapp.ui.view;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;

import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.model.backend.model.Organization;
import it.qbteam.stalkerapp.model.data.User;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;
import it.qbteam.stalkerapp.presenter.HomeContract;
import it.qbteam.stalkerapp.presenter.HomePresenter;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.tools.OrganizationViewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment implements HomeContract.View, OrganizationViewAdapter.OrganizationListener, SearchView.OnQueryTextListener, OnBackPressListener {

    private HomePresenter OrganizationListPresenter;
    private ArrayList<Organization> organizationList;
    private RecyclerView recyclerView;
    private static HomeFragment instance = null;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout refresh;
    private String path;
    public final static String TAG="Home_Fragment";
    Dialog myDialog;
    Button downloadButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        instance=this;
        path= getContext().getFilesDir() + "/Organizzazioni.txt";
    }

    public static HomeFragment getInstance() {
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizations_list, container, false);
        downloadButton = view.findViewById(R.id.scaricoID);
        refresh=view.findViewById(R.id.swiperefreshID);
        recyclerView=view.findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        OrganizationListPresenter=new HomePresenter(this);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadList();
                refresh.setRefreshing(false);
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadList();
                downloadButton.setVisibility(View.INVISIBLE);
            }
        });

        checkFile();

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



    //SCARICA LA LISTA DAL SERVER E LA SALVA IN FILE LOCALE
    public void downloadList() {
        OrganizationListPresenter.downloadFile(path, HomePageActivity.getInstance().getUser());
    }

    //PROVA A LEGGERE LA LISTA DELLE ORGANIZZAZIONI DA FILE INTERNO E NEL CASO LA TORNA E STAMPA A SCHERMO
    public void checkFile()  {
        System.out.println("CheckFile effettuato");
        organizationList=OrganizationListPresenter.checkFile(path);
        if(organizationList!=null){
            adapter=new OrganizationViewAdapter(organizationList,this.getContext(),this);
            recyclerView.setAdapter(adapter);
        }
        else
            Toast.makeText(getActivity(),"Devi ancora scaricare la lista",Toast.LENGTH_SHORT).show();

    }

    //Risposta positiva al download della lista delle organizzazioni dal server
    @Override
    public void onSuccessDownloadFile(String message) {
        checkFile();
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    //Risposta negativa al download della lista delle organizzazioni dal server
    @Override
    public void onFailureDownloadFile(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        downloadButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailureCheckFile(String message) {
        AlertDialog download = new AlertDialog.Builder(getContext())
                .setTitle("Scarica lista delle organizzazioni")
                .setMessage("La tua lista delle organizzazioni è ancora vuota, vuoi scaricarla?")
                .setPositiveButton("Scarica", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadList();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        download.show();
    }


    //  MyAdapter.OnOrganizzazioneListener
    @Override
    public void organizationClick(int position) {
            Bundle bundle=new Bundle();
            bundle.putString("name", organizationList.get(position).getName());
            bundle.putString("description", organizationList.get(position).getDescription());
            System.out.println("DESCRIPTION"+organizationList.get(position).getDescription());
            bundle.putString("image", organizationList.get(position).getImage());
        System.out.println("IMAGE:"+organizationList.get(position).getImage());

        if(organizationList.get(position).getTrackingMode().getValue()=="anonymous"){
            StandardOrganizationFragment stdOrgFragment= new StandardOrganizationFragment();
            stdOrgFragment.setArguments(bundle);
            FragmentTransaction transaction= getChildFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.HomeFragmentID, stdOrgFragment).commit();
        }
        else{
            LDAPorganizationFragment LDAPFragment= new LDAPorganizationFragment();
            LDAPFragment.setArguments(bundle);
            FragmentTransaction transaction= getChildFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.HomeFragmentID, LDAPFragment).commit();
        }

    }

        @Override
    public void organizationLongClick(int position) {
        myDialog=new Dialog(getContext());
        myDialog.setContentView(R.layout.dialog_organizzazione);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView dialog_nomeOrganizzazione=myDialog.findViewById(R.id.dialog_nomeOrganizzazione);
        ImageView image=myDialog.findViewById(R.id.imageID);
        //image.set(organizationList.get(position).getImage());
        TextView dialog_tracciamento=myDialog.findViewById(R.id.dialog_tracciamento);
        dialog_nomeOrganizzazione.setText(organizationList.get(position).getName());
        myDialog.show();
        Button moreInfo=myDialog.findViewById(R.id.Button_moreInfo);
        Button aggPref=myDialog.findViewById(R.id.Button_aggiungiPreferiti);
        moreInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                organizationClick(position);
                myDialog.dismiss();
            }
        });
        aggPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    MyStalkersListFragment.getInstance().addOrganization(organizationList.get(position));
                    MyStalkersListFragment.getInstance().addOrganizationRest(organizationList.get(position), HomePageActivity.getInstance().getUser());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                myDialog.dismiss();
            }
        });

    }



    public void alphabeticalOrder(){

        Collections.sort(organizationList);
        try {
            adapter=new OrganizationViewAdapter(organizationList,this.getContext(),this);
            recyclerView.setAdapter(adapter);
            OrganizationListPresenter.updateFile(organizationList,path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // SearchView.OnQueryTextListener
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    // SearchView.OnQueryTextListener
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
}



