package qbteam.stalkerapp.ui.view;

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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import qbteam.stalkerapp.MainActivity;
import qbteam.stalkerapp.tools.BackPressImplementation;
import qbteam.stalkerapp.tools.OnBackPressListener;
import qbteam.stalkerapp.model.data.Organization;
import qbteam.stalkerapp.presenter.ListaPreferitiContract;
import qbteam.stalkerapp.presenter.ListaPreferitiPresenter;
import qbteam.stalkerapp.R;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ListaPreferiti extends Fragment implements ListaPreferitiContract.View, OrganizationViewAdapter.OnOrganizzazioneListener, SearchView.OnQueryTextListener, OnBackPressListener {


    private ListaPreferitiPresenter listaPreferitiPresenter;
    private ArrayList<Organization> listOrganizzazioni;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Organization> listaAggiornata;
    private static ListaPreferiti instance = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("Creazione HomeFragment");
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        listaPreferitiPresenter=new ListaPreferitiPresenter(this);
        listOrganizzazioni=new ArrayList<>();

        controllaFile();



        return view;
    }
    public static ListaPreferiti getInstance() {
        return instance;
    }

    public void controllaFile() {
        if(listaPreferitiPresenter.controlla(this, "/Preferiti.txt")!=null){
            System.out.println("non è vuota");
            listOrganizzazioni=listaPreferitiPresenter.controlla(this,"/Preferiti.txt");
            adapter=new OrganizationViewAdapter(listOrganizzazioni,this.getContext(),this);
            recyclerView.setAdapter(adapter);
        }

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
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getContext())
                // set message, title, and icon
                .setTitle("Elimina organizzazione")
                .setMessage("Sei sicuro di voler eliminare l'organizzazione?")
                .setPositiveButton("Elimina", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        try {
                            elimina(position);
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
    public void elimina(int position) throws IOException, JSONException {

        listaPreferitiPresenter.rimuovi(listOrganizzazioni.get(position).getNome(),listOrganizzazioni);
        adapter=new OrganizationViewAdapter(listOrganizzazioni,this.getContext(),this);
        recyclerView.setAdapter(adapter);
        aggiornaFileLocale(listOrganizzazioni);

    }
    public void aggiungiOrganizzazione(String nameOrg) throws IOException, JSONException {
        Organization aux=new Organization(nameOrg);
        listOrganizzazioni.add(aux);
        adapter=new OrganizationViewAdapter(listOrganizzazioni,this.getContext(),this);
        recyclerView.setAdapter(adapter);
        aggiornaFileLocale(listOrganizzazioni);

    }

    public void aggiornaFileLocale(ArrayList<Organization> list) throws IOException, JSONException {
        listaPreferitiPresenter.updateFile(list,this,"/Preferiti.txt");
    }

    @Override
        public void onPrepareOptionsMenu(Menu menu){


            if(menu.findItem(R.id.preferitiID)!=null)
                menu.findItem(R.id.preferitiID).setVisible(false);


            super.onPrepareOptionsMenu(menu);

        }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.cerca_organizzazione, menu);
        MenuItem item= menu.findItem(R.id.cercaID);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.ordina:
                Collections.sort(listOrganizzazioni);
                try {
                    adapter=new OrganizationViewAdapter(listOrganizzazioni,this.getContext(),this);
                    recyclerView.setAdapter(adapter);
                    listaPreferitiPresenter.updateFile(listOrganizzazioni,this,"/Preferiti.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;

            case R.id.preferitiID:
                return false;
            case R.id.logoutID:
                FirebaseAuth.getInstance().signOut();   //logout
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;

            default:
                break;
        }
        return false;
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
    public void onLoadListFailure(String message) {

    }

    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }


    /*
    private ArrayAdapter<String> adapter;

    private ListView listaOrg;
    private JSONObject jo,mainObj;
    private JSONArray ja;
    private ArrayList<String> preferiti;
    private ListaPreferitiPresenter preferitiPresenter;
    private static ListaPreferiti instance = null;
    public final static String TAG="Preferiti_FRAGMENT";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        instance = this;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("Creazione Listapreferiti");
       View view= inflater.inflate(R.layout.fragment_lista_preferiti, container, false);

        listaOrg = view.findViewById(R.id.ListaOrg);
        listaOrg.setLongClickable(true);
        preferiti=new ArrayList<>();
        preferitiPresenter=new ListaPreferitiPresenter();
        controllaFile();
        listaOrg.setOnItemClickListener(new AdapterView.OnItemClickListener() {    // Inizio Indirizzamento layout dell'organizzazione scelta
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                   Organizzazione organizzazione=new Organizzazione();
                   Bundle bundle=new Bundle();
                   bundle.putString("nomeOrganizzazione",listaOrg.getItemAtPosition(position).toString());
                   organizzazione.setArguments(bundle);
                   FragmentTransaction transaction =getChildFragmentManager().beginTransaction();
                   // Store the Fragment in stack
                   transaction.addToBackStack(null);
                   transaction.replace(R.id.ListaPreferiti, organizzazione).commit();
            }
        });

        listaOrg.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                    elimina(listaOrg.getItemAtPosition(position).toString());
                    return true;
            }
        });
        return view;

    }


    public static ListaPreferiti getInstance() {
        return instance;
    }



   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.cerca_organizzazione, menu);
        MenuItem item= menu.findItem(R.id.cercaID);

        SearchView searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(preferiti.size()!=0){
                    adapter.getFilter().filter(newText);
                }
                return false;

            }
        });
        //super.onCreateOptionsMenu(menu,inflater);
    }
    public boolean costruisciJSONobject(String s) throws JSONException, IOException {
        //AGGIUNGO OGNI VOLTA UN'ORGANIZZAZIONE ALLA LISTA(INCREMENTALE)
        for(int i = 0; i<preferiti.size(); i++)
        {
            if(preferiti.get(i).equals(s))
                return false;
        }
        preferiti.add(s);
        aggiungiPreferiti();
        System.out.println(preferiti + " " + preferiti.size());
        //CONVERTO LA LISTA DINAMICA IN UN NUOVO ARRAY
        String[] array = new String[preferiti.size()];
        array = preferiti.toArray(array);
        System.out.println(array.length);
        //COSTRUISCO JSONOBJECT
        ja=new JSONArray();
        for(int i=0;i<array.length;i++){
            jo=new JSONObject();
            jo.put("nome", array[i]);
            System.out.println(array[i]);
            ja.put(jo);
        }
        mainObj=new JSONObject();
        mainObj.put("listaOrganizzazioni", ja);
        System.out.println("JSON OBJECT COSTRUITO "+" "+ mainObj.toString()+"  ");
        costruisciFile(mainObj);
        return true;

    }
    public void costruisciFile(JSONObject jo) throws  IOException {
        String s="";
        s=jo.toString();
        FileWriter w;
        w=new FileWriter(getContext().getFilesDir()+"/Preferiti.txt");
        w.write(s);
        w.flush();
        w.close();
        BufferedReader fileReader = new BufferedReader(new FileReader(getContext().getFilesDir() + "/Preferiti.txt"));
        String st;
        System.out.println(getContext().getFilesDir());
        while ((st = fileReader.readLine()) != null)
            System.out.println(st);
    }
    public void aggiungiPreferiti(){
        adapter=new ArrayAdapter<>(getContext(),R.layout.row,R.id.textView2,preferiti);
        listaOrg.setAdapter(adapter);

    }

    private void elimina(final String nomeOrg){

        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getContext())
                // set message, title, and icon
                .setTitle("Elimina organizzazione")
                .setMessage("Sei sicuro di voler eliminare l'organizzazione?")
                .setPositiveButton("Elimina", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            eliminaDaArrayList(nomeOrg);
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
     public void eliminaDaArrayList(String s) throws IOException, JSONException {
        preferiti.remove(s);
        aggiungiPreferiti();
        aggiornaFile();


}
    public void aggiornaFile() throws JSONException, IOException {
    String[] array = new String[preferiti.size()];
    array = preferiti.toArray(array);

    //COSTRUISCO JSONOBJECT
    ja=new JSONArray();
    for(int i=0;i<array.length;i++){
        jo=new JSONObject();
        jo.put("nome", array[i]);
        System.out.println(array[i]);
        ja.put(jo);
    }
    mainObj=new JSONObject();
    mainObj.put("listaOrganizzazioni", ja);
    System.out.println("JSON OBJECT COSTRUITO "+" "+ mainObj.toString()+"  ");
    costruisciFile(mainObj);
}

     public void controllaFile(){
     if(preferitiPresenter.controlla(this)!=null){
         preferiti=preferitiPresenter.controlla(this);
         adapter=new ArrayAdapter<>(getContext(),R.layout.row,R.id.textView2,preferiti);
         listaOrg.setAdapter(adapter);
     }
     else Toast.makeText(getActivity(),"Lista preferiti ancora vuota",Toast.LENGTH_SHORT).show();
}

*/

}