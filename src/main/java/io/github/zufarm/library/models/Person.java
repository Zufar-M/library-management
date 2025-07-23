package io.github.zufarm.library.models;

public class Person {
	private String fullName;
	private int birthYear;
	
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
	public Person(String fullName, int birthYear) {
		this.fullName = fullName;
		this.birthYear = birthYear;
	}
	// For @ModelAttribute
	public Person() {
		
	}
	
}
