# stalker-app

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>it.qbteam</groupId>
    <artifactId>stalker-app</artifactId>
    <version>0.0.1</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "it.qbteam:stalker-app:0.0.1"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

- target/stalker-app-0.0.1.jar
- target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import it.qbteam.api.AccessApi;

public class AccessApiExample {

    public static void main(String[] args) {
        AccessApi apiInstance = new AccessApi();
        List<String> orgAuthServerIds = null; // List<String> | One or more orgAuthServerIds. If it is called by the app user, the orgAuthServerIds parameter can only consist in one identifier. Otherwise it can be more than one identifier.
        Long organizationId = null; // Long | ID of an organization
        try {
            List<OrganizationAccess> result = apiInstance.getAuthenticatedAccessListInOrganization(orgAuthServerIds, organizationId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AccessApi#getAuthenticatedAccessListInOrganization");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *http://localhost:8080*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*AccessApi* | [**getAuthenticatedAccessListInOrganization**](docs/AccessApi.md#getAuthenticatedAccessListInOrganization) | **GET** /access/organization/{organizationId}/authenticated/{orgAuthServerIds} | Returns all the authenticated accesses in an organization registered of one or more users (orgAuthServerIds are separated by commas).
*AccessApi* | [**getAuthenticatedAccessListInPlace**](docs/AccessApi.md#getAuthenticatedAccessListInPlace) | **GET** /access/place/{placeId}/authenticated/{orgAuthServerIds} | Returns all the authenticated accesses in a place registered of one or more users (orgAuthServerIds are separated by commas).
*AdministratorApi* | [**bindAdministratorToOrganization**](docs/AdministratorApi.md#bindAdministratorToOrganization) | **POST** /organization/administrator/bind | Bind an already existent administrator to the organization.
*AdministratorApi* | [**createNewAdministratorToOrganization**](docs/AdministratorApi.md#createNewAdministratorToOrganization) | **POST** /organization/administrator/create | Creates and binds a new administrator to the organization.
*AdministratorApi* | [**getAdministratorListOfOrganization**](docs/AdministratorApi.md#getAdministratorListOfOrganization) | **GET** /organization/{organizationId}/administrator | Returns the list of administrators of the organization.
*AdministratorApi* | [**getPermissionList**](docs/AdministratorApi.md#getPermissionList) | **GET** /organization/permission/{administratorId} | Gets the list of permission that an administrator has permissions to view/manage/own.
*AdministratorApi* | [**unbindAdministratorFromOrganization**](docs/AdministratorApi.md#unbindAdministratorFromOrganization) | **POST** /organization/administrator/unbind | Unbind an administrator to the organization.
*AdministratorApi* | [**updateAdministratorPermission**](docs/AdministratorApi.md#updateAdministratorPermission) | **POST** /organization/administrator/update | Update the permission for an already existent administrator in the organization.
*FavoriteApi* | [**addFavoriteOrganization**](docs/FavoriteApi.md#addFavoriteOrganization) | **POST** /organization/favorite/{userId}/add/{organizationId} | Adds a new organization to the user&#39;s favorite organization list.
*FavoriteApi* | [**getFavoriteOrganizationList**](docs/FavoriteApi.md#getFavoriteOrganizationList) | **GET** /organization/favorite/{userId} | Gets the list of favorite organizations of a user.
*FavoriteApi* | [**removeFavoriteOrganization**](docs/FavoriteApi.md#removeFavoriteOrganization) | **DELETE** /organization/favorite/{userId}/remove/{organizationId} | Removes the organization from the user&#39;s favorite organization list.
*MovementApi* | [**trackMovementInOrganization**](docs/MovementApi.md#trackMovementInOrganization) | **POST** /movement/track/organization | Tracks the user movement inside the trackingArea of an organization.
*MovementApi* | [**trackMovementInPlace**](docs/MovementApi.md#trackMovementInPlace) | **POST** /movement/track/place | Tracks the user movement inside the trackingArea of a place of an organization.
*OrganizationApi* | [**addFavoriteOrganization**](docs/OrganizationApi.md#addFavoriteOrganization) | **POST** /organization/favorite/{userId}/add/{organizationId} | Adds a new organization to the user&#39;s favorite organization list.
*OrganizationApi* | [**bindAdministratorToOrganization**](docs/OrganizationApi.md#bindAdministratorToOrganization) | **POST** /organization/administrator/bind | Bind an already existent administrator to the organization.
*OrganizationApi* | [**createNewAdministratorToOrganization**](docs/OrganizationApi.md#createNewAdministratorToOrganization) | **POST** /organization/administrator/create | Creates and binds a new administrator to the organization.
*OrganizationApi* | [**getAdministratorListOfOrganization**](docs/OrganizationApi.md#getAdministratorListOfOrganization) | **GET** /organization/{organizationId}/administrator | Returns the list of administrators of the organization.
*OrganizationApi* | [**getAuthenticatedAccessListInOrganization**](docs/OrganizationApi.md#getAuthenticatedAccessListInOrganization) | **GET** /access/organization/{organizationId}/authenticated/{orgAuthServerIds} | Returns all the authenticated accesses in an organization registered of one or more users (orgAuthServerIds are separated by commas).
*OrganizationApi* | [**getFavoriteOrganizationList**](docs/OrganizationApi.md#getFavoriteOrganizationList) | **GET** /organization/favorite/{userId} | Gets the list of favorite organizations of a user.
*OrganizationApi* | [**getOrganization**](docs/OrganizationApi.md#getOrganization) | **GET** /organization/{organizationId} | Gets the available data for a single organization.
*OrganizationApi* | [**getOrganizationList**](docs/OrganizationApi.md#getOrganizationList) | **GET** /organization | Returns the list of all organizations.
*OrganizationApi* | [**getOrganizationPresenceCounter**](docs/OrganizationApi.md#getOrganizationPresenceCounter) | **GET** /presence/organization/{organizationId}/counter | Gets the number of people currently inside the organization&#39;s trackingArea.
*OrganizationApi* | [**getOrganizationPresenceList**](docs/OrganizationApi.md#getOrganizationPresenceList) | **GET** /presence/organization/{organizationId} | Gets the list of people currently inside the organization&#39;s trackingArea.
*OrganizationApi* | [**getPermissionList**](docs/OrganizationApi.md#getPermissionList) | **GET** /organization/permission/{administratorId} | Gets the list of permission that an administrator has permissions to view/manage/own.
*OrganizationApi* | [**getPlaceListOfOrganization**](docs/OrganizationApi.md#getPlaceListOfOrganization) | **GET** /organization/{organizationId}/place | Returns the list of places of the organization.
*OrganizationApi* | [**getTimePerUserReport**](docs/OrganizationApi.md#getTimePerUserReport) | **GET** /report/organization/{organizationId} | Gets the report of total time spent per user inside the organization.
*OrganizationApi* | [**removeFavoriteOrganization**](docs/OrganizationApi.md#removeFavoriteOrganization) | **DELETE** /organization/favorite/{userId}/remove/{organizationId} | Removes the organization from the user&#39;s favorite organization list.
*OrganizationApi* | [**requestDeletionOfOrganization**](docs/OrganizationApi.md#requestDeletionOfOrganization) | **POST** /organization/{organizationId}/requestdeletion | Sends a deletion request to the system. The request will be examined by Stalker administrators.
*OrganizationApi* | [**trackMovementInOrganization**](docs/OrganizationApi.md#trackMovementInOrganization) | **POST** /movement/track/organization | Tracks the user movement inside the trackingArea of an organization.
*OrganizationApi* | [**unbindAdministratorFromOrganization**](docs/OrganizationApi.md#unbindAdministratorFromOrganization) | **POST** /organization/administrator/unbind | Unbind an administrator to the organization.
*OrganizationApi* | [**updateAdministratorPermission**](docs/OrganizationApi.md#updateAdministratorPermission) | **POST** /organization/administrator/update | Update the permission for an already existent administrator in the organization.
*OrganizationApi* | [**updateOrganization**](docs/OrganizationApi.md#updateOrganization) | **PUT** /organization/{organizationId} | Updates one or more properties of an organization.
*OrganizationApi* | [**updateTrackingArea**](docs/OrganizationApi.md#updateTrackingArea) | **PUT** /organization/{organizationId}/trackingArea | Updates the coordinates of the tracking area of an organization.
*PermissionApi* | [**bindAdministratorToOrganization**](docs/PermissionApi.md#bindAdministratorToOrganization) | **POST** /organization/administrator/bind | Bind an already existent administrator to the organization.
*PermissionApi* | [**getPermissionList**](docs/PermissionApi.md#getPermissionList) | **GET** /organization/permission/{administratorId} | Gets the list of permission that an administrator has permissions to view/manage/own.
*PermissionApi* | [**unbindAdministratorFromOrganization**](docs/PermissionApi.md#unbindAdministratorFromOrganization) | **POST** /organization/administrator/unbind | Unbind an administrator to the organization.
*PermissionApi* | [**updateAdministratorPermission**](docs/PermissionApi.md#updateAdministratorPermission) | **POST** /organization/administrator/update | Update the permission for an already existent administrator in the organization.
*PlaceApi* | [**createNewPlace**](docs/PlaceApi.md#createNewPlace) | **POST** /place | Creates a new place for an organization.
*PlaceApi* | [**deletePlace**](docs/PlaceApi.md#deletePlace) | **DELETE** /place/{placeId} | Deletes a place of an organization.
*PlaceApi* | [**getAuthenticatedAccessListInPlace**](docs/PlaceApi.md#getAuthenticatedAccessListInPlace) | **GET** /access/place/{placeId}/authenticated/{orgAuthServerIds} | Returns all the authenticated accesses in a place registered of one or more users (orgAuthServerIds are separated by commas).
*PlaceApi* | [**getPlaceListOfOrganization**](docs/PlaceApi.md#getPlaceListOfOrganization) | **GET** /organization/{organizationId}/place | Returns the list of places of the organization.
*PlaceApi* | [**getPlacePresenceCounter**](docs/PlaceApi.md#getPlacePresenceCounter) | **GET** /presence/place/{placeId}/counter | Gets the number of people currently inside the place&#39;s trackingArea.
*PlaceApi* | [**getPlacePresenceList**](docs/PlaceApi.md#getPlacePresenceList) | **GET** /presence/place/{placeId} | Gets the list of people currently inside the place&#39;s trackingArea.
*PlaceApi* | [**trackMovementInPlace**](docs/PlaceApi.md#trackMovementInPlace) | **POST** /movement/track/place | Tracks the user movement inside the trackingArea of a place of an organization.
*PlaceApi* | [**updatePlace**](docs/PlaceApi.md#updatePlace) | **PUT** /place/{placeId} | Updates one or more properties of a place of an organization.
*PresenceApi* | [**getOrganizationPresenceCounter**](docs/PresenceApi.md#getOrganizationPresenceCounter) | **GET** /presence/organization/{organizationId}/counter | Gets the number of people currently inside the organization&#39;s trackingArea.
*PresenceApi* | [**getOrganizationPresenceList**](docs/PresenceApi.md#getOrganizationPresenceList) | **GET** /presence/organization/{organizationId} | Gets the list of people currently inside the organization&#39;s trackingArea.
*PresenceApi* | [**getPlacePresenceCounter**](docs/PresenceApi.md#getPlacePresenceCounter) | **GET** /presence/place/{placeId}/counter | Gets the number of people currently inside the place&#39;s trackingArea.
*PresenceApi* | [**getPlacePresenceList**](docs/PresenceApi.md#getPlacePresenceList) | **GET** /presence/place/{placeId} | Gets the list of people currently inside the place&#39;s trackingArea.
*ReportApi* | [**getTimePerUserReport**](docs/ReportApi.md#getTimePerUserReport) | **GET** /report/organization/{organizationId} | Gets the report of total time spent per user inside the organization.


## Documentation for Models

 - [Administrator](docs/Administrator.md)
 - [AdministratorInfo](docs/AdministratorInfo.md)
 - [Favorite](docs/Favorite.md)
 - [Organization](docs/Organization.md)
 - [OrganizationAccess](docs/OrganizationAccess.md)
 - [OrganizationMovement](docs/OrganizationMovement.md)
 - [OrganizationPresenceCounter](docs/OrganizationPresenceCounter.md)
 - [Permission](docs/Permission.md)
 - [Place](docs/Place.md)
 - [PlaceAccess](docs/PlaceAccess.md)
 - [PlaceMovement](docs/PlaceMovement.md)
 - [PlacePresenceCounter](docs/PlacePresenceCounter.md)
 - [TimePerUserReport](docs/TimePerUserReport.md)
 - [User](docs/User.md)
 - [UserInfo](docs/UserInfo.md)


## Documentation for Authorization

Authentication schemes defined for the API:
### bearerAuth

- **Type**: HTTP basic authentication


## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author

qbteamswe@gmail.com

