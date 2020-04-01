package com.example.stalkerapp.ui.main;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.stalkerapp.HomePage;
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


public class HomeFragment extends Fragment {
    public final static String TAG = "Home_FRAGMENT";

    private RequestQueue mQueue;
    String inline=" ";
    JSONObject jsonObject;
    JSONArray jsonArray2;
    private static HomeFragment instance = null;
    ArrayAdapter<String> adapter;
    ArrayList<String> listaOrganizzazioni;
    ListView listaOrg;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        setHasOptionsMenu(true);
        jsonObject = new JSONObject();
        jsonArray2 = new JSONArray();
        listaOrg = view.findViewById(R.id.ListaOrg);
        listaOrganizzazioni= new ArrayList<>();
        mQueue= Volley.newRequestQueue(getContext());



        listaOrg.setOnItemClickListener(new AdapterView.OnItemClickListener() {    // Inizio Indirizzamento layout dell'organizzazione scelta
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


               if(HomePage.fragmentManager.findFragmentByTag("Organizzazione_FRAGMENT")==null)

               {
                        Bundle bundle=new Bundle();
                        Fragment organizzazione=new Organizzazione();
                        bundle.putString("nomeOrganizzazione",listaOrg.getItemAtPosition(position).toString());
                        organizzazione.setArguments(bundle);
                        HomePage.fragmentManager.beginTransaction().add(R.id.container,organizzazione,"Organizzazione_FRAGMENT").addToBackStack( "1" ).commit();
                }
               else {
                   HomePage.fragmentManager.beginTransaction().show(HomePage.fragmentManager.findFragmentByTag("Organizzazione_FRAGMENT")).commit();
               }




                }

        });  //Fine Indirizzamento layout dell'organizzazione scelta

      File organizzazioniFile = new File(getContext().getFilesDir()+"/Organizzazioni.txt");
        if(organizzazioniFile.length()==0 || !organizzazioniFile.exists()){
            System.out.println("File is empty ...");
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
            System.out.println("File is not empty ...");

        }

        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.cerca_organizzazione, menu);
        MenuItem item= menu.findItem(R.id.cercaID);
        if(HomePage.fragmentManager.findFragmentByTag("Organizzazione_FRAGMENT")!=null)
        {
            item.setVisible(false);
            return;
        }
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


    public void Parse2() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    URL url=new URL( "https://api.myjson.com/bins/skt66");
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
}



public void StampaAschermo() throws IOException {
    Parse2();
    adapter=new ArrayAdapter<>(getContext(),R.layout.row,R.id.textView2,listaOrganizzazioni);
    listaOrg.setAdapter(adapter);
}

}