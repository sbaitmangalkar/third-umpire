package com.voter.info.model;

public class SearchResult {
	private String result;
	private String probableAddress;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getProbableAddress() {
		return probableAddress;
	}
	public void setProbableAddress(String probableAddress) {
		this.probableAddress = probableAddress;
	}
	
	@Override
	public String toString() {
		return "SearchResult[" + "result - \n" + result + "\n" + "\nprobableAddress - " + probableAddress + "]";
	}
}
