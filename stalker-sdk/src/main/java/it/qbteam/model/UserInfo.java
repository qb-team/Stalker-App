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

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;

@ApiModel(description = "")
public class UserInfo {
  
  @SerializedName("name")
  private String name = null;
  @SerializedName("surname")
  private String surname = null;
  @SerializedName("mail")
  private String mail = null;

  /**
   * Name of the user (if available in the authentication server of the organization).
   **/
  @ApiModelProperty(required = true, value = "Name of the user (if available in the authentication server of the organization).")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Surname of the user (if available in the authentication server of the organization).
   **/
  @ApiModelProperty(required = true, value = "Surname of the user (if available in the authentication server of the organization).")
  public String getSurname() {
    return surname;
  }
  public void setSurname(String surname) {
    this.surname = surname;
  }

  /**
   * Mail address of the user.
   **/
  @ApiModelProperty(required = true, value = "Mail address of the user.")
  public String getMail() {
    return mail;
  }
  public void setMail(String mail) {
    this.mail = mail;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserInfo userInfo = (UserInfo) o;
    return (this.name == null ? userInfo.name == null : this.name.equals(userInfo.name)) &&
        (this.surname == null ? userInfo.surname == null : this.surname.equals(userInfo.surname)) &&
        (this.mail == null ? userInfo.mail == null : this.mail.equals(userInfo.mail));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.name == null ? 0: this.name.hashCode());
    result = 31 * result + (this.surname == null ? 0: this.surname.hashCode());
    result = 31 * result + (this.mail == null ? 0: this.mail.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserInfo {\n");
    
    sb.append("  name: ").append(name).append("\n");
    sb.append("  surname: ").append(surname).append("\n");
    sb.append("  mail: ").append(mail).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}