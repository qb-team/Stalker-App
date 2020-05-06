package it.qbteam.stalkerapp.model.backend.api;


import retrofit2.Call;
import retrofit2.http.*;

import it.qbteam.stalkerapp.model.backend.modelBackend.Place;

import java.util.List;

public interface PlaceApi {

  /**
   * Returns the list of places of the organization.
   * Returns the list of places of the organization. Both app users and web-app administrators can access this end-point.
   * @param organizationId ID of an organization. (required)
   * @return Call&lt;List&lt;Place&gt;&gt;
   */
  @GET("place/organization/{organizationId}")
  Call<List<Place>> getPlaceListOfOrganization(
    @retrofit2.http.Path("organizationId") Long organizationId
  );



}
