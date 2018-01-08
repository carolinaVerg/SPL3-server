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
	
	public static void main(String[] args) {
		Movie movie=null;
		Gson gson = new Gson();
		try {
			movieDataBase = gson.fromJson(new FileReader("Database/example_Movies.json"), MovieDataBase.class);
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
		LinkedList<String> toAdd = new LinkedList<String>();
		toAdd.add("country1");
		toAdd.add("country2");
		System.out.println(movieDataBase.getAllmovies1().get(0).getName());
		movieDataBase.addMovie(new Movie(24, "checkMovie", 9, toAdd, 1, 8));
		System.out.println("Done 1");
		//.out.println(movieData.getData()[0].getName());
		Gson gson2= new Gson();
		ArrayList<Movie> arrList = movieDataBase.getAllmovies1();
		try(Writer writer = new FileWriter("Database/example_Movies.json")){
			Gson gson3 = new GsonBuilder().create();
			gson3.toJson(movieDataBase,writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done 2");
		//TODO
	}
}




