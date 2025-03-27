package api.tests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.github.javafaker.Faker;

import api.endpoints.ActionMethods;
import api.payloads.Userpayload;
import io.restassured.response.Response;

public class UserTest {
    Faker faker;
    Userpayload userPayload;
    ActionMethods actionMethods;
    SoftAssert softAssert;
    String createdUsername;  // Store the username for the GET request

    @BeforeClass
    public void setup() {
        faker = new Faker();
        softAssert = new SoftAssert();

        // Initialize user payload and action methods
        userPayload = new Userpayload();
        actionMethods = new ActionMethods();

        // Generate random user data using Faker
        userPayload.setId((int) faker.number().randomNumber());
        userPayload.setUsername(faker.name().username());
        userPayload.setFirstName(faker.name().firstName());
        userPayload.setLastName(faker.name().lastName());
        userPayload.setEmail(faker.internet().emailAddress());
        userPayload.setPassword(faker.internet().password());
        userPayload.setPhone(faker.phoneNumber().phoneNumber());
        userPayload.setUserStatus(faker.number().numberBetween(0, 1));

        
    }

    @Test(priority=1)
    public void testCreateUser() {
    	
    	 
		// Print the generated user details for debugging
        System.out.println("Generated user: " +userPayload.toString());

        // Send a POST request to create the user using ActionMethods
        Response response = actionMethods.createUser(userPayload);
        response.then().log().all();

        // Verify the response status code and other fields
        softAssert.assertEquals(response.getStatusCode(), 200);  // Check for successful creation (HTTP 200)
        softAssert.assertEquals(response.jsonPath().getInt("code"), 200, "'code' field should be 200");
        softAssert.assertEquals(response.getHeader("Content-Type"), "application/json", "Content-Type is not application/json");

        // Call softAssert.assertAll() to trigger the soft assertions
        softAssert.assertAll();
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  // 2 seconds delay
    }
    
    

    @Test(priority = 2)
    public void testGetUser() {
        // Debugging: Log the username being used for the GET request
        System.out.println("Fetching user with username: " + this.userPayload.getUsername());  

        // Send GET request to fetch the user by username (created during POST)
        Response response = actionMethods.gettingUser(this.userPayload.getUsername());  
        response.then().log().all();  // Log full response for debugging

        // Verify that the response status code is 200 (user found)
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200, but got: " + response.getStatusCode());

        // Verify the response content type
        softAssert.assertEquals(response.getHeader("Content-Type"), "application/json", "Content-Type is not application/json");

        // Assert that the returned username matches the created username
        String returnedUsername = response.jsonPath().getString("username");
        softAssert.assertEquals(returnedUsername, userPayload.getUsername(), "Username mismatch: expected " + userPayload.getUsername() + ", but found " + returnedUsername);

        // Call softAssert.assertAll() to trigger the soft assertions
        softAssert.assertAll();
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  // 2 seconds delay
    }
    
    
    @Test(priority = 3)
    public void testUpdateEmailAndPhone() {
        System.out.println("Updating email and phone for user with username: " + this.userPayload.getUsername());
        // Create a new user payload with updated email and phone number
        userPayload.setEmail(faker.internet().emailAddress());
        userPayload.setPassword(faker.internet().password());
        userPayload.setPhone(faker.phoneNumber().phoneNumber());
        // Send PUT request to update the user with new email and phone number
        Response response = actionMethods.updatingUser(this.userPayload.getUsername(), userPayload);
        // Log the response
        response.then().log().all();
     // Hard assertion that the status code is 200 (success)
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200, but got: " + response.getStatusCode());
     // Assertions to check if the email and phone number are updated
        
       //for updating data calling get response once again
        Response updatedresponse = actionMethods.gettingUser(this.userPayload.getUsername());  
        updatedresponse.then().log().all();  // Log full response for debugging
     // Assertions to check if the email and phone number are updated
        Assert.assertEquals(updatedresponse.jsonPath().getString("email"), userPayload.getEmail(), "Email mismatch after update");
        Assert.assertEquals(updatedresponse.jsonPath().getString("phone"), userPayload.getPhone(), "Phone number mismatch after update");
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  // 2 seconds delay

       
    }
    @Test(priority=4)
    public void DeleteUserByName() {
    	Response response=actionMethods.deletingUser(this.userPayload.getUsername());
    	Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200, but got: " + response.getStatusCode());
 	   //Response getResponse = actionMethods.gettingUser(this.userPayload.getUsername());
 	   // Assert.assertEquals(getResponse.getStatusCode(), 404, "Expected 404 Not Found after deletion.");

    }


}
