package it.qbteam.stalkerapp.model.backend.api;

import retrofit2.Call;
import retrofit2.http.*;

import it.qbteam.stalkerapp.model.backend.modelBackend.Favorite;
import it.qbteam.stalkerapp.model.backend.modelBackend.Organization;

import java.util.List;

public interface FavoriteApi {
  /**
   * Adds a new organization to the user&#39;s favorite organization list.
   * Adds a new organization to the user&#39;s favorite organization list. If the organization has trackingMode: authenticated, then the user account of the organization must be linked to Stalker&#39;s account. Only app users can access this end-point.
   * @param favorite  (required)
   * @return Call&lt;Favorite&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("favorite/addfavorite")
  Call<Favorite> addFavoriteOrganization(
    @retrofit2.http.Body Favorite favorite
  );

  /**
   * Gets the list of favorite organizations of a user.
   * Gets the list of favorite organizations of a user.  Only app users can access this end-point.
   * @param userId ID of the user. It must be the same of the userId of the authenticated user. (required)
   * @return Call&lt;List&lt;Organization&gt;&gt;
   */
  @GET("favorite/{userId}")
  Call<List<Organization>> getFavoriteOrganizationList(
    @retrofit2.http.Path("userId") String userId
  );

  /**
   * Removes the organization from the user&#39;s favorite organization list.
   * Removes the organization from the user&#39;s favorite organization list. Only app users can access this end-point.
   * @param favorite  (required)
   * @return Call&lt;Void&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("favorite/removefavorite")
  Call<Void> removeFavoriteOrganization(
    @retrofit2.http.Body Favorite favorite
  );
}
