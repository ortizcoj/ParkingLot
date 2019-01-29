package org.kodejava.example.jackson;

import java.util.Random;

import com.google.gson.*;


public class User {
	
	private String name;
	private String email;
	private byte[] password;
	private String carMake;
	private String carModel;
	private String carColor;
	
	public User(String uname, String uemail, byte[] upassword, String ucarMake, String ucarModel, String ucarColor) {
		name = uname;
		email = uemail;
		password = upassword;
		carMake = ucarMake;
		carModel = ucarModel;
		carColor = ucarColor;
	}
	
	public String gsonify() {
		
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	public String getCarMake() {
		return carMake;
	}

	public void setCarMake(String carMake) {
		this.carMake = carMake;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public String getCarColor() {
		return carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}

	public static void main(String args[]) {
		
		byte[] b = new byte[5];
		Random ran = new Random();
		ran.nextBytes(b);
		User user = new User("alex", "alex@site.com", b, "honda", "truck", "grey");
		
		String json = user.gsonify();
		System.out.println(json);
		
	}
	
}
