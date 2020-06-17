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
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.time.OffsetDateTime;
import javax.validation.constraints.*;
import javax.validation.Valid;

/**
 * Subject interested in tracking people&#39;s presence inside its own places, in either an anonymous or authenticated way.
 */
@ApiModel(description = "Subject interested in tracking people's presence inside its own places, in either an anonymous or authenticated way.")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2020-05-02T17:39:19.016+02:00[Europe/Rome]")
public class Organization implements Comparable<Organization>, Serializable {
  public static final String SERIALIZED_NAME_ID = "id";
  @SerializedName(SERIALIZED_NAME_ID)
  private Long id;

  public static final String SERIALIZED_NAME_NAME = "name";
  @SerializedName(SERIALIZED_NAME_NAME)
  private String name;

  public static final String SERIALIZED_NAME_DESCRIPTION = "description";
  @SerializedName(SERIALIZED_NAME_DESCRIPTION)
  private String description;

  public static final String SERIALIZED_NAME_IMAGE = "image";
  @SerializedName(SERIALIZED_NAME_IMAGE)
  private String image;

  public static final String SERIALIZED_NAME_STREET = "street";
  @SerializedName(SERIALIZED_NAME_STREET)
  private String street;

  public static final String SERIALIZED_NAME_NUMBER = "number";
  @SerializedName(SERIALIZED_NAME_NUMBER)
  private String number;

  public static final String SERIALIZED_NAME_POST_CODE = "postCode";
  @SerializedName(SERIALIZED_NAME_POST_CODE)
  private Integer postCode;

  public static final String SERIALIZED_NAME_CITY = "city";
  @SerializedName(SERIALIZED_NAME_CITY)
  private String city;

  public static final String SERIALIZED_NAME_COUNTRY = "country";
  @SerializedName(SERIALIZED_NAME_COUNTRY)
  private String country;

  public static final String SERIALIZED_NAME_AUTHENTICATION_SERVER_U_R_L = "authenticationServerURL";
  @SerializedName(SERIALIZED_NAME_AUTHENTICATION_SERVER_U_R_L)
  private String authenticationServerURL;

  public static final String SERIALIZED_NAME_CREATION_DATE = "creationDate";
  @SerializedName(SERIALIZED_NAME_CREATION_DATE)
  private OffsetDateTime creationDate;

  public static final String SERIALIZED_NAME_LAST_CHANGE_DATE = "lastChangeDate";
  @SerializedName(SERIALIZED_NAME_LAST_CHANGE_DATE)
  private OffsetDateTime lastChangeDate;

  public static final String SERIALIZED_NAME_TRACKING_AREA = "trackingArea";
  @SerializedName(SERIALIZED_NAME_TRACKING_AREA)
  private String trackingArea;

  public static final String SERIALIZED_ORG_AUTH_SERVER_ID = "orgAuthServerId";
  @SerializedName(SERIALIZED_ORG_AUTH_SERVER_ID)
  private String orgAuthServerId;

  @Override
  public int compareTo(Organization o) {
    return name.compareTo(o.getName());
  }


  /**
   * How an user who added to its favorites the organization can be tracked inside the organization&#39;s trackingArea and its places.
   */
  @JsonAdapter(TrackingModeEnum.Adapter.class)
  public enum TrackingModeEnum {
    authenticated("authenticated"),

    anonymous("anonymous");

    private String value;

    TrackingModeEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static TrackingModeEnum fromValue(String value) {
      for (TrackingModeEnum b : TrackingModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
    public void setTrackingMode(String tracking){
      this.value=tracking;
    }

    public static class Adapter extends TypeAdapter<TrackingModeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final TrackingModeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public TrackingModeEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return TrackingModeEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_TRACKING_MODE = "trackingMode";
  @SerializedName(SERIALIZED_NAME_TRACKING_MODE)
  private TrackingModeEnum trackingMode;


  public Organization id(Long id) {

    this.id = id;
    return this;
  }

  public Long getId() {
    return id;
  }

  public Organization setId(Long id) {
    this.id = id;
    return this;
  }

  public void setOrgAuthServerId(String auth){
    this.orgAuthServerId=auth;
  }

  public String getOrgAuthServerId(){
    return this.orgAuthServerId;
  }

  /**
   * Unique identifier for an organization.
   * minimum: 1
   * @return id
   **/
  @NotNull
  @Min(1L)  @ApiModelProperty(required = true, value = "Unique identifier for an organization.")

  /*public Long getId() {
    return id;
  }


  public void setId(Long id) {
    this.id = id;
  }*/


  public Organization name(String name) {

    this.name = name;
    return this;
  }

  /**
   * Name of the organization.
   * @return name
   **/
  @NotNull
  @Size(max=128)  @ApiModelProperty(required = true, value = "Name of the organization.")

  public String getName() {
    return name;
  }


  public Organization setName(String name) {
    this.name = name;
    return this;
  }


  public Organization description(String description) {

    this.description = description;
    return this;
  }

  /**
   * Small description of what the organization does.
   * @return description
   **/
  @javax.annotation.Nullable
  @Size(max=512)  @ApiModelProperty(value = "Small description of what the organization does.")

  public String getDescription() {
    return description;
  }


  public void setDescription(String description) {
    this.description = description;
  }


  public Organization image(String image) {

    this.image = image;
    return this;
  }

  /**
   * Image/logo for the organization which gets shown on the application.
   * @return image
   **/
  @javax.annotation.Nullable
  @Size(max=300)  @ApiModelProperty(value = "Image/logo for the organization which gets shown on the application.")

  public String getImage() {
    return image;
  }


  public void setImage(String image) {
    this.image = image;
  }


  public Organization street(String street) {

    this.street = street;
    return this;
  }

  /**
   * The street where the organization is located.
   * @return street
   **/
  @NotNull
  @Size(max=256)  @ApiModelProperty(required = true, value = "The street where the organization is located.")

  public String getStreet() {
    return street;
  }


  public void setStreet(String street) {
    this.street = street;
  }


  public Organization number(String number) {

    this.number = number;
    return this;
  }

  /**
   * The number in the street where the organization is located.
   * @return number
   **/
  @NotNull
  @Size(max=10)  @ApiModelProperty(required = true, value = "The number in the street where the organization is located.")

  public String getNumber() {
    return number;
  }


  public void setNumber(String number) {
    this.number = number;
  }


  public Organization postCode(Integer postCode) {

    this.postCode = postCode;
    return this;
  }

  /**
   * The postcode where the organization is located.
   * minimum: 0
   * maximum: 99999
   * @return postCode
   **/
  @NotNull
  @Min(0) @Max(99999)  @ApiModelProperty(required = true, value = "The postcode where the organization is located.")

  public Integer getPostCode() {
    return postCode;
  }


  public void setPostCode(Integer postCode) {
    this.postCode = postCode;
  }


  public Organization city(String city) {

    this.city = city;
    return this;
  }

  /**
   * The city where the organization is located.
   * @return city
   **/
  @NotNull
  @Size(max=100)  @ApiModelProperty(required = true, value = "The city where the organization is located.")

  public String getCity() {
    return city;
  }


  public void setCity(String city) {
    this.city = city;
  }


  public Organization country(String country) {

    this.country = country;
    return this;
  }

  /**
   * The country where the organization is located.
   * @return country
   **/
  @NotNull
  @Size(max=100)  @ApiModelProperty(required = true, value = "The country where the organization is located.")

  public String getCountry() {
    return country;
  }


  public void setCountry(String country) {
    this.country = country;
  }


  public Organization authenticationServerURL(String authenticationServerURL) {

    this.authenticationServerURL = authenticationServerURL;
    return this;
  }

  /**
   * URL or IP address of the authentication server of the organization. If it&#39;s required a specific port or protocol it must be specified. Needed only if trackingMethod is set to authenticated.
   * @return authenticationServerURL
   **/
  @javax.annotation.Nullable
  @Size(max=2048)  @ApiModelProperty(value = "URL or IP address of the authentication server of the organization. If it's required a specific port or protocol it must be specified. Needed only if trackingMethod is set to authenticated.")

  public String getAuthenticationServerURL() {
    return authenticationServerURL;
  }


  public void setAuthenticationServerURL(String authenticationServerURL) {
    this.authenticationServerURL = authenticationServerURL;
  }


  public Organization creationDate(OffsetDateTime creationDate) {

    this.creationDate = creationDate;
    return this;
  }

  /**
   * When the organization was added to the system.
   * @return creationDate
   **/
  @NotNull
  @Valid
  @ApiModelProperty(required = true, value = "When the organization was added to the system.")

  public OffsetDateTime getCreationDate() {
    return creationDate;
  }


  public void setCreationDate(OffsetDateTime creationDate) {
    this.creationDate = creationDate;
  }


  public Organization lastChangeDate(OffsetDateTime lastChangeDate) {

    this.lastChangeDate = lastChangeDate;
    return this;
  }

  /**
   * When the organization parameters were last changed.
   * @return lastChangeDate
   **/
  @NotNull
  @Valid
  @ApiModelProperty(required = true, value = "When the organization parameters were last changed.")

  public OffsetDateTime getLastChangeDate() {
    return lastChangeDate;
  }


  public void setLastChangeDate(OffsetDateTime lastChangeDate) {
    this.lastChangeDate = lastChangeDate;
  }


  public Organization trackingArea(String trackingArea) {

    this.trackingArea = trackingArea;
    return this;
  }

  /**
   * Area subjected to movement tracking of people. It is a collection of (longitude, latitude) pairs consisting in a polygon. The string is expressed in JSON format.
   * @return trackingArea
   **/
  @NotNull
  @ApiModelProperty(required = true, value = "Area subjected to movement tracking of people. It is a collection of (longitude, latitude) pairs consisting in a polygon. The string is expressed in JSON format.")

  public String getTrackingArea() {
    return trackingArea;
  }


  public void setTrackingArea(String trackingArea) {
    this.trackingArea = trackingArea;
  }


  public Organization trackingMode(TrackingModeEnum trackingMode) {

    this.trackingMode = trackingMode;
    return this;
  }

  /**
   * How an user who added to its favorites the organization can be tracked inside the organization&#39;s trackingArea and its places.
   * @return trackingMode
   **/
  @NotNull
  @ApiModelProperty(required = true, value = "How an user who added to its favorites the organization can be tracked inside the organization's trackingArea and its places.")

  public TrackingModeEnum getTrackingMode() {
    return trackingMode;
  }


  public void setTrackingMode(String trackingMode) {
    if (trackingMode.equals(TrackingModeEnum.anonymous.value)){
      this.trackingMode = TrackingModeEnum.anonymous;
    } else if (trackingMode.equals(TrackingModeEnum.authenticated.value)){
      this.trackingMode = TrackingModeEnum.authenticated;
    }

  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Organization organization = (Organization) o;
    return Objects.equals(this.id, organization.id) &&
            Objects.equals(this.name, organization.name) &&
            Objects.equals(this.description, organization.description) &&
            Objects.equals(this.image, organization.image) &&
            Objects.equals(this.street, organization.street) &&
            Objects.equals(this.number, organization.number) &&
            Objects.equals(this.postCode, organization.postCode) &&
            Objects.equals(this.city, organization.city) &&
            Objects.equals(this.country, organization.country) &&
            Objects.equals(this.authenticationServerURL, organization.authenticationServerURL) &&
            Objects.equals(this.creationDate, organization.creationDate) &&
            Objects.equals(this.lastChangeDate, organization.lastChangeDate) &&
            Objects.equals(this.trackingArea, organization.trackingArea) &&
            Objects.equals(this.trackingMode, organization.trackingMode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, image, street, number, postCode, city, country, authenticationServerURL, creationDate, lastChangeDate, trackingArea, trackingMode);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Organization {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    image: ").append(toIndentedString(image)).append("\n");
    sb.append("    street: ").append(toIndentedString(street)).append("\n");
    sb.append("    number: ").append(toIndentedString(number)).append("\n");
    sb.append("    postCode: ").append(toIndentedString(postCode)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    authenticationServerURL: ").append(toIndentedString(authenticationServerURL)).append("\n");
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
    sb.append("    lastChangeDate: ").append(toIndentedString(lastChangeDate)).append("\n");
    sb.append("    trackingArea: ").append(toIndentedString(trackingArea)).append("\n");
    sb.append("    trackingMode: ").append(toIndentedString(trackingMode)).append("\n");
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
