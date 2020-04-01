package com.example.stalkerapp.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.util.SparseBooleanArray;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ListaPreferiti extends Fragment {
    ArrayAdapter<String> adapter;
    ListView listaOrg;
    JSONObject jo,mainObj;
    JSONArray ja;
    ArrayList<String> preferiti;
    private static ListaPreferiti instance = null;

    public final static String TAG="Preferiti_FRAGMENT";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fragment_lista_preferiti, container, false);
        setHasOptionsMenu(true);
        listaOrg = view.findViewById(R.id.ListaOrg);
        listaOrg.setLongClickable(true);
        preferiti=new ArrayList<>();

       //CONTROLLO ESISTENZA DEL FILE
        File organizzazioniFile = new File(getContext().getFilesDir()+"/Preferiti.txt");
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
                    preferiti.add(nomeOrganizzazione);

                }
            } catch (JSONException | FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            adapter=new ArrayAdapter<>(getContext(),R.layout.row,R.id.textView2,preferiti);
            listaOrg.setAdapter(adapter);
            System.out.println("File is not empty ...");

        }
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


        });

        listaOrg.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                int count = listaOrg.getCount();
                for(int item=count-1;item>=0;item--){

                        adapter.remove(preferiti.get(item));

                }

                adapter.notifyDataSetChanged();
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
                if(preferiti.size()!=0){
                    adapter.getFilter().filter(newText);
                }
                return false;

            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }
    public void costruisciJSONobject(String s) throws JSONException, IOException {
        //AGGIUNGO OGNI VOLTA UN'ORGANIZZAZIONE ALLA LISTA(INCREMENTALE)
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



}