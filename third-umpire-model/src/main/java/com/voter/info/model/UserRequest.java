package com.voter.info.model;

public class UserRequest {
	private String name;
	private String district;
	private String assemblyConstituencyName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAssemblyConstituencyName() {
		return assemblyConstituencyName;
	}

	public void setAssemblyConstituencyName(String assemblyConstituencyName) {
		this.assemblyConstituencyName = assemblyConstituencyName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assemblyConstituencyName == null) ? 0 : assemblyConstituencyName.hashCode());
		result = prime * result + ((district == null) ? 0 : district.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserRequest))
			return false;
		UserRequest other = (UserRequest) obj;
		if (assemblyConstituencyName == null) {
			if (other.assemblyConstituencyName != null)
				return false;
		} else if (!assemblyConstituencyName.equals(other.assemblyConstituencyName))
			return false;
		if (district == null) {
			if (other.district != null)
				return false;
		} else if (!district.equals(other.district))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserRequest[name=" + name + "," + "district=" + district + "," + "assemblyConstituencyName="
				+ assemblyConstituencyName + "]";
	}

}
