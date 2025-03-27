package api.tests;

import java.util.Arrays;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import api.endpoints.RestClient;
import api.endpoints.RoutesFinder;
import api.payloads.Pet;
import io.restassured.response.Response;

public class PetTest {

    private static final String AUTH_TOKEN = "your_actual_auth_token_here";  // Set your actual token here
    private static final String BASE_URI = "https://petstore.swagger.io/v2";  // Your base URL for Swagger Petstore
    //private static final String POST_PET_URL = "/pet";  // The endpoint to post a pet
    
    private RestClient restClient;
    private ObjectMapper objectMapper;
    private RoutesFinder routes;
    Pet pet;

    @BeforeClass
    public void setUp() {
        // Initialize RestClient and RoutesFinder here
        Properties prop = new Properties();
        prop.setProperty("token", AUTH_TOKEN);  // Set your token here
        restClient = new RestClient(prop, BASE_URI);  // Initialize RestClient with properties
        objectMapper = new ObjectMapper();
        routes = new RoutesFinder();  // Initialize RoutesFinder
        routes.init_prop();  // Load properties for routes if needed
        
        
        // Create a new Pet object
        pet = new Pet();
        pet.setId(0);

        Pet.Category category = new Pet.Category();
        category.setId(0);
        category.setName("dog");
        pet.setCategory(category);

        pet.setName("doggie");
        pet.setPhotoUrls(Arrays.asList("https://example.com/photo1.jpg"));

        Pet.Tag tag = new Pet.Tag();
        tag.setId(0);
        tag.setName("friendly");
        pet.setTags(Arrays.asList(tag));

        pet.setStatus("available");
    }

    @Test
    public void testCreatePet() throws Exception {
      
        // Convert the Pet object to a JSON string
        String petJson = objectMapper.writeValueAsString(pet);

        // Fetch the full URL for the POST operation from RoutesFinder
        String fullUrl = routes.getFullUrl("post_pet_url");

        // Send POST request to create the pet
        Response response = restClient.post(
                fullUrl,
                "json",
                petJson,
                true,
                true
        );

        // Extract the pet ID from the response (assuming it's returned)
        int petId = response.jsonPath().getInt("id");
        System.out.println("Pet ID: " + petId);

        // Validate the status code
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        // Validate Content-Type header
        Assert.assertEquals(response.getHeader("Content-Type"), "application/json", "Content-Type should be 'application/json'");

        // Parse the response into a Pet object
        Pet responsePet = objectMapper.readValue(response.getBody().asString(), Pet.class);

        // Validate pet's name and status
        Assert.assertEquals(responsePet.getName(), "doggie", "Pet name should be 'doggie'");
        Assert.assertEquals(responsePet.getStatus(), "available", "Pet status should be 'available'");
    }
    
