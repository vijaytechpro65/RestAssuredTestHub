package api.endpoints;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class RoutesFinder {
    
    // Property object to hold the loaded properties
    public Properties prop;
    
    // Method to load the properties file
    public void init_prop() {
        prop = new Properties();
        try (FileInputStream ip = new FileInputStream(".\\src\\test\\resource\\config\\config.properties")) {
            prop.load(ip); // Load the properties from the file
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Method to get the full URL by appending the base_url with the specific path
    public String getFullUrl(String key) {
        String baseUrl = prop.getProperty("base_url");
        String route = prop.getProperty(key);
        return baseUrl + route;  // Combine base_url with the specific route
    }
}
