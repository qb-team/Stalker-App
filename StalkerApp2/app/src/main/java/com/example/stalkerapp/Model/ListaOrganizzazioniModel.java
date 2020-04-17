package com.example.stalkerapp.Model;

import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.stalkerapp.Organizzazioni;
import com.example.stalkerapp.Presenter.ListaOrganizzazioniContract;

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
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ListaOrganizzazioniModel implements ListaOrganizzazioniContract.Intractor {

    private ListaOrganizzazioniContract.ListaOrganizzazioniListener listaOrganizzazioniListener;
    public ListaOrganizzazioniModel( ListaOrganizzazioniContract.ListaOrganizzazioniListener listaOrganizzazioniListener){
        this.listaOrganizzazioniListener=listaOrganizzazioniListener;
    }
    @Override
    public ArrayList<Organizzazioni> performControllaLista(Fragment fragment) {
        //CONTROLLO ESISTENZA DEL FILE
        ArrayList<Organizzazioni> aux = new ArrayList<>();
        File organizzazioniFile = new File(fragment.getContext().getFilesDir()+"/Organizzazioni.txt");
        if(organizzazioniFile.length()==0 || !organizzazioniFile.exists()){
            //listaOrganizzazioniListener.onFailure("Lista organizzazioni ancora vuota, vai a scaricarla!");
            return null;
        }
        else {
            try {
                System.out.println("OrganizzazioniFile:  " + organizzazioniFile);
                FileInputStream fin=new FileInputStream(organizzazioniFile);
                System.out.println("fin:  " + fin);
                byte[] buffer= new byte[(int)organizzazioniFile.length()];
                System.out.println("buffer:  " + buffer);
                new DataInputStream(fin).readFully(buffer);
                fin.close();
                String s=new String(buffer,"UTF-8");
                JSONObject jsonObject = new JSONObject(s);
                System.out.println("jsonObject:  " + jsonObject);
                JSONArray jsonArray = (JSONArray) jsonObject.get("listaOrganizzazioni");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject organizzazione= jsonArray.getJSONObject(i);

                    String nomeOrganizzazione= organizzazione.getString("nome");
                    Organizzazioni organizzazione1=new Organizzazioni(nomeOrganizzazione);
                    System.out.println("organizzazione1:  " + organizzazione1);
                    aux.add(organizzazione1);
                }
            } catch (JSONException | FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return aux;
        }
    }

    @Override
    public void performAggiornaLista(final Fragment fragment, final ArrayList<Organizzazioni> listaAttuale) throws InterruptedException {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() { try {
                URL url = new URL("https://api.jsonbin.io/b/5e873f30dd6c3c63eaed8ed8/7");
                //URL NON VALIDO
                if (url == null) {
                    //Toast.makeText(getActivity(), "Errore nello scaricamento", Toast.LENGTH_SHORT).show();
                    return ;
                }
                OkHttpClient client = new OkHttpClient();
                final Request req = new Request.Builder().url(url).get().build();
                final Response resp = client.newCall(req).execute();
                final int code = resp.code();
                final ResponseBody body = resp.body();
                String inline=" ";
                String s=null;
                inline = body.string();
                System.out.println(inline);
                JSONObject jsonObject = new JSONObject(inline);
                JSONArray jsonArray = (JSONArray) jsonObject.get("listaOrganizzazioni");
                for (int i = 0; i < jsonArray.length(); i++) {
                    FileWriter w;
                    w = new FileWriter(fragment.getContext().getFilesDir() + "/Organizzazioni.txt");
                    w.write(inline);
                    w.flush();
                    w.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            }
        });
        thread.start();
        thread.join();



    }
}


//    @Override
//    public ArrayList<Organizzazioni> performAggiornaLista(final Fragment fragment, final ArrayList<Organizzazioni> listaAttuale) throws InterruptedException {
//
//        final ArrayList<Organizzazioni> aux=new ArrayList<>();
//        Thread thread = new Thread(new Runnable() {
//
//            @Override
//            public void run() { try {
//                URL url = new URL("https://api.jsonbin.io/b/5e873f30dd6c3c63eaed8ed8/1");
//                //URL NON VALIDO
//                if (url == null) {
//                    //Toast.makeText(getActivity(), "Errore nello scaricamento", Toast.LENGTH_SHORT).show();
//                    return ;
//                }
//                OkHttpClient client = new OkHttpClient();
//                final Request req = new Request.Builder().url(url).get().build();
//                final Response resp = client.newCall(req).execute();
//                final int code = resp.code();
//                final ResponseBody body = resp.body();
//                String inline=" ";
//                String s=null;
//                inline = body.string();
//                File organizzazioniFile = new File(fragment.getContext().getFilesDir() + "/Organizzazioni.txt");
//                //ESISTE GIA' UN FILE CONTENENTE UNA LISTA DI ORGANIZZAZIONI NEL DISPOSITIVO
//                if (organizzazioniFile.exists()) {
//                    FileInputStream fin = new FileInputStream(organizzazioniFile);
//                    byte[] buffer = new byte[(int) organizzazioniFile.length()];
//                    new DataInputStream(fin).readFully(buffer);
//                    fin.close();
//                    s = new String(buffer, "UTF-8");
//                }
//                //ERRORE DAL SERVER
//                if (code != 200) {
//                    //Toast.makeText(getActivity(),"Errore nello scaricamento",Toast.LENGTH_SHORT).show();
//                    body.close();
//                    return ;
//
//                }
//                //LISTA ATTUALE E LISTA PRESENTE SUL SERVER UGUALI
//                if (s != null && s.equals(inline)) {
//                    body.close();
//                    return;
//                }
//                //LISTA ATTUALE E LISTA PRESENTE SUL SERVER DIVERSE
//                else
//                {
//
//                    System.out.println(inline);
//                    JSONObject jsonObject = new JSONObject(inline);
//                    JSONArray jsonArray = (JSONArray) jsonObject.get("listaOrganizzazioni");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject organizzazione = jsonArray.getJSONObject(i);
//                        String nomeOrganizzazione = organizzazione.getString("nome");
//                        System.out.println(nomeOrganizzazione);
//                        FileWriter w;
//                        w = new FileWriter(fragment.getContext().getFilesDir() + "/Organizzazioni.txt");
//                        w.write(inline);
//                        w.flush();
//                        w.close();
//                        Organizzazioni organizzazione1=new Organizzazioni(nomeOrganizzazione);
//                        aux.add(organizzazione1);
//                        body.close();
//                    }
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            }
//        });
//        thread.start();
//        thread.join();
//        return aux;
//
//
//    }
