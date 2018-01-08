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
		System.out.println(movieDataBase.getAllMovies().get(0).getName());
		//.out.println(movieData.getData()[0].getName());
		//TODO
	}
}




