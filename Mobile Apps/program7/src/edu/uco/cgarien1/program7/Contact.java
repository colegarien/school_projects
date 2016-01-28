// Cole Garien

package edu.uco.cgarien1.program7;

public class Contact {
	private String lastName = "";
	private String firstName ="";
	private String email = "";
	private String number = "";
	private String url = "";
	
	public Contact(String firstName, String lastName, String email, String number, String url){
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.number = number;
		this.url = url;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}



	@Override
	public String toString() {
		return lastName;
	}
}