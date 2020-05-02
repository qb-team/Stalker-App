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
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
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
            if(homeListener!=null)
                 homeListener.onFailureCheck("La lista è ancora vuota, scaricala!");

        }

        else {
            try {
                FileInputStream fin = new FileInputStream(organizationFile);
                byte[] buffer = new byte[(int) organizationFile.length()];
                new DataInputStream(fin).readFully(buffer);
                fin.close();
                String s = new String(buffer, "UTF-8");
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = (JSONArray) jsonObject.get("organisationList");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObj= jsonArray.getJSONObject(i);

                    String name= jsonObj.getString("name");
                    String description=jsonObj.getString("description");
                    String image=jsonObj.getString("image");
                    String city=jsonObj.getString("city");
                    String trackingMode=jsonObj.getString("trackingMode");
                    Long orgId=jsonObj.getLong("id");
                    String creationDate=jsonObj.getString("creationDate");
                    String serverUrl;

                    Organization organization=new Organization();
                    organization.setName(name);
                    organization.setImage(image);
                    organization.setDescription(description);
                    organization.setCity(city);
                    organization.setId(orgId);
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                    OffsetDateTime offsetDateTime = OffsetDateTime.parse(creationDate, dateTimeFormatter);
                    organization.setCreationDate(offsetDateTime);

                    if(trackingMode.equals("authenticated")){
                        serverUrl=jsonObj.getString("authenticationServerURL");
                        organization.setAuthenticationServerURL(serverUrl);
                    }
                    organization.setTrackingMode(Organization.TrackingModeEnum.fromValue(trackingMode));
                    System.out.println("organization:  " + organization);
                    aux.add(organization);
                }

        } catch (JSONException | IOException e) {
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


                for(int i=0;i<response.body().size();i++){
                       Organization o =new Organization();
                       o.setName(response.body().get(i).getName());
                       o.setCity(response.body().get(i).getCity());
                       o.setCountry(response.body().get(i).getCountry());
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
                       if(response.body().get(i).getTrackingMode().getValue()=="authenticated")
                           o.setAuthenticationServerURL(response.body().get(i).getAuthenticationServerURL());
                       returnList.add(o);
                    }

                try {
                    saveInLocalFile(returnList,path);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                homeListener.onSuccessDownload("Lista scaricata con successo");
            }

            @Override
            public void onFailure(Call<List<Organization>> call, Throwable t) {
                 homeListener.onFailureDownload("Errore durante lo scaricamento della lista");
            }});
    }

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