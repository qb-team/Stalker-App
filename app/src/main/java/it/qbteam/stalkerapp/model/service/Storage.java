package it.qbteam.stalkerapp.model.service;

import it.qbteam.stalkerapp.model.backend.ApiClient;
import it.qbteam.stalkerapp.model.backend.api.OrganizationApi;
import it.qbteam.stalkerapp.model.backend.model.Organization;
import it.qbteam.stalkerapp.model.data.User;
import it.qbteam.stalkerapp.presenter.HomeContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.validation.constraints.NotNull;
import it.qbteam.stalkerapp.presenter.MyStalkersListContract;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Storage implements HomeContract.Model, MyStalkersListContract.Model {

    HomeContract.HomeListener homeListener;
    MyStalkersListContract.MyStalkerListener myStalkerListener;

    public Storage(HomeContract.HomeListener homeListener, MyStalkersListContract.MyStalkerListener myStalkerListener){
         this.homeListener=homeListener;
         this.myStalkerListener=myStalkerListener;
    }
    @Override
    public ArrayList<Organization> performCheckFile(String path) {

        //CONTROLLO ESISTENZA DEL FILE
        ArrayList<Organization> aux = new ArrayList<>();
        File organizationFile = new File(path);
        if(organizationFile.length()==0 || !organizationFile.exists()) {

        }
            //DA CAPIRE COME CHIAMARE QUELLO GIUSTO TRA HOME_FRAGMENT E MY_STALKER_FRAGMENT
            /*if(nameFile=="/Organizzazioni.txt")
                homeListener.onFailureFile("Local file empty");
            else{
                myStalkerListener.onFailureFile("Local file empty");
            }

            return;
        }*/
        else {
            try {

                FileInputStream fin = new FileInputStream(organizationFile);
                byte[] buffer = new byte[(int) organizationFile.length()];
                new DataInputStream(fin).readFully(buffer);
                fin.close();
                String s = new String(buffer, "UTF-8");
                JSONObject jsonObject = new JSONObject(s);
                System.out.println("jsonObject:  " + jsonObject);
                JSONArray jsonArray = (JSONArray) jsonObject.get("organisationList");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObj= jsonArray.getJSONObject(i);
                    String name= jsonObj.getString("name");
                    String city=jsonObj.getString("city");
                    String trackingMode=jsonObj.getString("trackingMode");
                    Organization organization=new Organization();
                    organization.setName(name);
                    organization.setCity(city);
                    organization.setTrackingMode(Organization.TrackingModeEnum.fromValue(trackingMode));
                    System.out.println("organization:  " + organization);
                    aux.add(organization);
                }

        } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return aux;
    }

    @Override
    public void performRemove(Organization organization, ArrayList<Organization>list, String path) throws IOException, JSONException {
        boolean trovato=false;
        for (Iterator<Organization> iterator = list.iterator(); iterator.hasNext(); ) {
            Organization o = iterator.next();
            if (o.getName().equals(organization.getName())) {
                // Remove the current element from the iterator and the list.
                iterator.remove();
                trovato=true;
            }
        }
        if(trovato){
            myStalkerListener.onSuccesRemove(list);
            saveInLocalFile(list,path);

        }

    }



    @Override
    public void performAddOrganizationLocal(Organization organization, ArrayList<Organization> list,String path) throws IOException, JSONException {
        boolean trovato = false;
        for (Iterator<Organization> iterator = list.iterator(); iterator.hasNext();) {
            Organization o = iterator.next();
            if (o.getName().equals(organization.getName())) {
                trovato = true;
            }
        }
        if(trovato){

            myStalkerListener.onFailureAdd("Questa organizzazione è stata già aggiunta a MyStalkers");

        }

        else {

            list.add(organization);
            saveInLocalFile(list,path);
            myStalkerListener.onSuccessAdd("Hai aggiunto l'organizzazione a MyStalkers");
        }


    }

    @Override
    public void performDownloadFile(String path, User user)  {
        ArrayList<Organization> returnList=new ArrayList<>();
        ApiClient ac = new ApiClient("bearerAuth").setBearerToken(user.getToken());
        OrganizationApi service = ac.createService(OrganizationApi.class);
        Call<List<Organization>> orgList = service.getOrganizationList();
        orgList.enqueue(new Callback<List<Organization>>() {
            @Override
            public void onResponse(@NotNull Call<List<Organization>> call, @NotNull Response<List<Organization>> response) {

                String inline="";

                for(int i=0;i<response.body().size();i++){
                       Organization o =new Organization();
                       o.setName(response.body().get(i).getName());
                       o.setCity(response.body().get(i).getCity());
                       o.setCountry(response.body().get(i).getCountry());
                       o.setAuthenticationServerURL(response.body().get(i).getAuthenticationServerURL());
                       o.setCreationDate(response.body().get(i).getCreationDate());
                       o.setDescription(response.body().get(i).getDescription());
                       o.setId(response.body().get(i).getId());
                       o.setImage(response.body().get(i).getImage());
                       o.setLastChangeDate(response.body().get(i).getLastChangeDate());
                       o.setNumber(response.body().get(i).getNumber());
                       o.setPostCode(response.body().get(i).getPostCode());
                       o.setStreet(response.body().get(i).getStreet());
                       o.setTrackingArea(response.body().get(i).getTrackingArea());
                       o.setTrackingMode(response.body().get(i).getTrackingMode());
                       returnList.add(o);
                    }

                FileWriter w;

                try {

                        w = new FileWriter(path);
                        w.write(inline);
                        System.out.println(inline);
                        w.flush();
                        w.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    saveInLocalFile(returnList,path);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                homeListener.onSuccessDownload(returnList);
            }

            @Override
            public void onFailure(Call<List<Organization>> call, Throwable t) {
                 homeListener.onFailureDownload("Errore durante lo scaricamento della lista");
            }});
    }
   /* private void convertToJson(ArrayList<Organization> returnList,String path) throws IOException {
        String jsonString="";
        for(int i=0;i<returnList.size();i++){
        Organization o = new Organization();
        o=returnList.get(i);
        ObjectMapper mapper = new ObjectMapper();
        //Converting the Object to JSONString
        jsonString += mapper.writeValueAsString(o);
        System.out.println("CIAOOOO"+jsonString);
        }
        FileWriter w;
        w = new FileWriter(path);
        w.write(jsonString);
        System.out.println(jsonString);
        w.flush();
        w.close();

    }*/
 public void saveInLocalFile(ArrayList<Organization>list,String path) throws JSONException, IOException {

    JSONArray ja = new JSONArray();

    for(int i=0;i<list.size();i++) {
        JSONObject jo = new JSONObject();
        jo.put("id", list.get(i).getId());
        jo.put("name", list.get(i).getName());
        jo.put("description", list.get(i).getDescription());
        jo.put("image", list.get(i).getImage());
        jo.put("street", list.get(i).getStreet());
        jo.put("postCode", list.get(i).getPostCode());
        jo.put("city", list.get(i).getCity());
        jo.put("country", list.get(i).getCountry());
        jo.put("authenticationServerURL", list.get(i).getAuthenticationServerURL());
        jo.put("creationDate", list.get(i).getCreationDate());
        jo.put("lastChangeDate", list.get(i).getLastChangeDate());
        jo.put("trackingArea", list.get(i).getTrackingArea());
        jo.put("trackingMode", list.get(i).getTrackingMode().toString());
        ja.put(jo);
    }
    JSONObject mainObj = new JSONObject();
    mainObj.put("organisationList", ja);

    String inline=mainObj.toString();
            FileWriter w;
            w = new FileWriter(path);
            w.write(inline);
         System.out.println(inline);
         w.flush();
         w.close();


}




}