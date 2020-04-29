package it.qbteam.stalkerapp.ui.view;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import it.qbteam.stalkerapp.MainActivity;
import it.qbteam.stalkerapp.model.data.Organization;
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
    private User user;
    public final static String TAG="Home_Fragment";
    Dialog myDialog;
    Button downloadButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

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
    public static HomeFragment getInstance() {
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("Creazione HomeFragment");
        View view = inflater.inflate(R.layout.fragment_organizations_list, container, false);
        downloadButton = view.findViewById(R.id.scaricoID);
        refresh=view.findViewById(R.id.swiperefreshID);
        recyclerView=view.findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        OrganizationListPresenter=new HomePresenter(this);
        organizationList=new ArrayList<>();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    downloadList();
                    refresh.setRefreshing(false);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    downloadList();
                    downloadButton.setVisibility(View.INVISIBLE);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        });


        checkFile();



        return view;
    }

    //Avvia lo scaricamento della lista
    public void downloadList() throws InterruptedException, IOException {
        OrganizationListPresenter.downloadFile(this,organizationList, user);

        checkFile();
    }


    public void checkFile()  {
        OrganizationListPresenter.checkFile(this, "/Organizzazioni.txt");
    }

     public void onSuccessCheckFile(ArrayList<Organization> list){
         organizationList=list;
         adapter=new OrganizationViewAdapter(organizationList,this.getContext(),this);
         recyclerView.setAdapter(adapter);
     }

    @Override
    public void onFailureCheckFile(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        downloadButton.setVisibility(View.VISIBLE);
    }


    //  MyAdapter.OnOrganizzazioneListener
    @Override
    public void organizationClick(int position) {
        Bundle bundle=new Bundle();
        bundle.putString("nomeOrganizzazione",organizationList.get(position).getNome());
        Fragment aux=organizationList.get(position).getFragment();
        aux.setArguments(bundle);
        FragmentTransaction transaction= getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.HomeFragmentID, aux).commit();
        }

        @Override
    public void organizationLongClick(int position) {
        myDialog=new Dialog(getContext());
        myDialog.setContentView(R.layout.dialog_organizzazione);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView dialog_nomeOrganizzazione=myDialog.findViewById(R.id.dialog_nomeOrganizzazione);
        TextView dialog_tracciamento=myDialog.findViewById(R.id.dialog_tracciamento);
        dialog_nomeOrganizzazione.setText(organizationList.get(position).getNome());
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
                    MyStalkersListFragment.getInstance().addOrganization(organizationList.get(position).getNome());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                myDialog.dismiss();
            }
        });

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu){

        MenuItem preferiti= menu.findItem(R.id.preferitiID);
        if (preferiti!=null)
           menu.findItem(R.id.preferitiID).setVisible(false);
    }

    //Quando viene invocato?
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.cerca_organizzazione, menu);

        MenuItem item= menu.findItem(R.id.cercaID);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);

    }




    public void alphabeticalOrder(){
        Collections.sort(organizationList);
        try {
            adapter=new OrganizationViewAdapter(organizationList,this.getContext(),this);
            recyclerView.setAdapter(adapter);
            OrganizationListPresenter.updateFile(organizationList,this,"/Organizzazioni.txt");
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
            for(int i=0;i<organizationList.size();i++){
                if(organizationList.get(i).getNome().toLowerCase().contains(userInput))
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