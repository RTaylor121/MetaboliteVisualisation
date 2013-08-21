package model;

import java.io.File;
import java.util.ArrayList;

import peakml.IPeak;
import peakml.IPeakSet;

public class Model {
		
	static IdentificationStore idStore;
	private static PeakStore peakStore;
	boolean pathLoaded, idsLoaded, peaksLoaded, linksGenerated;
	
	int[] selectedPeaks, selectedIds, linkedPeaks, linkedIds;
	
	ArrayList<Integer> linkIdRows;
	
	public Model(){
		idStore = new IdentificationStore(this);
		peakStore = new PeakStore(this);
		pathLoaded = false;
	}
	
	public boolean isPeaksLoaded() {
		return peaksLoaded;
	}

	public void setPeaksLoaded(boolean peaksLoaded) {
		this.peaksLoaded = peaksLoaded;
	}
	public ArrayList<Ident> loadIdentifications(File file){
		return idStore.newparseInputFile(file);
	}

//	public ArrayList<properIdent> loadIdentifications(File file){
//		return idStore.parseInputFile(file);
//	}
	
	public IdentificationStore getIdStore() {
		return idStore;
	}

	public static void setIdStore(IdentificationStore idStore) {
		Model.idStore = idStore;
	}
	
	public IPeakSet<IPeak> loadPeaks(File file){
		return getPeakStore().parseInputFile(file);
	}

	public static void setPeakStore(PeakStore peakStore) {
		Model.peakStore = peakStore;
	}

	public PeakStore getPeakStore() {
		return peakStore;
	}

	public boolean isPathLoaded() {
		return pathLoaded;
	}

	public void setPathLoaded(boolean pathLoaded) {
		this.pathLoaded = pathLoaded;
	}

	public boolean isIdsLoaded() {
		return idsLoaded;
	}

	public void setIdsLoaded(boolean idsLoaded) {
		this.idsLoaded = idsLoaded;
	}

	public int[] getSelectedPeaks() {
		return selectedPeaks;
	}

	public void setSelectedPeaks(int[] selectedPeaks) {
		this.selectedPeaks = selectedPeaks;
	}

	public int[] getSelectedIds() {
		return selectedIds;
	}

	public void setSelectedIds(int[] selectedIds) {
		this.selectedIds = selectedIds;
	}

	public int[] getLinkedPeaks() {
		return linkedPeaks;
	}

	public void setLinkedPeaks(int[] linkedPeaks) {
		this.linkedPeaks = linkedPeaks;
	}

	public int[] getLinkedIds() {
		return linkedIds;
	}

	public void setLinkedIds(int[] linkedIds) {
		this.linkedIds = linkedIds;
	}

	public ArrayList<Integer> getLinkIdRows() {
		return linkIdRows;
	}

	public void setLinkIdRows(ArrayList<Integer> linkIdRows) {
		this.linkIdRows = linkIdRows;
	}
	
}