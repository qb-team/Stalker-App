package it.qbteam.stalkerapp.model.service;

import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;
import it.qbteam.stalkerapp.contract.HomeContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

import it.qbteam.stalkerapp.contract.MyStalkersListContract;

public class Storage implements HomeContract.Model, MyStalkersListContract.Model {

    HomeContract.HomeListener homeListener;
    MyStalkersListContract.MyStalkerListener myStalkerListener;

    public Storage(HomeContract.HomeListener homeListener, MyStalkersListContract.MyStalkerListener myStalkerListener){
         this.homeListener=homeListener;
         this.myStalkerListener=myStalkerListener;
    }


    @Override
    public ArrayList<Organization> performCheckFileLocal(String path) {

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
                    String trackingArea=jsonObj.getString("trackingArea");
                    Organization organization=new Organization();
                    organization.setName(name);
                    organization.setImage(image);
                    organization.setDescription(description);
                    organization.setCity(city);
                    organization.setId(orgId);
                    organization.setTrackingArea(trackingArea);
                    organization.setTrackingMode(trackingMode);
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                    OffsetDateTime offsetDateTime = OffsetDateTime.parse(creationDate, dateTimeFormatter);
                    organization.setCreationDate(offsetDateTime);

                    if(trackingMode.equals("authenticated")){
                        serverUrl=jsonObj.getString("authenticationServerURL");
                        organization.setAuthenticationServerURL(serverUrl);
                    }

                    aux.add(organization);
                }

        } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
        return aux;
    }

    @Override
    public void performRemoveLocal(Organization organization, ArrayList<Organization>list, String path) throws IOException, JSONException {
        boolean trovato=false;
        for (Iterator<Organization> iterator = list.iterator(); iterator.hasNext(); ) {
            Organization o = iterator.next();
            if (o.getName().equals(organization.getName())) {
                iterator.remove();
                trovato=true;
            }
        }
        if(trovato){
            myStalkerListener.onSuccesRemove(list);
            performUpdateFile(list,path);

        }

    }

    @Override
    public void performAddOrganizationLocal(Organization organization, ArrayList<Organization> list,String path) throws IOException, JSONException {
        boolean trovato = false;
        if(list!=null){
        for (Iterator<Organization> iterator = list.iterator(); iterator.hasNext();) {
            Organization o = iterator.next();
            if (o.getName().equals(organization.getName())) {
                trovato = true;
            }
        }
        if(trovato){
            myStalkerListener.onFailureAdd("Organizzazione già presente in MyStalkers");
        }
        else {
            list.add(organization);
            performUpdateFile(list,path);
            myStalkerListener.onSuccessAdd(list,"Hai aggiunto l'organizzazione a MyStalkers");
        }}
        else{
            list=new ArrayList<>();
            list.add(organization);
            performUpdateFile(list,path);
            myStalkerListener.onSuccessAdd(list,"Hai aggiunto l'organizzazione a MyStalkers");
        }
    }

    public void performUpdateFile(ArrayList<Organization> list, String path) throws IOException, JSONException {

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
        jo.put("trackingMode", list.get(i).getTrackingMode());
        ja.put(jo);
    }

    JSONObject mainObj = new JSONObject();
    mainObj.put("organisationList", ja);

    String inline=mainObj.toString();
            FileWriter w;
            w = new FileWriter(path);
            w.write(inline);
            w.flush();
            w.close();
    }


    public static void serializeMovementInLocal(OrganizationMovement organizationMovement) throws IOException {

        //Saving of OrganizationMovement in a file
        File toWrite = new File("data/user/0/it.qbteam.stalkerapp/files/Movement.txt");
        FileOutputStream fos=new FileOutputStream(toWrite);
        ObjectOutputStream oos=new ObjectOutputStream(fos);

        // Method for serialization of OrganizationMovement
        oos.writeObject(organizationMovement);
        oos.flush();
        oos.close();
        fos.close();

    }
    public static OrganizationMovement deserializeMovementInLocal() throws IOException, ClassNotFoundException {

        OrganizationMovement organizationMovement= new OrganizationMovement();
        // Reading the OrganizationMovement from a file
        FileInputStream fis= new FileInputStream("data/user/0/it.qbteam.stalkerapp/files/Movement.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        // Method for deserialization of object
        organizationMovement = (OrganizationMovement)ois.readObject();
        ois.close();
        fis.close();
        return organizationMovement;

    }

    public static void deleteMovement() throws IOException {

        OrganizationMovement organizationMovement=null;
        // Reading the OrganizationMovement from a file
        File toDelete=new File("data/user/0/it.qbteam.stalkerapp/files/Movement.txt");
        FileOutputStream fos=new FileOutputStream(toDelete);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        //sovvrascivo con OrganizationMovement null==delete
        oos.writeObject(organizationMovement);
        oos.flush();
        oos.close();
        fos.close();
    }
}