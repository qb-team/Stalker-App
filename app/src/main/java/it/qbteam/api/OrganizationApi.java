package it.qbteam.api;



import retrofit2.Call;
import retrofit2.http.*;
import it.qbteam.model.OrganizationTommaso;
import java.util.List;


public interface OrganizationApi {
  /**
   * Gets the available data for a single organization.
   * Gets the data available for a single organization.  Both app users and web-app administrators can access this end-point but,  app users can request information for all the organizations while web-app  administrators can only for the organizations they have access to.
   * @param organizationId ID of an organization. (required)
   * @return Call&lt;Organization&gt;
   */
  @GET("organization/{organizationId}")
  Call<OrganizationTommaso> getOrganization(
    @retrofit2.http.Path("organizationId") Long organizationId
  );

  /**
   * Returns the list of all organizations.
   * Returns the list of all organizations available in the system. The list can be empty. Only app users can access this end-point.
   * @return Call&lt;List&lt;Organization&gt;&gt;
   */
  @GET("organization")
  Call<List<OrganizationTommaso>> getOrganizationList();

}
