package io.github.zufarm.library.models;

public class Person {
	private int id;
	private String fullName;
	private int birthYear;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public int getBirthYear() {
		return birthYear;
	}
	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}
	// For @ModelAttribute
	public Person() {
		
	}
	public Person(int id, String fullName, int birthYear) {
		this.id = id;
		this.fullName = fullName;
		this.birthYear = birthYear;
	}
	
}
