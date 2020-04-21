package qbteam.stalkerapp.ui.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import qbteam.stalkerapp.Presenter.ListaPreferitiPresenter;
import qbteam.stalkerapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ListaPreferiti extends RootFragment {
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
    //nascondo il menu della organizzazione
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.preferitiID);
        if(menuItem!=null)
        menuItem.setVisible(false);
    }
//creo il menu
   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

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
        super.onCreateOptionsMenu(menu,inflater);
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




}