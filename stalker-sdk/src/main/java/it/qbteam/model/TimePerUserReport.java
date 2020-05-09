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
 * Represents a row in the report generated for an user by the viewer administrator.
 **/
@ApiModel(description = "Represents a row in the report generated for an user by the viewer administrator.")
public class TimePerUserReport {
  
  @SerializedName("organizationId")
  private Long organizationId = null;
  @SerializedName("orgAuthServerId")
  private String orgAuthServerId = null;
  @SerializedName("totalTime")
  private Date totalTime = null;

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
  @ApiModelProperty(required = true, value = "User unique identifier from the authentication server of the organization.")
  public String getOrgAuthServerId() {
    return orgAuthServerId;
  }
  public void setOrgAuthServerId(String orgAuthServerId) {
    this.orgAuthServerId = orgAuthServerId;
  }

  /**
   * Total amount of time spent inside the organization by the user.
   **/
  @ApiModelProperty(required = true, value = "Total amount of time spent inside the organization by the user.")
  public Date getTotalTime() {
    return totalTime;
  }
  public void setTotalTime(Date totalTime) {
    this.totalTime = totalTime;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimePerUserReport timePerUserReport = (TimePerUserReport) o;
    return (this.organizationId == null ? timePerUserReport.organizationId == null : this.organizationId.equals(timePerUserReport.organizationId)) &&
        (this.orgAuthServerId == null ? timePerUserReport.orgAuthServerId == null : this.orgAuthServerId.equals(timePerUserReport.orgAuthServerId)) &&
        (this.totalTime == null ? timePerUserReport.totalTime == null : this.totalTime.equals(timePerUserReport.totalTime));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.organizationId == null ? 0: this.organizationId.hashCode());
    result = 31 * result + (this.orgAuthServerId == null ? 0: this.orgAuthServerId.hashCode());
    result = 31 * result + (this.totalTime == null ? 0: this.totalTime.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimePerUserReport {\n");
    
    sb.append("  organizationId: ").append(organizationId).append("\n");
    sb.append("  orgAuthServerId: ").append(orgAuthServerId).append("\n");
    sb.append("  totalTime: ").append(totalTime).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}