    @Test
    public void testGetPet() throws Exception {
        // Assuming the pet ID is 0 (you can change this as needed)
        int petId = 1;

        // Use RoutesFinder to get the full URL dynamically
        String fullUrl = routes.getFullUrl("get_pet_url");  // Fetching the full URL dynamically from RoutesFinder
        fullUrl = fullUrl.replace("{petId}", String.valueOf(petId));  // Replace the petId placeholder with actual value

        // Send GET request to fetch the pet using RestClient
        Response response = restClient.get(
                fullUrl,         // fullUrl is dynamically fetched using RoutesFinder
                true,            // Include Authorization Header
                true             // Log the request and response
        );

        // Validate the status code (should be 200 for successful retrieval)
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        // Validate the headers in the response
        Assert.assertEquals(response.getHeader("Content-Type"), "application/json", "Content-Type should be 'application/json'");

        // Parse the response into a Pet object
        Pet responsePet = objectMapper.readValue(response.getBody().asString(), Pet.class);

        // Validate the response pet's id, name, and status
        Assert.assertEquals(responsePet.getId(), petId, "Pet ID should match the request ID");
        Assert.assertEquals(responsePet.getName(), "doggie", "Pet name should be 'doggie'");
        Assert.assertEquals(responsePet.getStatus(), "available", "Pet status should be 'available'");

        // Example of printing response for debugging (you can comment it out after testing)
        // System.out.println("Response Body: " + response.getBody().asString());
    }
    @Test(priority = 3)
    public void testUpdatePet() throws Exception {
        // Assuming the pet ID is the one generated during the POST call
        int petId = 123;  // Replace with the actual pet ID you want to update (e.g., from POST response)

        // Step 1: Create a Pet object with the updated details
        Pet pet = new Pet();
        pet.setId(petId);  // Use the existing pet ID that you want to update

        Pet.Category category = new Pet.Category();
        category.setId(0);  // Category ID stays the same
        category.setName("dog");
        pet.setCategory(category);

        pet.setName("doggie-updated");  // Updated pet name
        pet.setPhotoUrls(Arrays.asList("https://example.com/photo2.jpg"));  // Updated photo URL

        Pet.Tag tag = new Pet.Tag();
        tag.setId(0);  // Tag ID stays the same
        tag.setName("playful");
        pet.setTags(Arrays.asList(tag));

        pet.setStatus("sold");  // Updated status

        // Convert Pet object to JSON string
        String petJson = objectMapper.writeValueAsString(pet);

        // Step 2: Send PUT request to update the pet using RestClient
        String fullUrl = routes.getFullUrl("put_pet_url");
        Response putResponse = restClient.put(
                fullUrl,         // fullUrl dynamically fetched from RoutesFinder
                "json",          // Content type
                petJson,         // Updated Pet object in JSON format
                true,            // Include Authorization Header
                true             // Log the request and response
        );

        // Step 3: Validate the PUT response
        // Validate the status code (should be 200 for successful update)
        Assert.assertEquals(putResponse.getStatusCode(), 200, "PUT status code should be 200");

        // Validate the headers in the response
        Assert.assertEquals(putResponse.getHeader("Content-Type"), "application/json", "Content-Type should be 'application/json'");

        // Parse the response into a Pet object
        Pet updatedPet = objectMapper.readValue(putResponse.getBody().asString(), Pet.class);

        // Step 4: Validate the updated pet's details
        Assert.assertEquals(updatedPet.getId(), petId, "Pet ID should match the updated pet ID");
        Assert.assertEquals(updatedPet.getName(), "doggie-updated", "Pet name should be 'doggie-updated'");
        Assert.assertEquals(updatedPet.getStatus(), "sold", "Pet status should be 'sold'");
        Assert.assertEquals(updatedPet.getPhotoUrls().get(0), "https://example.com/photo2.jpg", "Pet photo URL should be updated");
        Assert.assertEquals(updatedPet.getTags().get(0).getName(), "playful", "Pet tag name should be 'playful'");
    }
    
    @Test(priority = 4)
    public void testDeletePet() throws Exception {
        // Assuming the pet ID is obtained after the POST call or previously known
        int petId = 123;  // Replace with the actual pet ID to delete

        // Step 1: Send DELETE request to remove the pet by its ID
        String deleteFullUrl = routes.getFullUrl("delete_pet_url");
        deleteFullUrl = deleteFullUrl.replace("{petId}", String.valueOf(petId));  // Replace {petId} with the actual petId

        Response deleteResponse = restClient.delete(
                deleteFullUrl,     // fullUrl dynamically fetched from RoutesFinder
                true,              // Include Authorization Header
                true               // Log the request and response
        );

        // Step 2: Validate the DELETE response
        // Validate the status code (should be 200 for successful deletion)
       // Assert.assertEquals(deleteResponse.getStatusCode(), 200, "DELETE status code should be 200");

        // Validate the headers in the response
        Assert.assertEquals(deleteResponse.getHeader("Content-Type"), "application/json", "Content-Type should be 'application/json'");

        // Step 3: Verify that the pet has been deleted (send a GET request to check if the pet still exists)
        String getFullUrl = routes.getFullUrl("get_pet_url");
        getFullUrl = getFullUrl.replace("{petId}", String.valueOf(petId));  // Use the same petId to verify deletion

        // Send GET request to fetch the pet by its ID after deletion
        Response getResponse = restClient.get(
                getFullUrl,      // fullUrl for GET request after deletion
                true,            // Include Authorization Header
                true             // Log the request and response
        );

        // Step 4: Validate the GET response (should return 404 if the pet is deleted)
       // Assert.assertEquals(getResponse.getStatusCode(), 404, "GET status code should be 404 after deletion");
        Assert.assertTrue(getResponse.getBody().asString().contains("Pet not found"), "Response body should contain 'Pet not found'");
    }



}

