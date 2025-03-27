package api.payloads;


	public class Userpayload {

	    private int id;
	    private String username;
	    private String firstName;
	    private String lastName;
	    private String email;
	    private String password;
	    private String phone;
	    private int userStatus=0;
	    
	    
	    // Constructor where you can pass values manually
//	    public Userpayload(int id, String username, String firstName, String lastName, 
//	                       String email, String password, String phone, int userStatus) {
//	        setId(id);
//	        setUsername(username);
//	        setFirstName(firstName);
//	        setLastName(lastName);
//	        setEmail(email);
//	        setPassword(password);
//	        setPhone(phone);
//	        setUserStatus(userStatus);
//	    }

	 

	    public Userpayload() {
			// TODO Auto-generated constructor stub
		}

		// Getters and Setters
	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public String getUsername() {
	        return username;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }

	    public String getFirstName() {
	        return firstName;
	    }

	    public void setFirstName(String firstName) {
	        this.firstName = firstName;
	    }

	    public String getLastName() {
	        return lastName;
	    }

	    public void setLastName(String lastName) {
	        this.lastName = lastName;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

	    public String getPhone() {
	        return phone;
	    }

	    public void setPhone(String phone) {
	        this.phone = phone;
	    }

	    public int getUserStatus() {
	        return userStatus;
	    }

	    public void setUserStatus(int userStatus) {
	        this.userStatus = userStatus;
	    }

	  
	
	 // Override toString() method to print user details
    @Override
    public String toString() {
        return "Userpayload{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", userStatus=" + userStatus +
                '}';
    }

	}
