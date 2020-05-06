package it.qbteam.stalkerapp.model.backend.api;


import retrofit2.Call;
import retrofit2.http.*;

import it.qbteam.stalkerapp.model.backend.modelBackend.OrganizationMovement;
import it.qbteam.stalkerapp.model.backend.modelBackend.PlaceMovement;

public interface MovementApi {
  /**
   * Tracks the user movement inside the trackingArea of an organization.
   * Tracks the user movement inside the trackingArea of an organization.
   * @param organizationMovement  (required)
   * @return Call&lt;OrganizationMovement&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })

  @POST("movement/track/organization")
  Call<OrganizationMovement> trackMovementInOrganization(
    @retrofit2.http.Body OrganizationMovement organizationMovement
  );

  /**
   * Tracks the user movement inside the trackingArea of a place of an organization.
   * Tracks the user movement inside the trackingArea of a place of an organization.
   * @param placeMovement  (required)
   * @return Call&lt;PlaceMovement&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("movement/track/place")
  Call<PlaceMovement> trackMovementInPlace(
    @retrofit2.http.Body PlaceMovement placeMovement
  );

}
