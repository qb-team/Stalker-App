package com.example.stalkerapp.ui.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.stalkerapp.MyAdapter;
import com.example.stalkerapp.Organizzazioni;
import com.example.stalkerapp.Presenter.ListaOrganizzazioniContract;
import com.example.stalkerapp.Presenter.ListaOrganizzazioniPresenter;
import com.example.stalkerapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class HomeFragment extends RootFragment implements ListaOrganizzazioniContract.View, MyAdapter.OnOrganizzazioneListener, SearchView.OnQueryTextListener{
    public final static String TAG = "Home_FRAGMENT";

    JSONObject jsonObject;
    JSONArray jsonArray2;
    private static HomeFragment instance = null;
    ListView listaOrg;
    private SwipeRefreshLayout aggiornamento;
    private boolean aggiunto;
    private ListaOrganizzazioniPresenter listaOrganizzazioniPresenter;
    //NUOVA VERSIONE DI ADAPTER
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private MyAdapter myAdapter;
    private ArrayList<Organizzazioni> listOrganizzazioni;
    //---------------------------
    public void HomeFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        instance = this;



    }

    public static HomeFragment getInstance() {
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("Creazione HomeFragment");
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        jsonObject = new JSONObject();
        jsonArray2 = new JSONArray();
        listaOrg = view.findViewById(R.id.ListaOrg);
        aggiornamento=view.findViewById(R.id.swiperefresh);
        listaOrganizzazioniPresenter=new ListaOrganizzazioniPresenter(this);
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        listOrganizzazioni=new ArrayList<>();
        adapter=new MyAdapter(listOrganizzazioni,this.getContext(),this);
        recyclerView.setAdapter(adapter);

        //CONTROLLO PER VEDERE SE ESISTE GIA' UN FILE CONTENENTE LA LISTA DELLE ORGANIZZAZIONI
        controllaFile();
        /*
        listaOrg.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    aggiungi(listaOrg.getItemAtPosition(position).toString());

                    return true;
            }
        });

*/
        return view;
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState){
        super.onViewCreated(rootView, savedInstanceState);

        aggiornamento.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                try {
                    listOrganizzazioni.clear();
                    aggiornaLista();
                    aggiornamento.setRefreshing(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.cerca_organizzazione, menu);
        MenuItem item= menu.findItem(R.id.cercaID);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }

    public void controllaFile(){
    if(listaOrganizzazioniPresenter.controlla(this)!=null){
        listOrganizzazioni=listaOrganizzazioniPresenter.controlla(this);
        adapter=new MyAdapter(listOrganizzazioni,this.getContext(),this);
        recyclerView.setAdapter(adapter);

    }
}
    @Override
    public void onLoadListFailure(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    public void aggiornaLista() throws InterruptedException {
        if(listaOrganizzazioniPresenter.aggiorna(this,listOrganizzazioni)!=null)
            listOrganizzazioni=listaOrganizzazioniPresenter.aggiorna(this,listOrganizzazioni);
            adapter=new MyAdapter(listOrganizzazioni,this.getContext(),this);
            recyclerView.setAdapter(adapter);
    }
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

    @Override
    public void organizzazioneClick(int position) {
        Organizzazione organizzazione=new Organizzazione();

        Bundle bundle=new Bundle();
        bundle.putString("nomeOrganizzazione",listOrganizzazioni.get(position).getNome());
        organizzazione.setArguments(bundle);
        FragmentTransaction transaction =getChildFragmentManager().beginTransaction();
        // Store the Fragment in stack
        transaction.addToBackStack(null);
        transaction.replace(R.id.HomeFragment, organizzazione).commit();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

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
}