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

public class moviesParser {

	private static MovieDataBase movieDataBase = MovieDataBase.getInstance();
	
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
}
