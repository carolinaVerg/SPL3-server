package bgu.spl181.net.impl;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class movieNameId implements Serializable {

	@SerializedName("name")
	private String name;
	@SerializedName("id")
	private int id;
	
	public movieNameId(String name, int id) {
		this.name=name;
		this.id=id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getId() {
		return this.id;
	}

}
