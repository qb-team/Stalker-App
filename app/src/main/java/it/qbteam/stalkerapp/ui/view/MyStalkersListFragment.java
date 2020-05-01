package it.qbteam.stalkerapp.ui.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import it.qbteam.stalkerapp.model.backend.model.Organization;
import it.qbteam.stalkerapp.model.data.User;
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
    private ArrayList<Organization> organizationList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private String path;
    private static MyStalkersListFragment instance = null;
    private  User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        path= getContext().getFilesDir() + "/Preferiti.txt";
        instance=this;

        if (FirebaseAuth.getInstance().getCurrentUser() != null ) {
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

            mUser.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                user = new User(task.getResult().getToken());
                                System.out.println("ECCO IL TOKEN:  " + user.getToken());
                                // Send token to your backend via HTTPS
                                // ...
                            } else {
                                // Handle error -> task.getException();
                            }
                        }
                    });
        }

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_preferiti, container, false);
        recyclerView=view.findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        myStalkersListPresenter =new MyStalkersListPresenter(this);
        organizationList =new ArrayList<>();
        organizationList=checkFile();

        if(organizationList!=null){
            adapter=new OrganizationViewAdapter(organizationList,this.getContext(),this);
            recyclerView.setAdapter(adapter);
        }
        else
            Toast.makeText(getActivity(),"Lista ancora vuota!",Toast.LENGTH_SHORT).show();

        return view;
    }

    public static MyStalkersListFragment getInstance() {
        return instance;
    }

    public ArrayList<Organization> checkFile() {

       return myStalkersListPresenter.checkFile(path);
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

        myStalkersListPresenter.removeRest(organizationList.get(position), user);
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

    public void addOrganizationRest(Organization organization, User user) throws IOException, JSONException {
        myStalkersListPresenter.addOrganizationRest(organization, user);
    }

}