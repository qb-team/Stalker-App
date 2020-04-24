/**
 * Stalker API
 * API di Stalker di Imola Informatica sviluppato da qbteam
 *
 * The version of the OpenAPI document: 1.0.0
 * Contact: qbteamswe@gmail.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package it.qbteam.api;

import it.qbteam.ApiInvoker;
import it.qbteam.ApiException;
import it.qbteam.Pair;

import it.qbteam.model.*;

import java.util.*;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import it.qbteam.model.TimePerUserReport;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class ReportApi {
  String basePath = "http://localhost:8080";
  ApiInvoker apiInvoker = ApiInvoker.getInstance();

  public void addHeader(String key, String value) {
    getInvoker().addDefaultHeader(key, value);
  }

  public ApiInvoker getInvoker() {
    return apiInvoker;
  }

  public void setBasePath(String basePath) {
    this.basePath = basePath;
  }

  public String getBasePath() {
    return basePath;
  }

  /**
  * Gets the report of total time spent per user inside the organization.
  * Gets the report of total time spent by each user inside the organization. Only web-app admininistrators can access this end-point.
   * @param organizationId ID of the organization. The viewer admininistrator must have permissions for this organization.
   * @return List<TimePerUserReport>
  */
  public List<TimePerUserReport> getTimePerUserReport (Long organizationId) throws TimeoutException, ExecutionException, InterruptedException, ApiException {
    Object postBody = null;
    // verify the required parameter 'organizationId' is set
    if (organizationId == null) {
      VolleyError error = new VolleyError("Missing the required parameter 'organizationId' when calling getTimePerUserReport",
        new ApiException(400, "Missing the required parameter 'organizationId' when calling getTimePerUserReport"));
    }

    // create path and map variables
    String path = "/report/organization/{organizationId}".replaceAll("\\{" + "organizationId" + "\\}", apiInvoker.escapeString(organizationId.toString()));

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();
    String[] contentTypes = {
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      HttpEntity httpEntity = localVarBuilder.build();
      postBody = httpEntity;
    } else {
      // normal form params
    }

    String[] authNames = new String[] { "bearerAuth" };

    try {
      String localVarResponse = apiInvoker.invokeAPI (basePath, path, "GET", queryParams, postBody, headerParams, formParams, contentType, authNames);
      if (localVarResponse != null) {
         return (List<TimePerUserReport>) ApiInvoker.deserialize(localVarResponse, "array", TimePerUserReport.class);
      } else {
         return null;
      }
    } catch (ApiException ex) {
       throw ex;
    } catch (InterruptedException ex) {
       throw ex;
    } catch (ExecutionException ex) {
      if (ex.getCause() instanceof VolleyError) {
        VolleyError volleyError = (VolleyError)ex.getCause();
        if (volleyError.networkResponse != null) {
          throw new ApiException(volleyError.networkResponse.statusCode, volleyError.getMessage());
        }
      }
      throw ex;
    } catch (TimeoutException ex) {
      throw ex;
    }
  }

      /**
   * Gets the report of total time spent per user inside the organization.
   * Gets the report of total time spent by each user inside the organization. Only web-app admininistrators can access this end-point.
   * @param organizationId ID of the organization. The viewer admininistrator must have permissions for this organization.
  */
  public void getTimePerUserReport (Long organizationId, final Response.Listener<List<TimePerUserReport>> responseListener, final Response.ErrorListener errorListener) {
    Object postBody = null;

    // verify the required parameter 'organizationId' is set
    if (organizationId == null) {
      VolleyError error = new VolleyError("Missing the required parameter 'organizationId' when calling getTimePerUserReport",
        new ApiException(400, "Missing the required parameter 'organizationId' when calling getTimePerUserReport"));
    }

    // create path and map variables
    String path = "/report/organization/{organizationId}".replaceAll("\\{format\\}","json").replaceAll("\\{" + "organizationId" + "\\}", apiInvoker.escapeString(organizationId.toString()));

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();



    String[] contentTypes = {
      
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      

      HttpEntity httpEntity = localVarBuilder.build();
      postBody = httpEntity;
    } else {
      // normal form params
          }

    String[] authNames = new String[] { "bearerAuth" };

    try {
      apiInvoker.invokeAPI(basePath, path, "GET", queryParams, postBody, headerParams, formParams, contentType, authNames,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String localVarResponse) {
            try {
              responseListener.onResponse((List<TimePerUserReport>) ApiInvoker.deserialize(localVarResponse,  "array", TimePerUserReport.class));
            } catch (ApiException exception) {
               errorListener.onErrorResponse(new VolleyError(exception));
            }
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            errorListener.onErrorResponse(error);
          }
      });
    } catch (ApiException ex) {
      errorListener.onErrorResponse(new VolleyError(ex));
    }
  }
}