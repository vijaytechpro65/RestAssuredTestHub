package api.endpoints;

import static io.restassured.RestAssured.given;

import api.payloads.Userpayload;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ActionMethods {
    // Instance of RoutesFinder to load and fetch routes
    private RoutesFinder routesFinder;

    // Constructor to initialize the RoutesFinder and load the properties
    public ActionMethods() {
        routesFinder = new RoutesFinder();
        routesFinder.init_prop();  // Load properties from the config file
    }

    // Method to create a user
    public Response createUser(Userpayload payload) {
        // Send POST request to create a user
        Response response = given()
            .contentType(ContentType.JSON)  // Specify the request's Content-Type as JSON
            .accept(ContentType.JSON)      // Specify that we accept JSON responses
            .body(payload)                 // Pass the payload (user data) as the request body
        .when()
            .post(routesFinder.getFullUrl("post_url"));  // Use RoutesFinder to get full URL
        return response;  // Return the response object
    }

    // Method to get a user by username
    public Response gettingUser(String username) {
        // Send GET request to retrieve the user
        Response response = given()
            .pathParam("username", username)  // Path parameter for username
        .when()
            .get(routesFinder.getFullUrl("get_url"));  // Use RoutesFinder to get full URL
        return response;
    }

    // Method to update a user by username
    public Response updatingUser(String username, Userpayload payload) {
        // Send PUT request to update the user
        Response response = given()
            .contentType(ContentType.JSON)  // Specify the request's Content-Type as JSON
            .accept(ContentType.JSON)      // Specify that we accept JSON responses
            .pathParam("username", username)  // Path parameter for username
            .body(payload)  // Pass the payload (updated user data) as the request body
        .when()
            .put(routesFinder.getFullUrl("put_url"));  // Use RoutesFinder to get full URL
        return response;
    }

    // Method to delete a user by username
    public Response deletingUser(String username) {
        // Send DELETE request to delete the user
        Response response = given()
            .pathParam("username", username)  // Path parameter for username
        .when()
            .delete(routesFinder.getFullUrl("delete_url"));  // Use RoutesFinder to get full URL
        return response;
    }
}
