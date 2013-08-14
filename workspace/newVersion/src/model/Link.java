package model;

public class Link {
	
	public String keggID;
	public String adduct;
	public String isotope;
	public double probability;
	public int peakIndex;
	
	public Link(String formula, String adduct, String isotope, double probability, int peakIndex){
		this.keggID = formula;
		this.adduct = adduct;
		this.isotope = isotope;
		this.probability = probability;
		this.peakIndex = peakIndex;
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
	public int getPeakIndex() {
		return peakIndex;
	}
	public void setPeakIndex(int peakIndex) {
		this.peakIndex = peakIndex;
	}
	public String toString(){
		if (this.adduct.equalsIgnoreCase("default"))
			return "Probability Identification for peak " + peakIndex + ": " + 
					keggID + ", default, " + probability;
		else
			return "Probability Identification for peak " + peakIndex + ": " + 
					keggID + ", " + adduct + ", " + isotope + ", " + probability;
	}
}