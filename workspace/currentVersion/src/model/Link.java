package model;

public class Link {
	
	public String keggID;
	public String adduct;
	public String isotope;
	public double probability;
	
	public Link(String keggID, String adduct, String isotope, double probability){
		this.keggID = keggID;
		this.adduct = adduct;
		this.isotope = isotope;
		this.probability = probability;
	}
	
	public String getKeggID() {
		return keggID;
	}
	public void setKeggID(String keggID) {
		this.keggID = keggID;
	}
	public String getAdduct() {
		return adduct;
	}
	public void setAdduct(String adduct) {
		this.adduct = adduct;
	}
	public String getIsotope() {
		return isotope;
	}
	public void setIsotope(String isotope) {
		this.isotope = isotope;
	}
	public double getProbability() {
		return probability;
	}
	public void setProbability(double probability) {
		this.probability = probability;
	}

	public String toString(){
		if (this.adduct.equalsIgnoreCase("default"))
			return keggID + ", default, " + probability;
		else
			return keggID + ", " + adduct + ", " + isotope + ", " + probability;
	}
}