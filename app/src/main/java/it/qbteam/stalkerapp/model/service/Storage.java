package it.qbteam.stalkerapp.model.service;

import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.contract.AccessHistoryContract;
import it.qbteam.stalkerapp.contract.PlaceAccessContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationAccess;
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
import java.util.List;
import it.qbteam.stalkerapp.contract.MyStalkersListContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.Place;
import it.qbteam.stalkerapp.model.backend.dataBackend.PlaceAccess;
import it.qbteam.stalkerapp.model.backend.dataBackend.PlaceMovement;

public class Storage implements HomeContract.Interactor, MyStalkersListContract.Interactor {

    private HomeContract.HomeListener homeListener;
    private MyStalkersListContract.MyStalkerListener myStalkerListener;
    private AccessHistoryContract.AccessHistoryListener accessHistoryListener;
    private PlaceAccessContract.PlaceAccessListener placeAccessListener;

    //Storage's constructor.
    public Storage(HomeContract.HomeListener homeListener, MyStalkersListContract.MyStalkerListener myStalkerListener,  AccessHistoryContract.AccessHistoryListener accessHistoryListener, PlaceAccessContract.PlaceAccessListener placeAccessListener){
         this.homeListener = homeListener;
         this.myStalkerListener = myStalkerListener;
         this.accessHistoryListener = accessHistoryListener;
         this.placeAccessListener = placeAccessListener;
    }

    //Checks if the list of organizations already exists in local file.
    @Override
    public List<Organization> performCheckFileLocal(String path) {

        List<Organization> aux = new ArrayList<>();
        File organizationFile = new File(path);
        if(organizationFile.length()==0 || !organizationFile.exists()) {
            if(homeListener != null)
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

                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObj= jsonArray.getJSONObject(i);
                    String name= jsonObj.getString("name");
                    String description=jsonObj.getString("description");
                    String image=jsonObj.getString("image");
                    String city=jsonObj.getString("city");
                    String country=jsonObj.getString("country");
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
                    organization.setCountry(country);
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

            }
            catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
        return aux;
    }

