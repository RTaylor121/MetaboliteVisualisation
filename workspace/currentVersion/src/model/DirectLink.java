package model;

public class DirectLink {
	
	private int peakIndex;
	private int idIndex;
	private double probability;
	
	public DirectLink(int peakIndex, int idIndex, double probability) {
		super();
		this.peakIndex = peakIndex;
		this.idIndex = idIndex;
		this.probability = probability;
	}
	
	public int getPeakIndex() {
		return peakIndex;
	}
	public void setPeakIndex(int peakIndex) {
		this.peakIndex = peakIndex;
	}
	public int getIdIndex() {
		return idIndex;
	}
	public void setIdIndex(int idIndex) {
		this.idIndex = idIndex;
	}
	public double getProbability() {
		return probability;
	}
	public void setProbability(double probability) {
		this.probability = probability;
	}
	
}
