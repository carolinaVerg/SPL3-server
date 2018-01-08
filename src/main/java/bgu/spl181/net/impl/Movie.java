package bgu.spl181.net.impl;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Movie implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("id")
	private int id;
	@SerializedName("name")
	private String name;
	@SerializedName("price")
	private long price;
	@SerializedName("bannedCountries")
	private List<String> bannedCountries;
	@SerializedName("availableAmount")
	private int availableAmount;
	@SerializedName("totalAmount")
	private int totalAmount;
 
	public Movie(int id,
			String name,
			long price,
			List<String> bannedCountries,
			int availableAmount,
			int totalAmount) {
		this.id=id;
		this.name=name;
		this.price=price;
		this.bannedCountries=bannedCountries;
		this.availableAmount=availableAmount;
		this.totalAmount=totalAmount;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public long getPrice() {
		return this.price;
	}
	
	public void setPrice(long price) {
		this.price=price;
	}
	
	public List<String> getBannedCountries(){
		return this.bannedCountries;
	}
	
	public int getAvailbleAmount() {
		return this.availableAmount;
	}
	
	public int getTotalAmount() {
		return this.totalAmount;
	}
	
	public boolean isBanned(String country) {
		return this.bannedCountries.contains(country);
	}
	
	public void reduceAmount() {
		this.availableAmount--;
	}
	public void incAmount() {
		this.availableAmount++;
	}
	
	@Override
	public String toString() {
		return "id: "+this.id+", name: "+this.name+", price: "+this.price+", bannedCountries: "
					+this.bannedCountries.toString()+", availableAmount: "+this.availableAmount+", totalAmount: "
					+this.totalAmount;
	}
}
