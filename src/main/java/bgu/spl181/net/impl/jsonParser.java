package bgu.spl181.net.impl;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;


public class jsonParser {

	private static MovieDataBase movieDataBase = MovieDataBase.getInstance();
	private static UsersDataBase userDataBase = UsersDataBase.getInstance();

	public static void parseUsersJSON() {

		JSONParser parser = new JSONParser();
		File srcFile = new File("Database/example_Users.json");
		Reader reader = null;
		try {
			reader = new FileReader(srcFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Object jsonObj = null;
		try {
			jsonObj = parser.parse(reader);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		JSONObject jsonObject = (JSONObject) jsonObj;
		JSONArray usersData = (JSONArray) jsonObject.get("users");

		for (int i = 0; i < usersData.size(); i++) {

			JSONObject currentUser = (JSONObject) usersData.get(i);
			String userName = currentUser.get("username").toString();
			String password = currentUser.get("password").toString();
			String country = currentUser.get("country").toString();
			boolean isAdmin = currentUser.get("type").toString().equals("true");
			JSONArray moviesData = (JSONArray) currentUser.get("movies");
			int balance = Integer.parseInt(currentUser.get("balance").toString());
			rentalMovieUser userToAdd = new rentalMovieUser(userName, password, country, isAdmin);
			userToAdd.addBalance(balance);
			for (int j = 0; j < moviesData.size(); j++) {
				userToAdd.addMovie(movieDataBase.getMovie(currentUser.get("name").toString()));
			}
			userDataBase.addUser(userToAdd);

		}
	}

	public static void parseMoviesJSON() {
		JSONParser parser = new JSONParser();
		File srcFile = new File("Database/example_Movies.json");
		Reader reader = null;
		try {
			reader = new FileReader(srcFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Object jsonObj = null;
		try {
			jsonObj = parser.parse(reader);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		JSONObject jsonObject = (JSONObject) jsonObj;
		JSONArray moviesData = (JSONArray) jsonObject.get("movies");

		for (int i = 0; i < moviesData.size(); i++) {

			JSONObject currentMovie = (JSONObject) moviesData.get(i);
			int id = Integer.parseInt(currentMovie.get("id").toString());
			String name = currentMovie.get("name").toString();
			int price = Integer.parseInt(currentMovie.get("price").toString());
			int availableAmount = Integer.parseInt(currentMovie.get("availableAmount").toString());
			int totalAmount = Integer.parseInt(currentMovie.get("totalAmount").toString());
			JSONArray bannedCountriesData = (JSONArray) currentMovie.get("bannedCountries");
			LinkedList<String> bannedCountriesList = new LinkedList<String>();
			for (int j = 0; j < bannedCountriesData.size(); i++)
				bannedCountriesList.add(bannedCountriesData.get(i).toString());

			Movie movieToAdd = new Movie(id, name, price, bannedCountriesList, availableAmount, totalAmount);
			movieDataBase.addMovie(movieToAdd);
		}
	}
	
	public static void main(String[] args) {
		Movie[] mv=null;
		//movieData movieData = null;
		Movie movie=null;
		Gson gson = new Gson();
		try {
			//movieData = gson.fromJson(new FileReader("Database/example_Movies.json"),movieData.class);
			movie = gson.fromJson(new FileReader("Database/oneMovie.json"), Movie.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(movie.getName());
		//.out.println(movieData.getData()[0].getName());
		//TODO
	}
}




