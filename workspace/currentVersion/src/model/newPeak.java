package model;

import peakml.IPeak;

public abstract class newPeak extends IPeak{

	private String id;

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public newPeak() {
		super();
		id = null;
		// TODO Auto-generated constructor stub
	}
	
	
	
}
