package it.qbteam.stalkerapp.model.service;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.json.JSONException;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;

import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.model.backend.ApiClient;
import it.qbteam.stalkerapp.model.backend.api.FavoriteApi;
import it.qbteam.stalkerapp.model.backend.api.MovementApi;
import it.qbteam.stalkerapp.model.backend.api.OrganizationApi;
import it.qbteam.stalkerapp.model.backend.api.PlaceApi;
import it.qbteam.stalkerapp.model.backend.dataBackend.Favorite;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationAccess;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;
import it.qbteam.stalkerapp.contract.HomeContract;
import it.qbteam.stalkerapp.contract.MyStalkersListContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.Place;
import it.qbteam.stalkerapp.model.backend.dataBackend.PlaceAccess;
import it.qbteam.stalkerapp.model.backend.dataBackend.PlaceMovement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Server {

    private  MyStalkersListContract.MyStalkerListener myStalkerListener;
    private  HomeContract.HomeListener homeListener;
    private Storage storage;

    //Server's constructor.
    public Server(MyStalkersListContract.MyStalkerListener myStalkerListener, HomeContract.HomeListener homeListener) {
        this.myStalkerListener = myStalkerListener;
        this.homeListener = homeListener;
        storage= new Storage(null, null);
    }

    //Removes the organization from the user's favorite organization list.
    public void performRemoveOrganizationServer(Organization organization, String UID, String userToken) {

                    Favorite favoriteUpload = new Favorite();
                    favoriteUpload.setUserId(UID);
                    favoriteUpload.setCreationDate(organization.getCreationDate());
                    favoriteUpload.setOrgAuthServerId(organization.getAuthenticationServerURL());
                    favoriteUpload.setOrganizationId(organization.getId());
                    ApiClient ac = new ApiClient("bearerAuth").setBearerToken(userToken);
                    FavoriteApi service = ac.createService(FavoriteApi.class);
                    Call<Void> favorite = service.removeFavoriteOrganization(favoriteUpload);
                    favorite.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                    }
            });

    }

    //Gets the list of favorite organizations of a user.
    public void performLoadFavoriteListServer(String UID, String userToken) {

                Favorite favoriteDownload = new Favorite();
                favoriteDownload.setUserId(UID);
                ApiClient ac = new ApiClient("bearerAuth").setBearerToken(userToken);
                FavoriteApi service = ac.createService(FavoriteApi.class);
                Call<List<Organization>> favorite = service.getFavoriteOrganizationList(favoriteDownload.getUserId());
                favorite.enqueue(new Callback<List<Organization>>() {
                    @Override
                    public void onResponse(Call<List<Organization>> call, Response<List<Organization>> response) {
                        try {
                            if(response.code()==200)
                                myStalkerListener.onSuccessLoad(response.body());
                            else
                                myStalkerListener.onFailureLoad();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onFailure(Call<List<Organization>> call, Throwable t) {
                    }
                });
    }

    //Adds a new organization to the user's favorite organization list.
    public void performAddOrganizationServer(Organization organization, String UID, String userToken) {

        Favorite favoriteUpload = new Favorite();
        favoriteUpload.setUserId(UID);
        favoriteUpload.setOrganizationId(organization.getId());
        favoriteUpload.setCreationDate(organization.getCreationDate());
        favoriteUpload.setOrgAuthServerId(organization.getAuthenticationServerURL());
        ApiClient ac = new ApiClient("bearerAuth").setBearerToken(userToken);
        FavoriteApi service = ac.createService(FavoriteApi.class);
        Call<Favorite> favorite = service.addFavoriteOrganization(favoriteUpload);
        favorite.enqueue(new Callback<Favorite>() {
            @Override
            public void onResponse(Call<Favorite> call, Response<Favorite> response) {

            }
            @Override
            public void onFailure(Call<Favorite> call, Throwable t) {
            }
        });
    }

    public void performDownloadPlaceServer(Long orgID, String userToken, SharedPreferences.Editor prefEditor, Gson gson)  {
        Place placeDownload = new Place();
        placeDownload.setOrganizationId(orgID);
        ApiClient ac = new ApiClient("bearerAuth").setBearerToken(userToken);
        PlaceApi service = ac.createService(PlaceApi.class);
        Call<List<Place>> place = service.getPlaceListOfOrganization(placeDownload.getOrganizationId());
        place.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                System.out.print(response.body());
                if(response.code()==200){
                    String placeDownloadJson = gson.toJson(response.body());
                    prefEditor.putString("placeDownload",placeDownloadJson);
                    prefEditor.commit();
                }
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {

            }
        });
    }

    //Tracks the user movement inside the trackingArea of an organization.
    public void performOrganizationMovementServer(String authServerID, Long orgID, String userToken, int type, String exitToken, OrganizationAccess organizationAccess, SharedPreferences.Editor prefEditor, Gson gson) {

        OrganizationMovement movementUpload = new OrganizationMovement();
        movementUpload.setMovementType(type);
        OffsetDateTime dateTime= OffsetDateTime.now();
        movementUpload.setTimestamp(dateTime);
        movementUpload.setOrganizationId(orgID);
        if(authServerID != null)
            movementUpload.setOrgAuthServerId(authServerID);
        if(type == -1)
            movementUpload.setExitToken(exitToken);
        System.out.print("MOVEMENT UPLOAD "+movementUpload+ "BEARTOKENN"+userToken);
        ApiClient ac = new ApiClient("bearerAuth").setBearerToken(userToken);
        MovementApi service = ac.createService(MovementApi.class);
        Call<OrganizationMovement> movement = service.trackMovementInOrganization(movementUpload);
        movement.enqueue(new Callback<OrganizationMovement>() {
            @Override
            public void onResponse(Call<OrganizationMovement> call, Response<OrganizationMovement> response) {

                try {
                    if(type==1 && response.code()==201){
                        movementUpload.setExitToken(response.body().getExitToken());
                        String organizationMovementJson = gson.toJson(movementUpload);
                        prefEditor.putString("organizationMovement",organizationMovementJson);
                        prefEditor.commit();
                        storage.saveLastAccess(movementUpload);
                    }
                    else if(type==-1 && response.code()==202){
                        //serialize in local the object List<OrganizationAccess>.
                        System.out.println("DATA: 0" + organizationAccess.getEntranceTimestamp() + " " + organizationAccess.getExitTimestamp());
                        String organizationAccessJson = gson.toJson(organizationAccess);
                        System.out.println(organizationAccessJson);
                        prefEditor.putString("organizationAccess",organizationAccessJson);
                        prefEditor.commit();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<OrganizationMovement> call, Throwable t) {
            }
        });

    }
    public void performPlaceMovementServer(String exitToken, int type, Long placeId, String authServerID, String userToken, PlaceAccess placeAccess, SharedPreferences.Editor prefEditor, Gson gson){
        PlaceMovement movementUpload= new PlaceMovement();
        movementUpload.setMovementType(type);
        OffsetDateTime dateTime= OffsetDateTime.now();
        movementUpload.setTimestamp(dateTime);
        movementUpload.setPlaceId(placeId);
        if(authServerID != null)
            movementUpload.setOrgAuthServerId(authServerID);
        if(type == -1)
            movementUpload.setExitToken(exitToken);
        ApiClient ac = new ApiClient("bearerAuth").setBearerToken(userToken);
        MovementApi service= ac.createService(MovementApi.class);
        Call<PlaceMovement> movement= service.trackMovementInPlace(movementUpload);
        movement.enqueue(new Callback<PlaceMovement>() {
            @Override
            public void onResponse(Call<PlaceMovement> call, Response<PlaceMovement> response) {

                if(type==1 && response.code() == 201 ){
                    movementUpload.setExitToken(response.body().getExitToken());
                    String placeMovementJson = gson.toJson(movementUpload);
                    prefEditor.putString("placeMovement",placeMovementJson);
                    prefEditor.commit();
                }
                else if(type==-1 && response.code() == 202){
                    //serialize in local the object List<PlaceAccess>.
                    String placeAccessJson = gson.toJson(placeAccess);
                    prefEditor.putString("placeAccess",placeAccessJson);
                    prefEditor.commit();
                }

            }

            @Override
            public void onFailure(Call<PlaceMovement> call, Throwable t) {

            }
        });

    }


    //Returns the list of all organizations.
    public void performDownloadOrganizationListServer(String path, String userToken)  {

        ArrayList<Organization> returnList = new ArrayList<>();
        ApiClient ac = new ApiClient("bearerAuth").setBearerToken(userToken);
        OrganizationApi service = ac.createService(OrganizationApi.class);
        Call<List<Organization>> orgList = service.getOrganizationList();
        orgList.enqueue(new Callback<List<Organization>>() {
            @Override
            public void onResponse(@NotNull Call<List<Organization>> call, @NotNull Response<List<Organization>> response) {

                if(response.body()!=null && response.code() == 200){

                    for(int i=0; i<response.body().size(); i++){

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
                        o.setTrackingMode(response.body().get(i).getTrackingMode().toString());

                        if(response.body().get(i).getTrackingMode().getValue() == "authenticated"){
                            o.setAuthenticationServerURL(response.body().get(i).getAuthenticationServerURL());
                        }
                        returnList.add(o);
                    }

                    try {
                        Storage save = new Storage(null,null);
                        save.performUpdateFile(returnList,path);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                    homeListener.onSuccessDownload("Lista scaricata con successo");
                }
            }

            @Override
            public void onFailure(Call<List<Organization>> call, Throwable t) {
                homeListener.onFailureDownload("Errore durante lo scaricamento della lista " + t.getMessage());
            }});
    }



}