/*
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


package it.qbteam.stalkerapp.model.backend.dataBackend;

import java.io.Serializable;
import java.util.Objects;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import javax.validation.constraints.*;
import javax.validation.Valid;

/**
 * Access to an organization.
 */
@ApiModel(description = "Access to an organization.")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2020-05-02T17:39:19.016+02:00[Europe/Rome]")
public class OrganizationAccess implements Serializable {
  public static final String SERIALIZED_NAME_ID = "id";
  @SerializedName(SERIALIZED_NAME_ID)
  private Long id;

  public static final String SERIALIZED_NAME_ENTRANCE_TIMESTAMP = "entranceTimestamp";
  @SerializedName(SERIALIZED_NAME_ENTRANCE_TIMESTAMP)
  private OffsetDateTime entranceTimestamp;

  public static final String SERIALIZED_NAME_EXIT_TIMESTAMP = "exitTimestamp";
  @SerializedName(SERIALIZED_NAME_EXIT_TIMESTAMP)
  private OffsetDateTime exitTimestamp;

  public static final String SERIALIZED_NAME_EXIT_TOKEN = "exitToken";
  @SerializedName(SERIALIZED_NAME_EXIT_TOKEN)
  private String exitToken;

  public static final String SERIALIZED_NAME_ORGANIZATION_ID = "organizationId";
  @SerializedName(SERIALIZED_NAME_ORGANIZATION_ID)
  private Long organizationId;

  public static final String SERIALIZED_NAME_ORG_AUTH_SERVER_ID = "orgAuthServerId";
  @SerializedName(SERIALIZED_NAME_ORG_AUTH_SERVER_ID)
  private String orgAuthServerId;

  public static final String SERIALIZED_NAME_ORG= "orgName";
  @SerializedName(SERIALIZED_NAME_ORG)
  private String orgName;

  public static final String SERIALIZED_ORG_TIME_STAY= "timeStay";
  @SerializedName(SERIALIZED_ORG_TIME_STAY)
  private Long timeStay;

  public static final String SERIALIZED_ACCESS_TYPE= "accessType";
  @SerializedName(SERIALIZED_ACCESS_TYPE)
  private String accessType;

  public void setAccessType(String accessType){this.accessType=accessType;}

  public String getAccessType(){return accessType;}

  public void setTimeStay(Long timeStay){
    this.timeStay=timeStay;
  }

  public Long getTimeStay(){
    return timeStay;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  public String getOrgName() {
    return orgName;
  }

  public OrganizationAccess id(Long id) {

    this.id = id;
    return this;
  }

  /**
   * Get id
   * minimum: 1
   * @return id
   **/
  @NotNull
  @Min(1L)  @ApiModelProperty(required = true, value = "")

  public Long getId() {
    return id;
  }


  public void setId(Long id) {
    this.id = id;
  }


  public OrganizationAccess entranceTimestamp(OffsetDateTime entranceTimestamp) {

    this.entranceTimestamp = entranceTimestamp;
    return this;
  }

  /**
   * Date and time of the moment in which the user entered the place.
   * @return entranceTimestamp
   **/
  @NotNull
  @Valid
  @ApiModelProperty(required = true, value = "Date and time of the moment in which the user entered the place.")

  public OffsetDateTime getEntranceTimestamp() {
    return entranceTimestamp;
  }


  public void setEntranceTimestamp(OffsetDateTime entranceTimestamp) {
    this.entranceTimestamp = entranceTimestamp;
  }


  public OrganizationAccess exitTimestamp(OffsetDateTime exitTimestamp) {

    this.exitTimestamp = exitTimestamp;
    return this;
  }

  /**
   * Date and time of the moment in which the user left the place.
   * @return exitTimestamp
   **/
  @javax.annotation.Nullable
  @Valid
  @ApiModelProperty(value = "Date and time of the moment in which the user left the place.")

  public OffsetDateTime getExitTimestamp() {
    return exitTimestamp;
  }


  public void setExitTimestamp(OffsetDateTime exitTimestamp) {
    this.exitTimestamp = exitTimestamp;
  }


  public OrganizationAccess exitToken(String exitToken) {

    this.exitToken = exitToken;
    return this;
  }

  /**
   * Token generated by the server required for registering the user exit movement.
   * @return exitToken
   **/
  @NotNull
  @Size(max=256)  @ApiModelProperty(required = true, value = "Token generated by the server required for registering the user exit movement.")

  public String getExitToken() {
    return exitToken;
  }


  public void setExitToken(String exitToken) {
    this.exitToken = exitToken;
  }


  public OrganizationAccess organizationId(Long organizationId) {

    this.organizationId = organizationId;
    return this;
  }

  /**
   * Unique identifier of the organization in which the user had access.
   * minimum: 1
   * @return organizationId
   **/
  @NotNull
  @Min(1L)  @ApiModelProperty(required = true, value = "Unique identifier of the organization in which the user had access.")

  public Long getOrganizationId() {
    return organizationId;
  }


  public void setOrganizationId(Long organizationId) {
    this.organizationId = organizationId;
  }


  public OrganizationAccess orgAuthServerId(String orgAuthServerId) {

    this.orgAuthServerId = orgAuthServerId;
    return this;
  }

  /**
   * User unique identifier from the authentication server of the organization.
   * @return orgAuthServerId
   **/
  @javax.annotation.Nullable
  @Size(max=256)  @ApiModelProperty(value = "User unique identifier from the authentication server of the organization.")

  public String getOrgAuthServerId() {
    return orgAuthServerId;
  }


  public void setOrgAuthServerId(String orgAuthServerId) {
    this.orgAuthServerId = orgAuthServerId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrganizationAccess organizationAccess = (OrganizationAccess) o;
    return Objects.equals(this.id, organizationAccess.id) &&
            Objects.equals(this.entranceTimestamp, organizationAccess.entranceTimestamp) &&
            Objects.equals(this.exitTimestamp, organizationAccess.exitTimestamp) &&
            Objects.equals(this.exitToken, organizationAccess.exitToken) &&
            Objects.equals(this.organizationId, organizationAccess.organizationId) &&
            Objects.equals(this.orgAuthServerId, organizationAccess.orgAuthServerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, entranceTimestamp, exitTimestamp, exitToken, organizationId, orgAuthServerId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationAccess {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    entranceTimestamp: ").append(toIndentedString(entranceTimestamp)).append("\n");
    sb.append("    exitTimestamp: ").append(toIndentedString(exitTimestamp)).append("\n");
    sb.append("    exitToken: ").append(toIndentedString(exitToken)).append("\n");
    sb.append("    organizationId: ").append(toIndentedString(organizationId)).append("\n");
    sb.append("    orgAuthServerId: ").append(toIndentedString(orgAuthServerId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
