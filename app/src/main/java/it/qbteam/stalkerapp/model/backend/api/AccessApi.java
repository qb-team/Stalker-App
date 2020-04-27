package it.qbteam.stalkerapp.model.backend.api;

import it.qbteam.stalkerapp.model.backend.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;
import it.qbteam.stalkerapp.model.backend.model.OrganizationAccess;
import it.qbteam.stalkerapp.model.backend.model.PlaceAccess;
import java.util.List;

public interface AccessApi {
  /**
   * Returns all the anonymous accesses in an organization registered of the user owning the exitTokens (exitTokens are separated by commas).
   * Returns all the anonymous accesses in an organization registered of the user owning the exitTokens (exitTokens are separated by commas) that are fully registered. Fully registered means that there are both the entrance and the exit timestamp. Only app users can access this end-point.
   * @param exitTokens One or more exitTokens. (required)
   * @param organizationId ID of an organization. (required)
   * @return Call&lt;List&lt;OrganizationAccess&gt;&gt;
   */
  @GET("access/organization/{organizationId}/anonymous/{exitTokens}")
  Call<List<OrganizationAccess>> getAnonymousAccessListInOrganization(
    @retrofit2.http.Path("exitTokens") List<String> exitTokens, @retrofit2.http.Path("organizationId") Long organizationId
  );

  /**
   * Returns all the anonymous accesses in a place registered of the user owning the exitTokens (exitTokens are separated by commas).
   * Returns all the anonymous accesses in a place registered of the user owning the exitTokens (exitTokens are separated by commas) that are fully registered. Fully registered means that there are both the entrance and the exit timestamp. Only app users can access this end-point.
   * @param exitTokens One or more exitTokens. (required)
   * @param placeId ID of a place. (required)
   * @return Call&lt;List&lt;PlaceAccess&gt;&gt;
   */
  @GET("access/place/{placeId}/anonymous/{exitTokens}")
  Call<List<PlaceAccess>> getAnonymousAccessListInPlace(
    @retrofit2.http.Path("exitTokens") List<String> exitTokens, @retrofit2.http.Path("placeId") Long placeId
  );

  /**
   * Returns all the authenticated accesses in an organization registered of one or more users (orgAuthServerIds are separated by commas).
   * Returns all the authenticated accesses in an organization registered of one or more users (orgAuthServerIds are separated by commas) that are fully registered. Fully registered means that there are both the entrance and the exit timestamp. Both app users and web-app administrators can access this end-point.
   * @param orgAuthServerIds One or more orgAuthServerIds. If it is called by the app user, the orgAuthServerIds parameter can only consist in one identifier. Otherwise it can be more than one identifier. (required)
   * @param organizationId ID of an organization (required)
   * @return Call&lt;List&lt;OrganizationAccess&gt;&gt;
   */
  @GET("access/organization/{organizationId}/authenticated/{orgAuthServerIds}")
  Call<List<OrganizationAccess>> getAuthenticatedAccessListInOrganization(
    @retrofit2.http.Path("orgAuthServerIds") List<String> orgAuthServerIds, @retrofit2.http.Path("organizationId") Long organizationId
  );

  /**
   * Returns all the authenticated accesses in a place registered of one or more users (orgAuthServerIds are separated by commas).
   * Returns all the authenticated accesses in a place registered of one or more users (orgAuthServerIds are separated by commas) that are fully registered. Fully registered means that there are both the entrance and the exit timestamp. Both app users and web-app administrators can access this end-point.
   * @param orgAuthServerIds One or more orgAuthServerIds. If it is called by the app user, the orgAuthServerIds parameter can only consist in one identifier. Otherwise it can be more than one identifier. (required)
   * @param placeId ID of a place. (required)
   * @return Call&lt;List&lt;PlaceAccess&gt;&gt;
   */
  @GET("access/place/{placeId}/authenticated/{orgAuthServerIds}")
  Call<List<PlaceAccess>> getAuthenticatedAccessListInPlace(
    @retrofit2.http.Path("orgAuthServerIds") List<String> orgAuthServerIds, @retrofit2.http.Path("placeId") Long placeId
  );

}
