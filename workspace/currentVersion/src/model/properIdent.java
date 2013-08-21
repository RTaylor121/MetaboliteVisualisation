package model;

public class properIdent {

	public String id;
	public String kegg;
	public double[] probabilities;
	private int buttonIndex;
	
	public properIdent(String kegg, double[] probabilities, int buttonIndex, String id){
		this.kegg = kegg;
		this.probabilities = probabilities;
		this.buttonIndex = buttonIndex;
		this.id = id;
	}
	
	public properIdent(properIdent ident){
		this.kegg = ident.kegg;
		this.probabilities = ident.probabilities;
		this.buttonIndex = ident.buttonIndex;
		this.id = ident.id;
	}
	
	public String getKegg() {
		return kegg;
	}
	public void setKegg(String kegg) {
		this.kegg = kegg;
	}
	public double[] getProbabilities() {
		return probabilities;
	}
	public void setProbabilities(double[] probabilities) {
		this.probabilities = probabilities;
	}

	public int getButtonIndex() {
		return buttonIndex;
	}

	public void setButtonIndex(int buttonIndex) {
		this.buttonIndex = buttonIndex;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
