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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;

import it.qbteam.stalkerapp.MainActivity;
import it.qbteam.stalkerapp.model.data.Organization;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;
import it.qbteam.stalkerapp.presenter.HomeContract;
import it.qbteam.stalkerapp.presenter.HomePresenter;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.tools.OrganizationViewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment implements HomeContract.View, OrganizationViewAdapter.OnOrganizzazioneListener, SearchView.OnQueryTextListener, OnBackPressListener {

    private HomePresenter listaOrganizzazioniPresenter;
    private ArrayList<Organization> listOrganizzazioni;
    private RecyclerView recyclerView;
    private static HomeFragment instance = null;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout aggiornamento;
    public final static String TAG="Home_Fragment";
    Dialog myDialog;
    Button scarico;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;
        setHasOptionsMenu(true);
    }
    public static HomeFragment getInstance() {
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("Creazione HomeFragment");
        View view = inflater.inflate(R.layout.fragment_organizations_list, container, false);

        scarico = view.findViewById(R.id.scaricoID);
        aggiornamento=view.findViewById(R.id.swiperefresh);
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        listaOrganizzazioniPresenter=new HomePresenter(this);
        listOrganizzazioni=new ArrayList<>();

        aggiornamento.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    downloadList();
                    aggiornamento.setRefreshing(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        scarico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    downloadList();
                    scarico.setVisibility(View.INVISIBLE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        controllaFile();


        return view;
    }

    //Avvia lo scaricamento della lista
    public void downloadList() throws InterruptedException {
        listaOrganizzazioniPresenter.downloadFile(this,listOrganizzazioni);
        controllaFile();
    }


    public void controllaFile()  {

        listaOrganizzazioniPresenter.checkFile(this, "/Organizzazioni.txt");

    }

     public void onSuccessCheckFile(ArrayList<Organization> list){

         listOrganizzazioni=list;
         adapter=new OrganizationViewAdapter(listOrganizzazioni,this.getContext(),this);
         recyclerView.setAdapter(adapter);
     }

    @Override
    public void onFailureCheckFile(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        scarico.setVisibility(View.VISIBLE);
    }


    //  MyAdapter.OnOrganizzazioneListener
    @Override
    public void organizzazioneClick(int position) {

        Bundle bundle=new Bundle();
        bundle.putString("nomeOrganizzazione",listOrganizzazioni.get(position).getNome());
        Fragment aux=listOrganizzazioni.get(position).getFragment();
        aux.setArguments(bundle);
        FragmentTransaction transaction= getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.HomeFragmentID, aux).commit();

        }

        @Override
    public void organizzazioneLongClick(int position) {
        myDialog=new Dialog(getContext());
        myDialog.setContentView(R.layout.dialog_organizzazione);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView dialog_nomeOrganizzazione=myDialog.findViewById(R.id.dialog_nomeOrganizzazione);
        TextView dialog_tracciamento=myDialog.findViewById(R.id.dialog_tracciamento);
        dialog_nomeOrganizzazione.setText(listOrganizzazioni.get(position).getNome());
        myDialog.show();
        Button moreInfo=myDialog.findViewById(R.id.Button_moreInfo);
        Button aggPref=myDialog.findViewById(R.id.Button_aggiungiPreferiti);
        moreInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                organizzazioneClick(position);
                myDialog.dismiss();
            }
        });
        aggPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean aggiunto= false;
                try {
                    MyStalkersListFragment.getInstance().aggiungiOrganizzazione(listOrganizzazioni.get(position).getNome());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(aggiunto==true)

                    Toast.makeText(getActivity(),"Aggiunta organizzazione ai preferiti",Toast.LENGTH_SHORT).show();
                else

                    Toast.makeText(getActivity(),"Hai già aggiunto questa organizzazione ai preferiti", Toast.LENGTH_SHORT).show();
                myDialog.dismiss();
            }
        });

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu){



    }

    //Quando viene invocato?
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.cerca_organizzazione, menu);
        menu.removeItem(R.id.preferitiID);
        MenuItem item= menu.findItem(R.id.cercaID);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
    }




    public void alphabeticalOrder(){
        Collections.sort(listOrganizzazioni);
        try {
            adapter=new OrganizationViewAdapter(listOrganizzazioni,this.getContext(),this);
            recyclerView.setAdapter(adapter);
            listaOrganizzazioniPresenter.updateFile(listOrganizzazioni,this,"/Organizzazioni.txt");
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
        if(listOrganizzazioni.size()!=0){
            for(int i=0;i<listOrganizzazioni.size();i++){
                if(listOrganizzazioni.get(i).getNome().toLowerCase().contains(userInput))
                    newList.add(listOrganizzazioni.get(i));
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