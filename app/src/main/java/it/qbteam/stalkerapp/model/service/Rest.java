package it.qbteam.stalkerapp.model.service;

import org.json.JSONException;
import java.io.IOException;
import it.qbteam.stalkerapp.model.backend.ApiClient;
import it.qbteam.stalkerapp.model.backend.api.FavoriteApi;
import it.qbteam.stalkerapp.model.backend.model.Favorite;
import it.qbteam.stalkerapp.model.backend.model.Organization;
import it.qbteam.stalkerapp.model.data.User;
import it.qbteam.stalkerapp.presenter.MyStalkersListContract;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Rest{

    MyStalkersListContract.MyStalkerListener myStalkerListener;

    public Rest (MyStalkersListContract.MyStalkerListener myStalkerListener){
        this.myStalkerListener=myStalkerListener;
    }

    public void performRemoveOrganizationRest(Organization organization, User user){

        Favorite favoriteUpload = new Favorite();
        favoriteUpload.setUserId(user.getToken());
        favoriteUpload.setOrganizationId(organization.getId());

        ApiClient ac = new ApiClient("bearerAuth").setBearerToken(user.getToken());
        FavoriteApi service = ac.createService(FavoriteApi.class);
        Call<Void> favorite = service.removeFavoriteOrganization(favoriteUpload);
        favorite.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(response.code());
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("ERROREEEEEEEEEEEEEEEEEE");
            }
        });
    }

    public void performAddOrganizationRest(Organization organization, User user) throws IOException, JSONException{

        Favorite favoriteUpload = new Favorite();
        favoriteUpload.setUserId(user.getToken());
        favoriteUpload.setOrganizationId(organization.getId());
        favoriteUpload.setCreationDate(organization.getCreationDate());

        ApiClient ac = new ApiClient("bearerAuth").setBearerToken(user.getToken());
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
                System.out.println("ERROREEEEEEEEEEEEEEEEEE");
            }
        });
    }
}
