package it.qbteam.stalkerapp.model.service;

import org.json.JSONException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import it.qbteam.stalkerapp.model.backend.ApiClient;
import it.qbteam.stalkerapp.model.backend.api.FavoriteApi;
import it.qbteam.stalkerapp.model.backend.api.MovementApi;
import it.qbteam.stalkerapp.model.backend.model.Favorite;
import it.qbteam.stalkerapp.model.backend.model.Organization;
import it.qbteam.stalkerapp.model.backend.model.OrganizationMovement;
import it.qbteam.stalkerapp.model.backend.model.PlaceMovement;
import it.qbteam.stalkerapp.model.data.User;
import it.qbteam.stalkerapp.presenter.MyStalkersListContract;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Rest {

    MyStalkersListContract.MyStalkerListener myStalkerListener;

    public Rest(MyStalkersListContract.MyStalkerListener myStalkerListener) {
        this.myStalkerListener = myStalkerListener;
    }

    public void performRemoveOrganizationRest(Organization organization, String UID, String userToken) {

        Favorite favoriteUpload = new Favorite();
        favoriteUpload.setUserId(UID);
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
    public void performLoadList(String UID, String userToken){

        Favorite favoriteDownload= new Favorite();
        favoriteDownload.setUserId(UID);
        ApiClient ac =new ApiClient("bearerAuth").setBearerToken(userToken);
        FavoriteApi service = ac.createService(FavoriteApi.class);
        Call<List<Organization>> favorite= service.getFavoriteOrganizationList(favoriteDownload.getUserId());
        favorite.enqueue(new Callback<List<Organization>>() {
            @Override
            public void onResponse(Call<List<Organization>> call, Response<List<Organization>> response) {

                myStalkerListener.onSuccessLoad(response.body());
             System.out.println("RISPOSTA LOAD: "+response.body());
            }

            @Override
            public void onFailure(Call<List<Organization>> call, Throwable t) {
                System.out.println("ERRORE LOAD");
            }
        });

    }
    public void performAddOrganizationRest(Organization organization, String UID, String userToken) throws IOException, JSONException {

        Favorite favoriteUpload = new Favorite();
        favoriteUpload.setUserId(UID);
        favoriteUpload.setOrganizationId(organization.getId());
        favoriteUpload.setCreationDate(organization.getCreationDate());
        ApiClient ac = new ApiClient("bearerAuth").setBearerToken(userToken);
        FavoriteApi service = ac.createService(FavoriteApi.class);
        Call<Favorite> favorite = service.addFavoriteOrganization(favoriteUpload);
        favorite.enqueue(new Callback<Favorite>() {
            @Override
            public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                //myStalkerListener.onSuccessAddRest();
                System.out.println(response.code());
            }

            @Override
            public void onFailure(Call<Favorite> call, Throwable t) {
                System.out.println("Errore durante l'aggiunta dell'organizzazione");
            }
        });
    }


    public static void performMovement(String name , String authServerID,OffsetDateTime timeStamp,long orgID, User user) {
        final String[] exitToken = new String[1];
        OrganizationMovement movementUpload= new OrganizationMovement();
        movementUpload.setMovementType(1);
        OffsetDateTime dateTime= OffsetDateTime.now();
        movementUpload.setTimestamp(dateTime);
        movementUpload.setOrganizationId(orgID);
        movementUpload.setOrgAuthServerId(authServerID);
        ApiClient ac = new ApiClient("bearerAuth").setBearerToken(user.getToken());
        MovementApi service = ac.createService(MovementApi.class);
        Call<OrganizationMovement> movement= service.trackMovementInOrganization(movementUpload);
        movement.enqueue(new Callback<OrganizationMovement>() {
                @Override
                public void onResponse(Call<OrganizationMovement> call, Response<OrganizationMovement> response) {

                    System.out.println(response.body().getExitToken());
                    System.out.println("INVIATO AL SERVER ENTRATA IN UNA ORGANIZZAZIONE");
                }

                @Override
                public void onFailure(Call<OrganizationMovement> call, Throwable t) {

                }
        });


    }
}