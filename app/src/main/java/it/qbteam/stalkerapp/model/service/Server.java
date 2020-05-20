package it.qbteam.stalkerapp.model.service;

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
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;
import it.qbteam.stalkerapp.contract.HomeContract;
import it.qbteam.stalkerapp.contract.MyStalkersListContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.Place;
import it.qbteam.stalkerapp.model.backend.dataBackend.PlaceMovement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Server {

    MyStalkersListContract.MyStalkerListener myStalkerListener;
    HomeContract.HomeListener homeListener;

    //Server's constructor.
    public Server(MyStalkersListContract.MyStalkerListener myStalkerListener, HomeContract.HomeListener homeListener) {
        this.myStalkerListener = myStalkerListener;
        this.homeListener = homeListener;
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
                         System.out.println(response.code());

                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        System.out.println("Errore durante la rimozione dell'organizzazione");
                    }
            });

    }

    //Gets the list of favorite organizations of a user.
    public void performLoadListServer(String UID, String userToken) {

                Favorite favoriteDownload = new Favorite();
                favoriteDownload.setUserId(UID);
                ApiClient ac = new ApiClient("bearerAuth").setBearerToken(userToken);
                FavoriteApi service = ac.createService(FavoriteApi.class);
                Call<List<Organization>> favorite = service.getFavoriteOrganizationList(favoriteDownload.getUserId());
                favorite.enqueue(new Callback<List<Organization>>() {
                    @Override
                    public void onResponse(Call<List<Organization>> call, Response<List<Organization>> response) {
                        try {
                            myStalkerListener.onSuccessLoad(response.body());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("RISPOSTA LOAD: " + response.code());
                    }
                    @Override
                    public void onFailure(Call<List<Organization>> call, Throwable t) {
                        System.out.println("ERRORE LOAD");
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
                System.out.println(response.code());
            }
            @Override
            public void onFailure(Call<Favorite> call, Throwable t) {
                System.out.println("Errore durante l'aggiunta dell'organizzazione");
            }
        });

    }

    public static void performDownloadPlaceServer(Long orgID, String userToken)  {
        Place placeDownload = new Place();
        placeDownload.setOrganizationId(orgID);
        ApiClient ac = new ApiClient("bearerAuth").setBearerToken(userToken);
        PlaceApi service = ac.createService(PlaceApi.class);
        Call<List<Place>> place = service.getPlaceListOfOrganization(placeDownload.getOrganizationId());
        place.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {

                try {
                    Storage.serializePlaceInLocal(response.body());
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.print("Errore durante lo scaricamento dei luoghi dell'organizzazione ");
                }


            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                System.out.println("ERRORE PLACE");
            }
        });





    }

    //Tracks the user movement inside the trackingArea of an organization.
    public static void performMovementServer(String authServerID,long orgID,String userToken,int type,String exitToken) {

        OrganizationMovement movementUpload = new OrganizationMovement();
        movementUpload.setMovementType(type);
        OffsetDateTime dateTime= OffsetDateTime.now();
        movementUpload.setTimestamp(dateTime);
        movementUpload.setOrganizationId(orgID);
        if(authServerID != null)
            movementUpload.setOrgAuthServerId(authServerID);
        if(type == -1)
            movementUpload.setExitToken(exitToken);
        ApiClient ac = new ApiClient("bearerAuth").setBearerToken(userToken);
        MovementApi service = ac.createService(MovementApi.class);
        Call<OrganizationMovement> movement = service.trackMovementInOrganization(movementUpload);
        movement.enqueue(new Callback<OrganizationMovement>() {
            @Override
            public void onResponse(Call<OrganizationMovement> call, Response<OrganizationMovement> response) {
                System.out.println(response.code());
                try {
                    if(type==1){
                        movementUpload.setExitToken(response.body().getExitToken());
                        Storage.serializeMovementInLocal(movementUpload);
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
    public static void performPlaceMovementServer(String exitToken,int type, Long placeId, String authServerID, Long orgId, String userToken){
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
                System.out.println(response.code());
                try {
                    if(type==1){
                        movementUpload.setExitToken(response.body().getExitToken());
                        Storage.serializePlaceMovement(movementUpload);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<PlaceMovement> call, Throwable t) {

            }
        });

    }


    //Returns the list of all organizations.
    public void performDownloadFileServer(String path, String userToken)  {

        ArrayList<Organization> returnList = new ArrayList<>();
        ApiClient ac = new ApiClient("bearerAuth").setBearerToken(userToken);
        OrganizationApi service = ac.createService(OrganizationApi.class);
        Call<List<Organization>> orgList = service.getOrganizationList();
        orgList.enqueue(new Callback<List<Organization>>() {
            @Override
            public void onResponse(@NotNull Call<List<Organization>> call, @NotNull Response<List<Organization>> response) {

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
                    if(response.body().get(i).getTrackingMode().getValue()=="authenticated")
                        o.setAuthenticationServerURL(response.body().get(i).getAuthenticationServerURL());
                    returnList.add(o);
                }

                try {
                    Storage save = new Storage(null,null);
                    save.performUpdateFile(returnList,path);
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

}