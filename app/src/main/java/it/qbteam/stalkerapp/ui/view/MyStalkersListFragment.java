package it.qbteam.stalkerapp.ui.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import it.qbteam.stalkerapp.MainActivity;
import it.qbteam.stalkerapp.model.data.Organization;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;
import it.qbteam.stalkerapp.presenter.MyStalkersListContract;
import it.qbteam.stalkerapp.presenter.MyStalkersListPresenter;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.tools.OrganizationViewAdapter;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MyStalkersListFragment extends Fragment implements MyStalkersListContract.View, OrganizationViewAdapter.OrganizationListener, SearchView.OnQueryTextListener, OnBackPressListener {

    private MyStalkersListPresenter myStalkersListPresenter;
    private ArrayList<Organization> OrganizationArrayList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private static MyStalkersListFragment instance = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        instance=this;

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("Creazione HomeFragment");
        View view = inflater.inflate(R.layout.fragment_organizations_list, container, false);
        recyclerView=view.findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        myStalkersListPresenter =new MyStalkersListPresenter(this);
        OrganizationArrayList=new ArrayList<>();
        checkFile();
        return view;
    }

    public static MyStalkersListFragment getInstance() {
        return instance;
    }

    public void checkFile() {
        myStalkersListPresenter.checkFile(this, "/Preferiti.txt");
    }

    //  MyAdapter.OnOrganizzazioneListener DA METTERE ALTRI 2 METODI LDAP E STANDARD COME RISPOSTE
    @Override
    public void organizationClick(int position) {

        Bundle bundle=new Bundle();
        bundle.putString("nomeOrganizzazione",OrganizationArrayList.get(position).getNome());
        Fragment aux=OrganizationArrayList.get(position).getFragment();
        aux.setArguments(bundle);
        FragmentTransaction transaction= getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.HomeFragmentID, aux).commit();

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

        myStalkersListPresenter.remove(OrganizationArrayList.get(position).getNome(),OrganizationArrayList);

    }

    public void addOrganization(String nameOrg) throws IOException, JSONException {

        myStalkersListPresenter.findOrganization(new Organization(nameOrg), OrganizationArrayList);
    }

    public void updateFileLocale(ArrayList<Organization> list) throws IOException, JSONException {
        myStalkersListPresenter.updateFile(list,this,"/Preferiti.txt");
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){


        MenuItem preferiti= menu.findItem(R.id.preferitiID);
        if (preferiti!=null)
            menu.findItem(R.id.preferitiID).setVisible(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.cerca_organizzazione, menu);
        MenuItem item= menu.findItem(R.id.cercaID);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
    }
    public void alphabeticalOrder(){
        Collections.sort(OrganizationArrayList);
        try {
            adapter=new OrganizationViewAdapter(OrganizationArrayList,this.getContext(),this);
            recyclerView.setAdapter(adapter);
            myStalkersListPresenter.updateFile(OrganizationArrayList,this,"/Organizzazioni.txt");
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
        if(OrganizationArrayList.size()!=0){
            for(int i=0;i<OrganizationArrayList.size();i++){
                if(OrganizationArrayList.get(i).getNome().toLowerCase().contains(userInput))
                    newList.add(OrganizationArrayList.get(i));
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


    @Override
    public void onSuccessCheckFile(ArrayList<Organization> list) {
        OrganizationArrayList=list;
        adapter=new OrganizationViewAdapter(OrganizationArrayList,this.getContext(),this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFailureCheckFile(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessSearchOrganization(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailureSearchOrganization(Organization organization) throws IOException, JSONException {
        myStalkersListPresenter.addOrganization(organization, OrganizationArrayList);
    }

    @Override
    public void onSuccessAddOrganization(String message) throws IOException, JSONException {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        adapter=new OrganizationViewAdapter(OrganizationArrayList,this.getContext(),this);
        recyclerView.setAdapter(adapter);
        updateFileLocale(OrganizationArrayList);
    }

    @Override
    public void onSuccessRemoveOrganization(ArrayList<Organization> list) throws IOException, JSONException {
        adapter=new OrganizationViewAdapter(list,this.getContext(),this);
        recyclerView.setAdapter(adapter);
        updateFileLocale(OrganizationArrayList);
    }


}