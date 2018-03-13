package com.voter.info.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * @author Shyam | catch.shyambaitmangalkar@gmail.com
 *
 */
public class Voter {
	private SimpleStringProperty fullName;
	private SimpleStringProperty dependent;
	private SimpleStringProperty dependentName;
	private SimpleStringProperty address;
	private SimpleStringProperty age;
	private SimpleStringProperty sex;
	
	public Voter() {
		
	}
	
	public Voter(String fullName, String dependent, String dependentName, String address, String age, String sex) {
		this.fullName = new SimpleStringProperty(fullName);
		this.dependent = new SimpleStringProperty(dependent);
		this.dependentName = new SimpleStringProperty(dependentName);
		this.address = new SimpleStringProperty(address);
		this.age = new SimpleStringProperty(age);
		this.sex = new SimpleStringProperty(sex);
	}

	public String getFullName() {
		return fullName.get();
	}

	public void setFullName(String fullName) {
		this.fullName.set(fullName);
	}

	public String getDependent() {
		return dependent.get();
	}

	public void setDependent(String dependent) {
		this.dependent.set(dependent);
	}

	public String getDependentName() {
		return dependentName.get();
	}

	public void setDependentName(String dependentName) {
		this.dependentName.set(dependentName);
	}

	public String getAddress() {
		return address.get();
	}

	public void setAddress(String address) {
		this.address.set(address);
	}

	public String getAge() {
		return age.get();
	}

	public void setAge(String age) {
		this.age.set(age);
	}

	public String getSex() {
		return sex.get();
	}

	public void setSex(String sex) {
		this.sex.set(sex);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result + ((dependent == null) ? 0 : dependent.hashCode());
		result = prime * result + ((dependentName == null) ? 0 : dependentName.hashCode());
		result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Voter))
			return false;
		Voter other = (Voter) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (dependent == null) {
			if (other.dependent != null)
				return false;
		} else if (!dependent.equals(other.dependent))
			return false;
		if (dependentName == null) {
			if (other.dependentName != null)
				return false;
		} else if (!dependentName.equals(other.dependentName))
			return false;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (sex == null) {
			if (other.sex != null)
				return false;
		} else if (!sex.equals(other.sex))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Voter[fullName: " + fullName.get() + "; " + "dependent: " + dependent.get() + "; " + "dependentName: "
				+ dependentName.get() + "; " + "address: " + address.get() + "; " + "age: " + age.get() + "; " + "sex: "
				+ sex.get() + "]";
	}
}
