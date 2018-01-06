package bgu.spl181.net.impl;

import java.util.List;

public class Movie {
	private int id;
	private String name;
	private long price;
	private List<String> bannedCountries;
	private int availableAmount;
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
}
