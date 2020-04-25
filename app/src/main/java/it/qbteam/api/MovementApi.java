package it.qbteam.api;

import it.qbteam.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody;

import it.qbteam.model.OrganizationMovement;
import it.qbteam.model.PlaceMovement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
