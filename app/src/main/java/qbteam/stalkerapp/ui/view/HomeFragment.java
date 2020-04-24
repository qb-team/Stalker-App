package qbteam.stalkerapp.ui.view;


import android.app.Dialog;
import android.app.ProgressDialog;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONException;

import qbteam.stalkerapp.MyAdapter;
import qbteam.stalkerapp.Organizzazioni;
import qbteam.stalkerapp.Presenter.ListaOrganizzazioniContract;
import qbteam.stalkerapp.Presenter.ListaOrganizzazioniPresenter;
import qbteam.stalkerapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends RootFragment implements ListaOrganizzazioniContract.View, MyAdapter.OnOrganizzazioneListener, SearchView.OnQueryTextListener {

    private ListaOrganizzazioniPresenter listaOrganizzazioniPresenter;
    private ArrayList<Organizzazioni> listOrganizzazioni;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout aggiornamento;
    Dialog myDialog;
    ////////////////////////////////////
    Button scarico;
    Button dialog_info;
    Button dialog_agg_Preferiti;
    ProgressDialog mProgressDialog;
    ///////////////////////////

    //---------------------------


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
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        listaOrganizzazioniPresenter=new ListaOrganizzazioniPresenter(this);
        listOrganizzazioni=new ArrayList<>();

        //////////// LISTENER  ///////////////

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



        //////////// FINE LISTENER  ///////////////
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
            adapter=new MyAdapter(listOrganizzazioni,this.getContext(),this);
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


        if(menu.findItem(R.id.preferitiID)!=null){
            menu.findItem(R.id.preferitiID).setVisible(false);
        }

        super.onPrepareOptionsMenu(menu);

    }

    //Quando viene invocato?
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.cerca_organizzazione, menu);
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
                adapter=new MyAdapter(listOrganizzazioni,this.getContext(),this);
                recyclerView.setAdapter(adapter);
                listaOrganizzazioniPresenter.updateFile(listOrganizzazioni,this,"/Organizzazioni.txt");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
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


}