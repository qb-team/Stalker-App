package com.example.stalkerapp.ui.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.stalkerapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import java.net.URL;
import java.util.ArrayList;


public class HomeFragment extends RootFragment {
    public final static String TAG = "Home_FRAGMENT";

    private RequestQueue mQueue;
    String inline=" ";
    JSONObject jsonObject;
    JSONArray jsonArray2;
    private static HomeFragment instance = null;
    ArrayAdapter<String> adapter;
    ArrayList<String> listaOrganizzazioni;
    ListView listaOrg;
    private SwipeRefreshLayout aggiornamento;
    private boolean aggiunto;

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
        listaOrganizzazioni= new ArrayList<>();
        mQueue= Volley.newRequestQueue(getContext());
        aggiornamento=view.findViewById(R.id.swiperefresh);

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
                transaction.replace(R.id.HomeFragment, organizzazione).commit();
            }

        });  //Fine Indirizzamento layout dell'organizzazione scelta
        listaOrg.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    aggiungi(listaOrg.getItemAtPosition(position).toString());

                    return true;
            }
        });

      File organizzazioniFile = new File(getContext().getFilesDir()+"/Organizzazioni.txt");
        if(organizzazioniFile.length()==0 || !organizzazioniFile.exists()){
            Toast.makeText(getActivity(),"Lista organizzazioni ancora vuota, vai a scaricarla!",Toast.LENGTH_SHORT).show();

        }
        else {
            try {
                FileInputStream fin=new FileInputStream(organizzazioniFile);
                byte[] buffer= new byte[(int)organizzazioniFile.length()];
                new DataInputStream(fin).readFully(buffer);
                fin.close();
                String s=new String(buffer,"UTF-8");
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = (JSONArray) jsonObject.get("listaOrganizzazioni");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject organizzazione= jsonArray.getJSONObject(i);
                    String nomeOrganizzazione= organizzazione.getString("nome");
                    listaOrganizzazioni.add(nomeOrganizzazione);

                }
            } catch (JSONException | FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            adapter=new ArrayAdapter<>(getContext(),R.layout.row,R.id.textView2,listaOrganizzazioni);
            listaOrg.setAdapter(adapter);
        }
        aggiornamento.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                        new Organizzazioni().execute();
                    }
        });

        return view;
    }

public class Organizzazioni extends AsyncTask<Void,Void,Void>{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
    @Override
    protected Void doInBackground(Void... params) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

            e.printStackTrace();

        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //Here you can update the view
        try {
            Parse2();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Notify swipeRefreshLayout that the refresh has finished
        aggiornamento.setRefreshing(false);
    }
}
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
                if(listaOrganizzazioni.size()!=0){
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }


    public void Parse2() throws IOException, InterruptedException {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    URL url=new URL( "https://api.jsonbin.io/b/5e873ea993960d63f0782fcf/2");
                    if(url==null) {
                        Toast.makeText(getActivity(), "Errore nello scaricamento", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    OkHttpClient client = new OkHttpClient();
                    final Request req = new Request.Builder().url(url).get().build();
                    final Response resp = client.newCall(req).execute();
                    final int code = resp.code();
                    final ResponseBody body = resp.body();
                    File organizzazioniFile = new File(getContext().getFilesDir()+"/Organizzazioni.txt");
                    String s=null;

                    if(organizzazioniFile.exists()){
                        FileInputStream fin=new FileInputStream(organizzazioniFile);
                    byte[] buffer= new byte[(int)organizzazioniFile.length()];
                    new DataInputStream(fin).readFully(buffer);
                    fin.close();
                    s=new String(buffer,"UTF-8");
                    }
                    inline = body.string();


                    if(code != 200) {
                        Toast.makeText(getActivity(),"Errore nello scaricamento",Toast.LENGTH_SHORT).show();
                        body.close();

                    }
                    if(s!=null && s.equals(inline)){
                            body.close();

                            return;
                    }
                    else
                    {

                        System.out.println("\nJSON data in string format");
                        System.out.println(inline);
                        JSONObject jsonObject = new JSONObject(inline);
                        JSONArray jsonArray = (JSONArray) jsonObject.get("listaOrganizzazioni");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject organizzazione= jsonArray.getJSONObject(i);
                            String nomeOrganizzazione= organizzazione.getString("nome");
                            System.out.println(nomeOrganizzazione);
                            FileWriter w;
                            w=new FileWriter(getContext().getFilesDir()+"/Organizzazioni.txt");
                            w.write(inline);
                            w.flush();
                            w.close();
                            BufferedReader fileReader = new BufferedReader(new FileReader(getContext().getFilesDir() + "/Organizzazioni.txt"));
                            String st;
                            System.out.println(getContext().getFilesDir());
                            while ((st = fileReader.readLine()) != null)
                                System.out.println(st);
                            listaOrganizzazioni.add(nomeOrganizzazione);

                            body.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.join();
        StampaAschermo();
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


public void StampaAschermo() throws IOException {

    adapter=new ArrayAdapter<>(getContext(),R.layout.row,R.id.textView2,listaOrganizzazioni);
    listaOrg.setAdapter(adapter);
}

}