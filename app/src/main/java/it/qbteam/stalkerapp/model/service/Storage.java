package it.qbteam.stalkerapp.model.service;

import androidx.fragment.app.Fragment;
import it.qbteam.stalkerapp.model.data.Organization;
import it.qbteam.stalkerapp.presenter.HomeContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import it.qbteam.stalkerapp.presenter.MyStalkersListContract;

public class Storage implements HomeContract.Model, MyStalkersListContract.Model {

    HomeContract.HomeListener homeListener;
    MyStalkersListContract.MyStalkerListener myStalkerListener;
    public Storage(HomeContract.HomeListener homeListener, MyStalkersListContract.MyStalkerListener myStalkerListener){
         this.homeListener=homeListener;
         this.myStalkerListener=myStalkerListener;
    }

    @Override
    public void performControllaLista(Fragment fragment, String nameFile) {
        //CONTROLLO ESISTENZA DEL FILE
        ArrayList<Organization> aux = new ArrayList<>();
        File organizzazioniFile = new File(fragment.getContext().getFilesDir()+nameFile);
        if(organizzazioniFile.length()==0 || !organizzazioniFile.exists()){
            if(nameFile=="/Organizzazioni.txt")
                homeListener.onFailureFile("Local file empty");
            else{
                myStalkerListener.onFailureFile("Local file empty");
            }

            return;
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
                    Organization organizzazione1=new Organization(nomeOrganizzazione);
                    System.out.println("organizzazione1:  " + organizzazione1);
                    aux.add(organizzazione1);
                }
            } catch (JSONException | FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
             if(nameFile=="/Organizzazioni.txt")
                 homeListener.onSuccessFile(aux);
             else{
                 myStalkerListener.onSuccessFile(aux);
             }

        }
    }

    @Override
    public ArrayList<Organization> performRimuovi(String name, ArrayList<Organization>list) {

        for (Iterator<Organization> iterator = list.iterator(); iterator.hasNext(); ) {
            Organization o = iterator.next();
            if (o.getNome().equals(name)) {
                // Remove the current element from the iterator and the list.
                iterator.remove();
            }
        }
    return list;
    }

    @Override
    public void performUpdateFile(ArrayList<Organization> list, Fragment fragment, String nameFile ) throws JSONException, IOException {

        JSONArray ja;
        JSONObject jo,mainObj;
        //CONVERTO LA LISTA DINAMICA IN UN NUOVO ARRAY
        String[] array = new String[list.size()];
        for(int i=0; i< array.length;i++)
            array[i]=list.get(i).getNome();

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
        String s="";
        s=mainObj.toString();
        FileWriter w;
        w=new FileWriter(fragment.getContext().getFilesDir()+nameFile);
        w.write(s);
        w.flush();
        w.close();

    }

    @Override
    public void performScaricaLista(final Fragment fragment, final ArrayList<Organization> listaAttuale) throws InterruptedException {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() { try {
                URL url = new URL("https://api.jsonbin.io/b/5e873f30dd6c3c63eaed8ed8/1");
                //URL NON VALIDO
                if (url == null) {

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

                //ERRORE DAL SERVER
                if (code != 200) {

                    body.close();
                    return ;

                }

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