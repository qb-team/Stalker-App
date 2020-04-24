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
 * Access to an organization.
 **/
@ApiModel(description = "Access to an organization.")
public class OrganizationAccess {
  
  @SerializedName("id")
  private Long id = null;
  @SerializedName("entranceTimestamp")
  private Date entranceTimestamp = null;
  @SerializedName("exitTimestamp")
  private Date exitTimestamp = null;
  @SerializedName("organizationId")
  private Long organizationId = null;
  @SerializedName("orgAuthServerId")
  private String orgAuthServerId = null;

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
   * Date and time of the moment in which the user entered the place.
   **/
  @ApiModelProperty(required = true, value = "Date and time of the moment in which the user entered the place.")
  public Date getEntranceTimestamp() {
    return entranceTimestamp;
  }
  public void setEntranceTimestamp(Date entranceTimestamp) {
    this.entranceTimestamp = entranceTimestamp;
  }

  /**
   * Date and time of the moment in which the user left the place.
   **/
  @ApiModelProperty(value = "Date and time of the moment in which the user left the place.")
  public Date getExitTimestamp() {
    return exitTimestamp;
  }
  public void setExitTimestamp(Date exitTimestamp) {
    this.exitTimestamp = exitTimestamp;
  }

  /**
   * Unique identifier of the organization in which the user had access.
   **/
  @ApiModelProperty(required = true, value = "Unique identifier of the organization in which the user had access.")
  public Long getOrganizationId() {
    return organizationId;
  }
  public void setOrganizationId(Long organizationId) {
    this.organizationId = organizationId;
  }

  /**
   * User unique identifier from the authentication server of the organization.
   **/
  @ApiModelProperty(value = "User unique identifier from the authentication server of the organization.")
  public String getOrgAuthServerId() {
    return orgAuthServerId;
  }
  public void setOrgAuthServerId(String orgAuthServerId) {
    this.orgAuthServerId = orgAuthServerId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrganizationAccess organizationAccess = (OrganizationAccess) o;
    return (this.id == null ? organizationAccess.id == null : this.id.equals(organizationAccess.id)) &&
        (this.entranceTimestamp == null ? organizationAccess.entranceTimestamp == null : this.entranceTimestamp.equals(organizationAccess.entranceTimestamp)) &&
        (this.exitTimestamp == null ? organizationAccess.exitTimestamp == null : this.exitTimestamp.equals(organizationAccess.exitTimestamp)) &&
        (this.organizationId == null ? organizationAccess.organizationId == null : this.organizationId.equals(organizationAccess.organizationId)) &&
        (this.orgAuthServerId == null ? organizationAccess.orgAuthServerId == null : this.orgAuthServerId.equals(organizationAccess.orgAuthServerId));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.id == null ? 0: this.id.hashCode());
    result = 31 * result + (this.entranceTimestamp == null ? 0: this.entranceTimestamp.hashCode());
    result = 31 * result + (this.exitTimestamp == null ? 0: this.exitTimestamp.hashCode());
    result = 31 * result + (this.organizationId == null ? 0: this.organizationId.hashCode());
    result = 31 * result + (this.orgAuthServerId == null ? 0: this.orgAuthServerId.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationAccess {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  entranceTimestamp: ").append(entranceTimestamp).append("\n");
    sb.append("  exitTimestamp: ").append(exitTimestamp).append("\n");
    sb.append("  organizationId: ").append(organizationId).append("\n");
    sb.append("  orgAuthServerId: ").append(orgAuthServerId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
