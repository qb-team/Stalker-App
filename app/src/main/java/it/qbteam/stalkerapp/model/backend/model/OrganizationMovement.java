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


package it.qbteam.stalkerapp.model.backend.model;

import java.util.Objects;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import javax.validation.constraints.*;
import javax.validation.Valid;

/**
 * Movement in an organization.
 */
@ApiModel(description = "Movement in an organization.")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2020-05-02T17:39:19.016+02:00[Europe/Rome]")
public class OrganizationMovement {
  public static final String SERIALIZED_NAME_EXIT_TOKEN = "exitToken";
  @SerializedName(SERIALIZED_NAME_EXIT_TOKEN)
  private String exitToken;

  public static final String SERIALIZED_NAME_TIMESTAMP = "timestamp";
  @SerializedName(SERIALIZED_NAME_TIMESTAMP)
  private OffsetDateTime timestamp;

  public static final String SERIALIZED_NAME_MOVEMENT_TYPE = "movementType";
  @SerializedName(SERIALIZED_NAME_MOVEMENT_TYPE)
  private Integer movementType;

  public static final String SERIALIZED_NAME_ORGANIZATION_ID = "organizationId";
  @SerializedName(SERIALIZED_NAME_ORGANIZATION_ID)
  private Long organizationId;

  public static final String SERIALIZED_NAME_ORG_AUTH_SERVER_ID = "orgAuthServerId";
  @SerializedName(SERIALIZED_NAME_ORG_AUTH_SERVER_ID)
  private String orgAuthServerId;


  public OrganizationMovement exitToken(String exitToken) {

    this.exitToken = exitToken;
    return this;
  }

  /**
   * Token generated by the server when the user entered in the organization. It is required only to track an exit movement. If this property is not empty in an entrance movement it will not be considered.
   * @return exitToken
   **/
  @javax.annotation.Nullable
  @Size(max=256)  @ApiModelProperty(value = "Token generated by the server when the user entered in the organization. It is required only to track an exit movement. If this property is not empty in an entrance movement it will not be considered.")

  public String getExitToken() {
    return exitToken;
  }


  public void setExitToken(String exitToken) {
    this.exitToken = exitToken;
  }


  public OrganizationMovement timestamp(OffsetDateTime timestamp) {

    this.timestamp = timestamp;
    return this;
  }

  /**
   * Date and time of the moment in which the user entered the place.
   * @return timestamp
   **/
  @NotNull
  @Valid
  @ApiModelProperty(required = true, value = "Date and time of the moment in which the user entered the place.")

  public OffsetDateTime getTimestamp() {
    return timestamp;
  }


  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }


  public OrganizationMovement movementType(Integer movementType) {

    this.movementType = movementType;
    return this;
  }

  /**
   * Type of movement. It can be: - Entrance: 1 - Exit: -1
   * minimum: -1
   * maximum: 1
   * @return movementType
   **/
  @NotNull
  @Min(-1) @Max(1)  @ApiModelProperty(required = true, value = "Type of movement. It can be: - Entrance: 1 - Exit: -1")

  public Integer getMovementType() {
    return movementType;
  }


  public void setMovementType(Integer movementType) {
    this.movementType = movementType;
  }


  public OrganizationMovement organizationId(Long organizationId) {

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


  public OrganizationMovement orgAuthServerId(String orgAuthServerId) {

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
    OrganizationMovement organizationMovement = (OrganizationMovement) o;
    return Objects.equals(this.exitToken, organizationMovement.exitToken) &&
            Objects.equals(this.timestamp, organizationMovement.timestamp) &&
            Objects.equals(this.movementType, organizationMovement.movementType) &&
            Objects.equals(this.organizationId, organizationMovement.organizationId) &&
            Objects.equals(this.orgAuthServerId, organizationMovement.orgAuthServerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(exitToken, timestamp, movementType, organizationId, orgAuthServerId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationMovement {\n");
    sb.append("    exitToken: ").append(toIndentedString(exitToken)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    movementType: ").append(toIndentedString(movementType)).append("\n");
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