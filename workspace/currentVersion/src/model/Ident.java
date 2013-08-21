package model;

public class Ident {

	public String id;
	public String kegg;
	public String name;
	public double[] probabilities;
	public double combinedProb;
	private int buttonIndex;
	
	public Ident(String id, String name, String kegg, double[] probabilities, double combProb, int buttonIndex){
		this.id = id;
		this.kegg = kegg;
		this.name = name;
		this.probabilities = probabilities;
		this.combinedProb = combProb;
		this.buttonIndex = buttonIndex;
	}
	
	public Ident(Ident ident){
		this.id = ident.id;
		this.kegg = ident.kegg;
		this.name = ident.name;
		this.probabilities = ident.probabilities;
		this.combinedProb = ident.combinedProb;
		this.buttonIndex = ident.buttonIndex;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getKegg() {
		return kegg;
	}
	public void setKegg(String kegg) {
		this.kegg = kegg;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public double[] getProbabilities() {
		return probabilities;
	}
	public void setProbabilities(double[] probabilities) {
		this.probabilities = probabilities;
	}

	public double getCombinedProb() {
		return combinedProb;
	}
	public void setCombinedProb(double combinedProb) {
		this.combinedProb = combinedProb;
	}

	public int getButtonIndex() {
		return buttonIndex;
	}

	public void setButtonIndex(int buttonIndex) {
		this.buttonIndex = buttonIndex;
	}

	public String toString(){
		return id + ", " + kegg + ", " + name + ", " + probabilities[0] + ", " + combinedProb;
	}
}
