package bgu.spl181.net.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class usersParser {

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
			rentalMovieUser userToAdd=null;
			if(isAdmin)
				userToAdd = new rentalMovieUser(userName, password, country,"admin" );
			else userToAdd = new rentalMovieUser(userName, password, country,"normal" );
			userToAdd.addBalance(balance);
			for (int j = 0; j < moviesData.size(); j++) {
				userToAdd.addMovie(movieDataBase.getMovie(currentUser.get("name").toString()));
			}
			userDataBase.addUser(userToAdd);

		}
	}
}
