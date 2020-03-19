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

package it.qbteam.model;

import java.util.Date;
import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;

/**
 * Movement in a place of an organization made with the authenticated trackingMode.
 **/
@ApiModel(description = "Movement in a place of an organization made with the authenticated trackingMode.")
public class PlaceAuthenticatedMovement extends Movement {
  
  @SerializedName("id")
  private Long id = null;
  @SerializedName("movementDiscriminator")
  private String movementDiscriminator = null;
  @SerializedName("timestamp")
  private Date timestamp = null;
  @SerializedName("movementType")
  private MovementTypeEnum movementType = null;
  @SerializedName("placeId")
  private Long placeId = null;
  @SerializedName("ldapId")
  private Long ldapId = null;

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getMovementDiscriminator() {
    return movementDiscriminator;
  }
  public void setMovementDiscriminator(String movementDiscriminator) {
    this.movementDiscriminator = movementDiscriminator;
  }

  /**
   * Date and time of the moment in which the user entered the place.
   **/
  @ApiModelProperty(required = true, value = "Date and time of the moment in which the user entered the place.")
  public Date getTimestamp() {
    return timestamp;
  }
  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Type of movement.
   **/
  @ApiModelProperty(required = true, value = "Type of movement.")
  public MovementTypeEnum getMovementType() {
    return movementType;
  }
  public void setMovementType(MovementTypeEnum movementType) {
    this.movementType = movementType;
  }

  /**
   * Unique identifier of the place in which the user had access.
   **/
  @ApiModelProperty(required = true, value = "Unique identifier of the place in which the user had access.")
  public Long getPlaceId() {
    return placeId;
  }
  public void setPlaceId(Long placeId) {
    this.placeId = placeId;
  }

  /**
   * Organization LDAP server's user unique identifier.
   **/
  @ApiModelProperty(required = true, value = "Organization LDAP server's user unique identifier.")
  public Long getLdapId() {
    return ldapId;
  }
  public void setLdapId(Long ldapId) {
    this.ldapId = ldapId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlaceAuthenticatedMovement placeAuthenticatedMovement = (PlaceAuthenticatedMovement) o;
    return (this.id == null ? placeAuthenticatedMovement.id == null : this.id.equals(placeAuthenticatedMovement.id)) &&
        (this.movementDiscriminator == null ? placeAuthenticatedMovement.movementDiscriminator == null : this.movementDiscriminator.equals(placeAuthenticatedMovement.movementDiscriminator)) &&
        (this.timestamp == null ? placeAuthenticatedMovement.timestamp == null : this.timestamp.equals(placeAuthenticatedMovement.timestamp)) &&
        (this.movementType == null ? placeAuthenticatedMovement.movementType == null : this.movementType.equals(placeAuthenticatedMovement.movementType)) &&
        (this.placeId == null ? placeAuthenticatedMovement.placeId == null : this.placeId.equals(placeAuthenticatedMovement.placeId)) &&
        (this.ldapId == null ? placeAuthenticatedMovement.ldapId == null : this.ldapId.equals(placeAuthenticatedMovement.ldapId));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.id == null ? 0: this.id.hashCode());
    result = 31 * result + (this.movementDiscriminator == null ? 0: this.movementDiscriminator.hashCode());
    result = 31 * result + (this.timestamp == null ? 0: this.timestamp.hashCode());
    result = 31 * result + (this.movementType == null ? 0: this.movementType.hashCode());
    result = 31 * result + (this.placeId == null ? 0: this.placeId.hashCode());
    result = 31 * result + (this.ldapId == null ? 0: this.ldapId.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlaceAuthenticatedMovement {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  movementDiscriminator: ").append(movementDiscriminator).append("\n");
    sb.append("  timestamp: ").append(timestamp).append("\n");
    sb.append("  movementType: ").append(movementType).append("\n");
    sb.append("  placeId: ").append(placeId).append("\n");
    sb.append("  ldapId: ").append(ldapId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
