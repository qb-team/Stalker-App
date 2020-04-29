package it.qbteam.stalkerapp.model.service;

import androidx.fragment.app.Fragment;

import it.qbteam.stalkerapp.model.backend.ApiClient;
import it.qbteam.stalkerapp.model.backend.api.OrganizationApi;
import it.qbteam.stalkerapp.model.backend.model.OrganizationTommaso;
import it.qbteam.stalkerapp.model.data.Organization;
import it.qbteam.stalkerapp.model.data.User;
import it.qbteam.stalkerapp.presenter.HomeContract;
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
import java.util.Iterator;
import java.util.List;

import javax.validation.constraints.NotNull;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import it.qbteam.stalkerapp.presenter.MyStalkersListContract;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Storage implements HomeContract.Model, MyStalkersListContract.Model {

    HomeContract.HomeListener homeListener;
    MyStalkersListContract.MyStalkerListener myStalkerListener;

    public Storage(HomeContract.HomeListener homeListener, MyStalkersListContract.MyStalkerListener myStalkerListener){
         this.homeListener=homeListener;
         this.myStalkerListener=myStalkerListener;
    }
    @Override
    public void performCheckFile(Fragment fragment, String nameFile) {

        //CONTROLLO ESISTENZA DEL FILE
        ArrayList<Organization> aux = new ArrayList<>();
        File organizationFile = new File(fragment.getContext().getFilesDir()+nameFile);
        if(organizationFile.length()==0 || !organizationFile.exists()){
            if(nameFile=="/Organizzazioni.txt")
                homeListener.onFailureFile("Local file empty");
            else{
                myStalkerListener.onFailureFile("Local file empty");
            }

            return;
        }
        else {
            try {
                System.out.println("organizationFile:  " + organizationFile);
                FileInputStream fin=new FileInputStream(organizationFile);
                System.out.println("fin:  " + fin);
                byte[] buffer= new byte[(int)organizationFile.length()];
                System.out.println("buffer:  " + buffer);
                new DataInputStream(fin).readFully(buffer);
                fin.close();
                String s=new String(buffer,"UTF-8");
                JSONObject jsonObject = new JSONObject(s);
                System.out.println("jsonObject:  " + jsonObject);
                JSONArray jsonArray = (JSONArray) jsonObject.get("listaOrganizzazioni");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject organization= jsonArray.getJSONObject(i);

                    String organizationName= organization.getString("nome");
                    Organization organization1=new Organization(organizationName);
                    System.out.println("organization1:  " + organization1);
                    aux.add(organization1);
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
    public void performRemove(String name, ArrayList<Organization>list) throws IOException, JSONException {

        for (Iterator<Organization> iterator = list.iterator(); iterator.hasNext(); ) {
            Organization o = iterator.next();
            if (o.getNome().equals(name)) {
                // Remove the current element from the iterator and the list.
                iterator.remove();
            }
        }
           myStalkerListener.onSuccesRemove(list);
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
    public void performFindOrganization(Organization organization, ArrayList<Organization> list) throws IOException, JSONException {
        boolean trovato = false;
        for (Iterator<Organization> iterator = list.iterator(); iterator.hasNext();) {
            Organization o = iterator.next();
            if (o.getNome().equals(organization.getNome())) {
                trovato = true;
            }
        }
        if(trovato)
            myStalkerListener.onSuccessSearch("L'organizzazione è già presente nella lista MyStalker");
        else
            myStalkerListener.onFailureSearch(organization);
    }

    @Override
    public void performAddOrganization(Organization organization, ArrayList<Organization> list) throws IOException, JSONException {
        list.add(organization);
        myStalkerListener.onSuccessAdd("L'organizzazione è stata aggiunta alla lista MyStalker");

    }

    @Override
    public void performDownloadFile(final Fragment fragment, final ArrayList<Organization> actualList, User user) throws InterruptedException, IOException {
        ApiClient ac = new ApiClient("bearerAuth").setBearerToken(user.getToken());
        OrganizationApi service = ac.createService(OrganizationApi.class);
        Call<List<OrganizationTommaso>> orgList = service.getOrganizationList();
        orgList.enqueue(new Callback<List<OrganizationTommaso>>() {
            @Override
            public void onResponse(@NotNull Call<List<OrganizationTommaso>> call, @NotNull Response<List<OrganizationTommaso>> response) {
                    ArrayList<OrganizationTommaso> org=new ArrayList<>();
                    String inline=response.body().toString();

                   for(int i=0;i<response.body().size();i++){
                       org.add(new OrganizationTommaso());
                       org.get(i).setName(response.body().get(i).getName());
                       System.out.println(org.get(i).getName());





                   }

                /*FileWriter w;
                w = new FileWriter(fragment.getContext().getFilesDir() + "/Organizzazioni.txt");
                w.write(inline);
                w.flush();
                w.close();*/


            }

            @Override
            public void onFailure(Call<List<OrganizationTommaso>> call, Throwable t) {

            }});
    }

       /* Thread thread = new Thread(new Runnable() {

            @Override
            public void run() { try {

              //  URL url = new URL("https://api.jsonbin.io/b/5e873f30dd6c3c63eaed8ed8/1");

                URL url = new URL("http://2.234.128.81:8080/organization");
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
        thread.join();*/




}