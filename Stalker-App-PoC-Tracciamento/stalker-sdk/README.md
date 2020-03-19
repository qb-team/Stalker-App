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
    <version>0.0.1-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "it.qbteam:stalker-app:0.0.1-SNAPSHOT"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

- target/stalker-app-0.0.1-SNAPSHOT.jar
- target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import it.qbteam.api.AccessApi;

public class AccessApiExample {

    public static void main(String[] args) {
        AccessApi apiInstance = new AccessApi();
        Long organizationId = null; // Long | ID of an organization
        try {
            List<OrganizationAuthenticatedAccess> result = apiInstance.getAccessListInOrganization(organizationId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AccessApi#getAccessListInOrganization");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *http://localhost:8080*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*AccessApi* | [**getAccessListInOrganization**](docs/AccessApi.md#getAccessListInOrganization) | **GET** /access/organization/{organizationId}/authenticated | Returns all the authenticated accesses in an organization registered.
*AccessApi* | [**getAccessListInOrganizationOfUsers**](docs/AccessApi.md#getAccessListInOrganizationOfUsers) | **GET** /access/organization/{organizationId}/authenticated/{userIds} | Returns all the authenticated accesses in an organization registered of one or more users (userIds are separated by commas).
*AccessApi* | [**getAccessListInPlace**](docs/AccessApi.md#getAccessListInPlace) | **GET** /access/place/{placeId}/authenticated | Returns all the authenticated accesses in a place registered.
*AccessApi* | [**getAccessListInPlaceOfUsers**](docs/AccessApi.md#getAccessListInPlaceOfUsers) | **GET** /access/place/{placeId}/authenticated/{userIds} | Returns all the authenticated accesses in a place registered of one or more users (userIds are separated by commas).
*AdminAuthenticationApi* | [**adminLogin**](docs/AdminAuthenticationApi.md#adminLogin) | **POST** /authentication/adminLogin | Lets the admin login via the authentication service.
*AdminAuthenticationApi* | [**adminLogout**](docs/AdminAuthenticationApi.md#adminLogout) | **POST** /authentication/adminLogout | Lets the admin logout from the system.
*OrganizationApi* | [**getOrganizationById**](docs/OrganizationApi.md#getOrganizationById) | **GET** /organization/{organizationId} | Gets the data of a single organization.
*OrganizationApi* | [**getOrganizationList**](docs/OrganizationApi.md#getOrganizationList) | **GET** /organization | Returns the list of all organizations.
*OrganizationApi* | [**updateOrganization**](docs/OrganizationApi.md#updateOrganization) | **PUT** /organization/{organizationId} | Updates one or more properties of a single organization.
*TrackingApi* | [**trackAnonymousMovementInOrganization**](docs/TrackingApi.md#trackAnonymousMovementInOrganization) | **POST** /movement/track/organization/anonymous | Tracks the user movement inside the trackingArea of an organization with the anonymous trackingMode.
*TrackingApi* | [**trackAnonymousMovementInPlace**](docs/TrackingApi.md#trackAnonymousMovementInPlace) | **POST** /movement/track/place/anonymous | Tracks the user movement inside the trackingArea of a place of an organization with the anonymous trackingMode.
*TrackingApi* | [**trackAuthenticatedMovementInOrganization**](docs/TrackingApi.md#trackAuthenticatedMovementInOrganization) | **POST** /movement/track/organization/authenticated | Tracks the user movement inside the trackingArea of an organization with the authenticated trackingMode.
*TrackingApi* | [**trackAuthenticatedMovementInPlace**](docs/TrackingApi.md#trackAuthenticatedMovementInPlace) | **POST** /movement/track/place/authenticated | Tracks the user movement inside the trackingArea of a place of an organization with the authenticated trackingMode.
*UserAuthenticationApi* | [**userLogin**](docs/UserAuthenticationApi.md#userLogin) | **POST** /authentication/userLogin | Lets the user login via the authentication service.
*UserAuthenticationApi* | [**userLogout**](docs/UserAuthenticationApi.md#userLogout) | **POST** /authentication/userLogout | Lets the user logout from the system.
*UserAuthenticationApi* | [**userRegistration**](docs/UserAuthenticationApi.md#userRegistration) | **POST** /authentication/userRegistration | Lets the user registrate into the system.


## Documentation for Models

 - [Access](docs/Access.md)
 - [Administrator](docs/Administrator.md)
 - [AuthResponseAdmin](docs/AuthResponseAdmin.md)
 - [AuthResponseUser](docs/AuthResponseUser.md)
 - [AuthenticationDataAdmin](docs/AuthenticationDataAdmin.md)
 - [AuthenticationDataUser](docs/AuthenticationDataUser.md)
 - [Favorite](docs/Favorite.md)
 - [Movement](docs/Movement.md)
 - [Organization](docs/Organization.md)
 - [OrganizationAnonymousAccess](docs/OrganizationAnonymousAccess.md)
 - [OrganizationAnonymousAccessAllOf](docs/OrganizationAnonymousAccessAllOf.md)
 - [OrganizationAnonymousMovement](docs/OrganizationAnonymousMovement.md)
 - [OrganizationAnonymousMovementAllOf](docs/OrganizationAnonymousMovementAllOf.md)
 - [OrganizationAuthenticatedAccess](docs/OrganizationAuthenticatedAccess.md)
 - [OrganizationAuthenticatedAccessAllOf](docs/OrganizationAuthenticatedAccessAllOf.md)
 - [OrganizationAuthenticatedMovement](docs/OrganizationAuthenticatedMovement.md)
 - [Permission](docs/Permission.md)
 - [Place](docs/Place.md)
 - [PlaceAnonymousAccess](docs/PlaceAnonymousAccess.md)
 - [PlaceAnonymousAccessAllOf](docs/PlaceAnonymousAccessAllOf.md)
 - [PlaceAnonymousMovement](docs/PlaceAnonymousMovement.md)
 - [PlaceAuthenticatedAccess](docs/PlaceAuthenticatedAccess.md)
 - [PlaceAuthenticatedAccessAllOf](docs/PlaceAuthenticatedAccessAllOf.md)
 - [PlaceAuthenticatedMovement](docs/PlaceAuthenticatedMovement.md)
 - [User](docs/User.md)


## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author

qbteamswe@gmail.com

