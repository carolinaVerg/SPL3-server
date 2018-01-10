package bgu.spl181.net.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonWriter;

public class jsonParser {

	private static MovieDataBase movieDataBase = MovieDataBase.getInstance();
	private static UsersDataBase userDataBase = UsersDataBase.getInstance();
	private static Gson gsonParser = new Gson();
	private static jsonParser instance  = new jsonParser();

	public static void readMoviesFromFile() {

		try {
			movieDataBase = gsonParser.fromJson(new FileReader("Database/example_Movies.json"), MovieDataBase.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//System.out.println("readMoviesFromFile - COMPLETED!");
		
	}

	public static void readUsersFromFile() {
		
		try {
			userDataBase = gsonParser.fromJson(new FileReader("Database/example_Users.json"), UsersDataBase.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//System.out.println("readUsersFromFile - COMPLETED!");
	}

	public static void writeMoviesToFile() {
	
		//Keep For Checking
		/*LinkedList<String> list = new LinkedList<String>();
		list.add("country1");
		list.add("country2");
		Movie movieToAdd = new Movie(4, "nameOfMovie", 35, list, 12, 2409);
		movieDataBase.addMovie(movieToAdd);*/
		
		try (Writer writer = new FileWriter("Database/example_Movies.json")) {
			gsonParser.toJson(movieDataBase, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//System.out.println("writeMoviesToFile - COMPLETED!");
	}

	public static void writeUsersToFile() {
		
		//Keep For Checking
		/*rentalMovieUser userToAdd = new rentalMovieUser("tryUser", "tryUser", "Israel", "normal");
		LinkedList<String> list1 = new LinkedList<String>();
		LinkedList<String> list2 = new LinkedList<String>();
		list1.add("country11");
		list1.add("country12");
		list2.add("country21");
		list2.add("country22");
		Movie movie1 = new Movie(1, "name1", 24, list1, 67, 123);
		Movie movie2 = new Movie(2, "name2", 9, list2, 43, 987);
		userToAdd.addMovie(movie1);
		userToAdd.addMovie(movie2);
		userDataBase.addUser(userToAdd);*/
		
		
		try (Writer writer = new FileWriter("Database/example_Users.json")) {
			gsonParser.toJson(userDataBase, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//System.out.println("writeUsersToFile - COMPLETED!");

	}

	public static jsonParser getInstance() {
		return instance;
	}
	
	public static void main(String[] args) {
		
		/*readMoviesFromFile();
		readUsersFromFile();
		writeMoviesToFile();
		writeUsersToFile();
		System.out.println("STOP");*/
		
	}
}
