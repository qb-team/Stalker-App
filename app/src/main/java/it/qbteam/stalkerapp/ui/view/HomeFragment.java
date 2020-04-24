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
import it.qbteam.stalkerapp.presenter.OrganizationsListContract;
import it.qbteam.stalkerapp.presenter.OrganizationsListPresenter;
import it.qbteam.stalkerapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment implements OrganizationsListContract.View, OrganizationViewAdapter.OnOrganizzazioneListener, SearchView.OnQueryTextListener, OnBackPressListener {

    private OrganizationsListPresenter listaOrganizzazioniPresenter;
    private ArrayList<Organization> listOrganizzazioni;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout aggiornamento;
    Dialog myDialog;
    Button scarico;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        listaOrganizzazioniPresenter=new OrganizationsListPresenter(this);
        listOrganizzazioni=new ArrayList<>();

        aggiornamento.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    scaricaLista();
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
                    scaricaLista();
                    scarico.setVisibility(View.INVISIBLE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        try {
            controllaFile();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return view;
    }

    //Avvia lo scaricamento della lista
    public void scaricaLista() throws InterruptedException {
        listaOrganizzazioniPresenter.scarica(this,listOrganizzazioni);
        controllaFile();
    }


    public void controllaFile() throws InterruptedException {
        if(listaOrganizzazioniPresenter.controlla(this, "/Organizzazioni.txt")!=null){
            System.out.println("non è vuota");
            listOrganizzazioni=listaOrganizzazioniPresenter.controlla(this,"/Organizzazioni.txt" );
            adapter=new OrganizationViewAdapter(listOrganizzazioni,this.getContext(),this);
            recyclerView.setAdapter(adapter);
        }
        else{
            scarico.setVisibility(View.VISIBLE);
            System.out.println("è vuota");
        }
    }

    // ListaOrganizzazioniContract.View
    @Override
    public void onLoadListFailure(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }



    //  MyAdapter.OnOrganizzazioneListener
    @Override
    public void organizzazioneClick(int position) {
        Organizzazione organizzazione=new Organizzazione();

        Bundle bundle=new Bundle();
        bundle.putString("nomeOrganizzazione",listOrganizzazioni.get(position).getNome());
        organizzazione.setArguments(bundle);
        FragmentTransaction transaction =getChildFragmentManager().beginTransaction();
        // Store the Fragment in stack
        transaction.addToBackStack(null);
        transaction.replace(R.id.HomeFragmentID, organizzazione).commit();
    }

    @Override
    public void organizzazioneLongClick(int position) {
        myDialog=new Dialog(getContext());
        myDialog.setContentView(R.layout.dialog_organizzazione);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView dialog_nomeOrganizzazione=myDialog.findViewById(R.id.dialog_nomeOrganizzazione);
        TextView dialog_tracciamento=myDialog.findViewById(R.id.dialog_tracciamento);
        ImageView dialog_immage=myDialog.findViewById(R.id.dialog_immage);
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
                    ListaPreferiti.getInstance().aggiungiOrganizzazione(listOrganizzazioni.get(position).getNome());
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if(id==R.id.ordina){
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
        if(id==R.id.logoutID){
            FirebaseAuth.getInstance().signOut();   //logout

            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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