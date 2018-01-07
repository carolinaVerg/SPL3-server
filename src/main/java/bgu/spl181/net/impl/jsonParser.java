package bgu.spl181.net.impl;

import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class jsonParser {

	private Movie[] moviesArray;
	private User[] usersData;

	private int movieWithId(int id) {
		for (int i = 0; i < this.moviesArray.length; i++)
			if (this.moviesArray[i].getId() == id)
				return i;
		return -1;
	}

	public void parseMovies() {

		JsonParser parser = new JsonParser();
		File file = new File("Database/example_Movies.json");
		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Reader reader = new InputStreamReader(inputStream);
		JsonElement rootElement = parser.parse(reader);
		JsonObject rootObject = rootElement.getAsJsonObject();
		JsonArray moviesData = rootObject.get("movies").getAsJsonArray();
		this.moviesArray = new Movie[moviesData.size()];

		for (int i = 0; i < moviesData.size(); i++) {

			JsonObject currentMovie = (JsonObject) moviesData.get(i);
			int id = currentMovie.get("id").getAsInt();
			String name = currentMovie.get("name").getAsString();
			int price = currentMovie.get("price").getAsInt();
			int availableAmount = currentMovie.get("availableAmount").getAsInt();
			int totalAmount = currentMovie.get("totalAmount").getAsInt();
			JsonArray bannedCountriesData = currentMovie.get("bannedCountries").getAsJsonArray();
			LinkedList<String> bannedCountriesList = new LinkedList<String>();
			for (int j = 0; j < bannedCountriesData.size(); i++)
				bannedCountriesList.add(bannedCountriesData.get(i).getAsString());

			Movie movieToAdd = new Movie(id, name, price, bannedCountriesList, availableAmount, totalAmount);
			this.moviesArray[i] = movieToAdd;
		}

	}

	public void parseUsers() {

		JsonParser parser = new JsonParser();
		File file = new File("Database/example_Users.json");
		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Reader reader = new InputStreamReader(inputStream);
		JsonElement rootElement = parser.parse(reader);
		JsonObject rootObject = rootElement.getAsJsonObject();
		JsonArray usersArray = rootObject.get("users").getAsJsonArray();
		this.usersData = new User[usersArray.size()];

		for (int i = 0; i < usersArray.size(); i++) {

			String userName = ((JsonObject) usersArray.get(i)).get("username").getAsString();
			String password = ((JsonObject) usersArray.get(i)).get("password").getAsString();
			String country = ((JsonObject) usersArray.get(i)).get("country").getAsString();
			boolean isAdmin = ((JsonObject) usersArray.get(i)).get("type").getAsString().equals("true");
			JsonArray moviesData = ((JsonObject) usersArray.get(i)).get("movies").getAsJsonArray();
			int balance = ((JsonObject) usersArray.get(i)).get("balance").getAsInt();

			rentalMovieUser userToAdd = new rentalMovieUser(userName, password, "?", country, isAdmin);
			userToAdd.addBalance(balance);
			//TODO: Add movies

		}

	}

	public static void main(String[] args) {
	}
}