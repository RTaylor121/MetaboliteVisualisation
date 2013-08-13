
public class properIdent {

	public String kegg;
	public double[] probabilities;
	
	public properIdent(String kegg, double[] probabilities){
		this.kegg = kegg;
		this.probabilities = probabilities;
	}
	
	public properIdent(properIdent ident){
		this.kegg = ident.kegg;
		this.probabilities = ident.probabilities;
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
	
}
