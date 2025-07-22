package io.github.zufarm.library.models;

public class Person {
	private String full_name;
	private int birth_year;
	
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}
	public int getBirth_year() {
		return birth_year;
	}
	public void setBirth_year(int birth_year) {
		this.birth_year = birth_year;
	}
	public Person(String full_name, int birth_year) {
		super();
		this.full_name = full_name;
		this.birth_year = birth_year;
	}
}
