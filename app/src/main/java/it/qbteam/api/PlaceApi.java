package it.qbteam.api;

import it.qbteam.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody;

import it.qbteam.model.Place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
