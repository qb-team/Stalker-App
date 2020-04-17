package com.example.stalkerapp.ui.main;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.stalkerapp.MyAdapter;
import com.example.stalkerapp.Organizzazioni;
import com.example.stalkerapp.Presenter.ListaOrganizzazioniContract;
import com.example.stalkerapp.Presenter.ListaOrganizzazioniPresenter;
import com.example.stalkerapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends RootFragment implements ListaOrganizzazioniContract.View, MyAdapter.OnOrganizzazioneListener, SearchView.OnQueryTextListener {

    private ListaOrganizzazioniPresenter listaOrganizzazioniPresenter;
    private ArrayList<Organizzazioni> listOrganizzazioni;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout aggiornamento;

    ////////////////////////////////////
    Button scarico;
    ProgressDialog mProgressDialog;
    ///////////////////////////

    //---------------------------
    public void HomeFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("Creazione HomeFragment");
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ////////////////////////////////////////////////////////
        scarico = view.findViewById(R.id.scaricoID);
        aggiornamento=view.findViewById(R.id.swiperefresh);
        recyclerView=view.findViewById(R.id.recyclerView);
        ////////////////////////////////////////////////////////////
        scarico.setVisibility(View.INVISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        listaOrganizzazioniPresenter=new ListaOrganizzazioniPresenter(this);
        listOrganizzazioni=new ArrayList<>();

        //////////// LISTENER  ///////////////

        aggiornamento.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    aggiornaLista();
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
                    aggiornaLista();
                    scarico.setVisibility(View.INVISIBLE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });




        //////////// FINE LISTENER  ///////////////

        try {
            controllaFile();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return view;
    }


    public void aggiornaLista() throws InterruptedException {
        listaOrganizzazioniPresenter.aggiorna(this,listOrganizzazioni);
        controllaFile();
    }


    public void controllaFile() throws InterruptedException {
        if(listaOrganizzazioniPresenter.controlla(this)!=null){
            System.out.println("non è vuota");
            listOrganizzazioni=listaOrganizzazioniPresenter.controlla(this);
            adapter=new MyAdapter(listOrganizzazioni,this.getContext(),this);
            recyclerView.setAdapter(adapter);
        }
        else{
            scarico.setVisibility(View.VISIBLE);
            System.out.println("è vuota");
//            mProgressDialog = new ProgressDialog(getContext());
//            mProgressDialog.setMessage("Sto scaricando la lista delle organizzazioni");
//            aggiornaLista();
//            mProgressDialog.dismiss();
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


    //Quando viene invocato?
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.cerca_organizzazione, menu);
        MenuItem item= menu.findItem(R.id.cercaID);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
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
        ArrayList<Organizzazioni> newList= new ArrayList<>();
        if(listOrganizzazioni.size()!=0){
            for(int i=0;i<listOrganizzazioni.size();i++){
                if(listOrganizzazioni.get(i).getNome().toLowerCase().contains(userInput))
                    newList.add(listOrganizzazioni.get(i));
            }
            adapter=new MyAdapter(newList,this.getContext(),this);
            recyclerView.setAdapter(adapter);

        }
        return false;
    }

/*    // Ordinamento Alfabetico non completamente funzionante
    public void OrdinamentoAlfabetico(){
        Collections.sort(listOrganizzazioni, new Comparator<Organizzazioni>() {
            @Override
            public int compare(Organizzazioni lhs, Organizzazioni rhs) {
                return lhs.getNome().compareTo(rhs.getNome());
            }
        });
        recyclerView.getAdapter().notifyDataSetChanged();
//        adapter=new MyAdapter(listOrganizzazioni,this.getContext(),this);
//        recyclerView.setAdapter(adapter);
        System.out.println("List after the use of" +
                " Collection.sort() :\n" + listOrganizzazioni.get(0).getNome() + listOrganizzazioni.get(1).getNome() + listOrganizzazioni.get(2).getNome());
    }

 */

/*  Aggiungi da implementare
    public void aggiungi(final String s){

        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getContext())
                // set message, title, and icon
                .setTitle("Aggiungi organizzazione")
                .setMessage("Vuoi aggiungere questa organizzazione alla tua lista dei preferiti?")
                .setPositiveButton("Aggiungi", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        try {
                            aggiunto=ListaPreferiti.getInstance().costruisciJSONobject(s);
                            if(aggiunto)
                                Toast.makeText(getActivity(),"Aggiunta organizzazione ai preferiti",Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getActivity(),"Hai già aggiutno questa organizzazione ai preferiti",Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
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
*/
}