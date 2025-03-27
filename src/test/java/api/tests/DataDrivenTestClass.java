package api.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;



import api.endpoints.ActionMethods;
import api.payloads.Userpayload;
import api.utilities.ExcelUtil;
import io.restassured.response.Response;

public class DataDrivenTestClass {

    SoftAssert softAssert = new SoftAssert();
    ActionMethods actionMethods = new ActionMethods();

    @DataProvider
    public Object[][] getUserData() {
        String sheetName = "UserDetails"; // Replace with your actual sheet name
        ExcelUtil.initExcel(sheetName);

        // Fetch data as a 2D array
        Object[][] userData = ExcelUtil.getDataAsArray();

        // Close the workbook
        ExcelUtil.close();

        return userData;
    }

    // Test Case for Create User
    @Test(priority = 1, dataProvider = "getUserData")
    public void testCreateUser(String userID, String Username, String FirstName, String LastName, String Email, String Password, String Phone) {
        try {
            // Create a user payload using Excel data
            Userpayload userPayload = new Userpayload();
            userPayload.setId(Integer.parseInt(userID));
            userPayload.setUsername(Username);
            userPayload.setFirstName( FirstName);
            userPayload.setLastName(LastName);
            userPayload.setEmail(Email);
            userPayload.setPassword(Password);
            userPayload.setPhone(Phone);
            
       // Send a POST request to create the user using ActionMethods
            Response response = actionMethods.createUser(userPayload);
            response.then().log().all();

            // Verify the response status code and other fields
            softAssert.assertEquals(response.getStatusCode(), 200);

            softAssert.assertAll();
        } catch (Exception e) {
            System.err.println("Error in creating user: " + e.getMessage());
        }
    }

    // Test Case for Get User
    @Test(priority = 2, dataProvider = "getUserData")
    public void testGetUser(String userID, String Username, String FirstName, String LastName, String Email, String Password, String Phone) {
        try {
            // Send a GET request to fetch the user by username
            Response response = actionMethods.gettingUser(Username);
            response.then().log().all();  // Log full response for debugging

            // Verify that the response status code is 200 (user found)
            softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200, but got: " + response.getStatusCode());

            // Assert that the returned username matches the created username
            softAssert.assertEquals(response.jsonPath().getString("username"), Username, "Username mismatch: expected " + Username);

            softAssert.assertAll();
        } catch (Exception e) {
            System.err.println("Error in fetching user: " + e.getMessage());
        }
    }

    // Test Case for Update User (email and phone)
    @Test(priority = 3, dataProvider = "getUserData")
    public void testUpdateUser(String userID, String Username, String FirstName, String LastName, String Email, String Password, String Phone) {
        try {
            // Create a new user payload with updated email and phone number
            Userpayload updatedUser = new Userpayload();
            updatedUser.setUsername(Username);
            updatedUser.setEmail("updated_" + Email); // Updated email for testing
            updatedUser.setPhone("updated_" + Phone); // Updated phone for testing

            // Send PUT request to update the user with new email and phone number
            Response response = actionMethods.updatingUser(Username, updatedUser);
            response.then().log().all();

            // Verify that the response status code is 200 (success)
            Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200, but got: " + response.getStatusCode());

            // For updating, call GET request to verify changes
            Response updatedResponse = actionMethods.gettingUser(Username);
            updatedResponse.then().log().all();

            // Verify the update (email and phone)
            Assert.assertEquals(updatedResponse.jsonPath().getString("email"), "updated_" + Email, "Email not updated");
            Assert.assertEquals(updatedResponse.jsonPath().getString("phone"), "updated_" + Phone, "Phone not updated");
        } catch (Exception e) {
            System.err.println("Error in updating user: " + e.getMessage());
        }
    }

    // Test Case for Delete User
    @Test(priority = 4, dataProvider = "getUserData")
    public void testDeleteUser(String userID, String Username, String FirstName, String LastName, String Email, String Password, String Phone) {
        try {
            // Send DELETE request to delete the user by username
            Response response = actionMethods.deletingUser(Username);
            Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200, but got: " + response.getStatusCode());
        } catch (Exception e) {
            System.err.println("Error in deleting user: " + e.getMessage());
        }
    }
}