    //Removes an organization from the local file and update the local file.
    @Override
    public void performRemoveLocal(Organization organization, List<Organization>list, String path) throws IOException, JSONException {
        boolean trovato = false;
        for (Iterator<Organization> iterator = list.iterator(); iterator.hasNext();) {
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

    //Adds an organization to the list of organizations and update the local file.
    @Override
    public void performAddOrganizationLocal(Organization organization, List<Organization> list,String path) throws IOException, JSONException {
        boolean trovato = false;
        if(list != null){
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
            }
        }

        else{
            list = new ArrayList<>();
            list.add(organization);
            performUpdateFile(list,path);
            myStalkerListener.onSuccessAdd(list,"Hai aggiunto l'organizzazione a MyStalkers");
        }
    }

    //Updates the list of organizations in the local file.
    public void performUpdateFile(List<Organization> list, String path) throws IOException, JSONException {

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

    public void serializePlaceInLocal(List<Place> place) throws IOException {
        //Saving places' list in a file
        File toWrite = new File(HomePageActivity.getPath()+"/PlaceList.txt");
        FileOutputStream fos=new FileOutputStream(toWrite,false);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        // Method for serialization of OrganizationMovement
        oos.writeObject(place);
        oos.flush();
        oos.close();
        fos.close();
    }
    public List<Place> deserializePlaceInLocal() throws IOException, ClassNotFoundException{

        List<Place> place;
        //Reading the places' list from a file
        FileInputStream fis= new FileInputStream(HomePageActivity.getPath()+"/PlaceList.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        //Method for deserialization of object
        place = (ArrayList)ois.readObject();
        ois.close();
        fis.close();
        return place;
    }

    //Deletes the current object Place serialized in a local file.
    public void deletePlace() throws IOException {

        //Reading the OrganizationMovement from a file
        File toDelete=new File(HomePageActivity.getPath()+"/PlaceList.txt");
        FileOutputStream fos=new FileOutputStream(toDelete);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        //Write the object OrganizationMovement null==delete
        oos.writeObject(null);
        oos.flush();
        oos.close();
        fos.close();
    }

    public void serializePlaceMovement(PlaceMovement placeMovement) throws IOException {
        //Saving of OrganizationMovement in a file
        File toWrite = new File(HomePageActivity.getPath()+"/PlaceMovement.txt");
        FileOutputStream fos=new FileOutputStream(toWrite,false);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        // Method for serialization of OrganizationMovement
        oos.writeObject(placeMovement);
        oos.flush();
        oos.close();
        fos.close();

    }
    public PlaceMovement deserializePlaceMovement() throws IOException, ClassNotFoundException {
        PlaceMovement placeMovement;
        //Reading the OrganizationMovement from a file
        FileInputStream fis= new FileInputStream(HomePageActivity.getPath()+"/PlaceMovement.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        //Method for deserialization of object
        placeMovement = (PlaceMovement)ois.readObject();
        ois.close();
        fis.close();
        return placeMovement;
    }

    //Serializes the object OrganizationMovement in a local file.
    public void serializeOrganizationMovementInLocal(OrganizationMovement organizationMovement) throws IOException {

        //Saving of OrganizationMovement in a file
        File toWrite = new File(HomePageActivity.getPath()+"/OrganizationMovement.txt");
        FileOutputStream fos=new FileOutputStream(toWrite,false);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        // Method for serialization of OrganizationMovement
        oos.writeObject(organizationMovement);
        oos.flush();
        oos.close();
        fos.close();

    }


    //Deserializes the object OrganizationMovement from a local file.
    public OrganizationMovement deserializeOrganizationMovementInLocal() throws IOException, ClassNotFoundException {

        OrganizationMovement organizationMovement;
        //Reading the OrganizationMovement from a file
        FileInputStream fis= new FileInputStream(HomePageActivity.getPath()+"/OrganizationMovement.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        //Method for deserialization of object
        organizationMovement = (OrganizationMovement)ois.readObject();
        ois.close();
        fis.close();
        return organizationMovement;

    }

    public void performCreateAllFile() throws IOException {

        String[] paths={HomePageActivity.getPath()+"/OrganizationMovement.txt",HomePageActivity.getPath()+"/PlaceMovement.txt",
                HomePageActivity.getPath()+"/PlaceList.txt"};

        for(int i=0; i<paths.length; i++){
           File file=new File(paths[i]);
           FileOutputStream fos=new FileOutputStream(file);
           ObjectOutputStream oos=new ObjectOutputStream(fos);
           oos.writeObject(null);
           oos.flush();
           oos.close();
           fos.close();

        }
    }

    public void serializePlaceAccessInLocal(PlaceAccess placeAccess) throws IOException, ClassNotFoundException {
        //Saving of PlaceAccess in a file
        List<PlaceAccess> oldList;
        File placeAccessFile = new File(HomePageActivity.getPath()+"/PlaceAccess.txt");
        if(placeAccessFile.length()==0 || !placeAccessFile.exists()) {
            FileOutputStream fos=new FileOutputStream(placeAccessFile);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oldList= new ArrayList<>();
            oldList.add(placeAccess);
            oos.writeObject(oldList);
            oos.flush();
            oos.close();
            fos.close();
        }
        else {
            FileInputStream fis= new FileInputStream(placeAccessFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            //Method for deserialization of object
            oldList= (List<PlaceAccess>) ois.readObject();
            ois.close();
            fis.close();
            if(oldList!=null)
                oldList.add(placeAccess);
            else{
                oldList = new ArrayList<>();
                oldList.add(placeAccess);
            }
            File toWrite = new File(HomePageActivity.getPath()+"/PlaceAccess.txt");
            FileOutputStream fos=new FileOutputStream(toWrite);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            // Method for serialization of PlaceAccess
            oos.writeObject(oldList);
            oos.flush();
            oos.close();
            fos.close();
        }
    }

    //Deserializes the object PlaceAccess from a local file.
    public void deserializePlaceAccessInLocal() throws IOException, ClassNotFoundException {

        List<PlaceAccess> placeAccessList;
        //Reading the OrganizationMovement from a file
        File placeAccessFile = new File(HomePageActivity.getPath()+"/PlaceAccess.txt");
        if(placeAccessFile.length() == 0 || !placeAccessFile.exists()) {
            FileOutputStream fos=new FileOutputStream(placeAccessFile);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(null);
            oos.flush();
            oos.close();
            fos.close();
        }
        else{

            FileInputStream fis= new FileInputStream(HomePageActivity.getPath()+"/PlaceAccess.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            //Method for deserialization of object
            placeAccessList= (List<PlaceAccess>) ois.readObject();
            ois.close();
            fis.close();
            placeAccessListener.onSuccessGetPlaceAccess(placeAccessList);
        }
    }
    public void performDeletePlaceAccess(Long orgID) throws IOException, ClassNotFoundException {
        List<PlaceAccess> placeAccessList;
        //Reading the placeAccess from a file
        File placeAccessFile = new File(HomePageActivity.getPath()+"/PlaceAccess.txt");
        if(placeAccessFile.length() == 0 || !placeAccessFile.exists()) {
            FileOutputStream fos=new FileOutputStream(placeAccessFile);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(null);
            oos.flush();
            oos.close();
            fos.close();
        }
        else{

            FileInputStream fis= new FileInputStream(HomePageActivity.getPath()+"/PlaceAccess.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            //Method for deserialization of object
            placeAccessList= (List<PlaceAccess>) ois.readObject();
            ois.close();
            fis.close();
            boolean trovato = false;
            for (Iterator<PlaceAccess> iterator = placeAccessList.iterator(); iterator.hasNext();) {
                PlaceAccess pa = iterator.next();
                if (pa.getOrgId().equals(orgID)) {
                    iterator.remove();
                    trovato=true;
                }
            }
            if(trovato){
                File toWrite = new File(HomePageActivity.getPath()+"/PlaceAccess.txt");
                FileOutputStream fos=new FileOutputStream(toWrite);
                ObjectOutputStream oos=new ObjectOutputStream(fos);
                // Method for serialization of PlaceAccess
                oos.writeObject(placeAccessList);
                oos.flush();
                oos.close();
                fos.close();
            }

        placeAccessListener.onSuccessDelete();
    }
    }
    //Serializes the object OrganizationAccess in a local file.
    public void serializeOrganizationAccessInLocal(OrganizationAccess organizationAccess) throws IOException, ClassNotFoundException {
        //Saving of OrganizationAccess in a file
        List<OrganizationAccess> oldList;
        File placeAccessFile = new File(HomePageActivity.getPath()+"/OrganizationAccess.txt");
        if(placeAccessFile.length()==0 || !placeAccessFile.exists()) {
            FileOutputStream fos=new FileOutputStream(placeAccessFile,false);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oldList= new ArrayList<>();
            oldList.add(organizationAccess);
            oos.writeObject(oldList);
            oos.flush();
            oos.close();
            fos.close();
        }
        else {
            if(organizationAccess!=null){
            FileInputStream fis= new FileInputStream(placeAccessFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            //Method for deserialization of object
            oldList= (List<OrganizationAccess>) ois.readObject();
            ois.close();
            fis.close();
            if(oldList!=null)
                oldList.add(organizationAccess);
            else{
                oldList = new ArrayList<>();
                oldList.add(organizationAccess);
            }
            File toWrite = new File(HomePageActivity.getPath()+"/OrganizationAccess.txt");
            FileOutputStream fos=new FileOutputStream(toWrite,false);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            // Method for serialization of OrganizationMovement
            oos.writeObject(oldList);
            oos.flush();
            oos.close();
            fos.close();
        }
        }
    }

    //Deserializes the object OrganizationAccess from a local file.
    public void deserializeOrganizationAccessInLocal() throws IOException, ClassNotFoundException {

        List<OrganizationAccess> organizationAccessList;
        //Reading the OrganizationMovement from a file
        File organizationAccessFile = new File(HomePageActivity.getPath()+"/OrganizationAccess.txt");
        if(organizationAccessFile.length()==0 || !organizationAccessFile.exists()) {
            FileOutputStream fos=new FileOutputStream(organizationAccessFile);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(null);
            oos.flush();
            oos.close();
            fos.close();
        }
        else{

        FileInputStream fis= new FileInputStream(HomePageActivity.getPath()+"/OrganizationAccess.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        //Method for deserialization of object
        organizationAccessList= (List<OrganizationAccess>) ois.readObject();
        ois.close();
        fis.close();
        accessHistoryListener.onSuccessGetOrganizationAccess(organizationAccessList);
        }
    }

    public void performDeleteOrganizationAccess() throws IOException {
        //Reading the OrganizationMovement from a file
        File toDelete=new File(HomePageActivity.getPath()+"/OrganizationAccess.txt");
        FileOutputStream fos=new FileOutputStream(toDelete);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        //Write the object OrganizationMovement null==delete
        oos.writeObject(null);
        oos.flush();
        oos.close();
        fos.close();
        accessHistoryListener.onSuccessDelete();
    }

    public void saveLastAccess(OrganizationMovement organizationMovement) throws IOException {
        System.out.print("salvo LAST ACCESS:"+organizationMovement);

        //Saving of OrganizationMovement in a file
        File toWrite = new File(HomePageActivity.getPath()+"/LastOrganizationAccess"+organizationMovement.getOrganizationId()+".txt");
        FileOutputStream fos1=new FileOutputStream(toWrite,false);
        ObjectOutputStream oos1=new ObjectOutputStream(fos1);

        // Method for serialization of OrganizationMovement
        oos1.writeObject(organizationMovement);
        oos1.flush();
        oos1.close();
        fos1.close();

    }

    public OrganizationMovement performGetLastAccess(Long orgID) throws IOException, ClassNotFoundException {

            OrganizationMovement organizationMovement;
            //Reading the OrganizationMovement from a file
            File organizationAccessFile = new File(HomePageActivity.getPath()+"/LastOrganizationAccess"+orgID+".txt");
            FileInputStream fis= new FileInputStream(organizationAccessFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            //Method for deserialization of object
            organizationMovement= (OrganizationMovement) ois.readObject();
            ois.close();
            fis.close();

        return organizationMovement;
    }
    }




