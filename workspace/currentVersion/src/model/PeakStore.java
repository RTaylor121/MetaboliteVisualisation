package model;

import java.awt.Desktop.Action;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import peakml.IPeak;
import peakml.IPeakSet;
import peakml.Peak;
import peakml.io.Header;
import peakml.io.ParseResult;
import peakml.io.peakml.PeakMLParser;

public class PeakStore {
	
	static IPeakSet<IPeak> peakset;
	Vector<IPeak> vectorPeaks;
	static ArrayList<Link[]> linkingData;
	static Model theModel;
	
	public int sort = 1;
	
	public PeakStore(Model model){
		theModel = model;
	}
	
	@SuppressWarnings("unchecked")
//	public Vector<IPeak> parseInputFile(File file){
	public ArrayList<IPeak> parseInputFile(File file){
		try {
			System.out.println(file.getAbsolutePath());
        	ParseResult result = PeakMLParser.parse(new FileInputStream(file), true);
//			Header header = result.header;
			peakset = (IPeakSet<IPeak>) result.measurement;
			linkingData = new ArrayList<Link[]>();
			getProbabilityAttributes();
			vectorPeaks = peakset.getPeaks();
			sortInit("mass", 0);
			ArrayList<IPeak> alPeaks = new ArrayList<IPeak>();
			for (IPeak peak : vectorPeaks)
				alPeaks.add(peak);
			return alPeaks;
		}
		catch (Exception e)
		{
			System.out.println("File operation failed.");
		}
		return null;
	}
	
	public IPeakSet<IPeak> getPeakset() {
		return peakset;
	}

	public static void setPeakset(IPeakSet<IPeak> peakset) {
		PeakStore.peakset = peakset;
	}

	public ArrayList<Link[]> getLinkingData() {
		return linkingData;
	}

	public static void setLinkingData(ArrayList<Link[]> linkingData) {
		PeakStore.linkingData = linkingData;
	}

	public static void addLinkData(String anno, int peakIndex){
		System.out.println("ADDING NEW LINK DATA: " + anno);
		String[] annos;
		String[] splitLine;
		Link[] newLinks;
		if (anno.contains(";")){
			 annos = anno.split(";", 0);
			 newLinks = new Link[annos.length];
			 for (int i = 0; i < annos.length; i++){
				 splitLine = annos[i].split(",", 0);
				 if (splitLine.length == 3){
						newLinks[i] = new Link(splitLine[0], "default", null, Double.parseDouble(splitLine[2]), peakIndex, -1);
					} else if (splitLine.length == 4) {
						newLinks[i] = new Link(splitLine[0], splitLine[1], splitLine[2], Double.parseDouble(splitLine[3]), peakIndex, -1);
					}
			 }
		} else {
			newLinks = new Link[1];
			splitLine = anno.split(",", 0);
			if (splitLine.length == 3){
				newLinks[0] = new Link(splitLine[0], "default", null, Double.parseDouble(splitLine[2]), peakIndex, -1);
			} else if (splitLine.length == 4) {
				newLinks[0] = new Link(splitLine[0], splitLine[1], splitLine[2], Double.parseDouble(splitLine[3]), peakIndex, -1);
			}
		}
		linkingData.add(newLinks);
	}
    
    public static void getProbabilityAttributes(){
		
		String annotation;
		linkingData.clear();
		int peakCounter = 0;
		for (IPeak peak : peakset){
			if (peak.getAnnotation("probabilityIdentification") != null){
				annotation = peak.getAnnotation("probabilityIdentification").getValue();
				addLinkData(annotation, peakCounter);
			} else {
				linkingData.add(null);
				System.out.println("no anno");
			}
			peakCounter++;
		}
	}

	public Vector<IPeak> getSelectedPeaks(int[] selected) {
		Vector<IPeak> selectedPeaks = new Vector<IPeak>();
		if (selected.length == 0){
			return null;
		} else {
			for (int i = 0; i < selected.length; i++){
				selectedPeaks.add(peakset.get(selected[i]));
			}
			return selectedPeaks;
		}
	}
	
	// order: 1 for high-low, 0 for low-high //
	public static int partition(
			Vector<IPeak> array, int left, int right, int pivotIndex, String field, int order){
		IPeak temp = array.get(pivotIndex);
		Link[] tempLink = linkingData.get(pivotIndex);
		double pivotValue = 0;
		if (field.equals("mass"))
			pivotValue = array.get(pivotIndex).getMass();
		else if (field.equals("intensity"))
			pivotValue = array.get(pivotIndex).getIntensity();
		else if (field.equals("retention"))
			pivotValue = array.get(pivotIndex).getRetentionTime();
		else if (field.equals("probability"))
			if (linkingData.get(pivotIndex) != null)
				pivotValue = linkingData.get(pivotIndex)[0].getProbability();
			else
				pivotValue = 0;
		else
			pivotValue = 0;
		
		array.set(pivotIndex, array.get(right));
		linkingData.set(pivotIndex, linkingData.get(right));
		array.set(right, temp);
		linkingData.set(right, tempLink);
		
		int storeIndex = left;
		double comparisonValue = 0;
		
		for (int i = left; i < right; i++){
			if (field.equals("mass"))
				comparisonValue = array.get(i).getMass();
			else if (field.equals("intensity"))
				comparisonValue = array.get(i).getIntensity();
			else if (field.equals("retention"))
				comparisonValue = array.get(i).getRetentionTime();
			else if (field.equals("probability")){
				if (linkingData.get(i) != null)
					comparisonValue = linkingData.get(i)[0].getProbability();
				else
					comparisonValue = 0;
			} else {
				comparisonValue = 0;
			}
			boolean comparison;
			if (order == 1){
				comparison = comparisonValue > pivotValue;
			}
			else {
				comparison = comparisonValue < pivotValue;
			}
			if (comparison){
				temp = array.get(i);
				tempLink = linkingData.get(i);
				array.set(i, array.get(storeIndex));
				linkingData.set(i, linkingData.get(storeIndex));
				array.set(storeIndex, temp);
				linkingData.set(storeIndex, tempLink);
				storeIndex++;
			}
		}
		temp = array.get(storeIndex);
		tempLink = linkingData.get(storeIndex);
		array.set(storeIndex, array.get(right));
		linkingData.set(storeIndex, linkingData.get(right));
		array.set(right, temp);
		linkingData.set(right, tempLink);
		return storeIndex;
	}
	
	
	// order: 1 for high-low, 0 for low-high //
	public void sortPeaks(
			Vector<IPeak> array, int left, int right, String field, int order){
		if (left < right) {
			int pivotIndex = left + (right-left)/2;
			int pivotNewIndex = partition(array, left, right, pivotIndex, field, order);
			sortPeaks(array, left, pivotNewIndex - 1, field, order);
			sortPeaks(array, pivotNewIndex + 1, right, field, order);
		}
	}
	
	// order: 1 for high-low, 0 for low-high //
	public void sortInit(String field, int order){
		sortPeaks(vectorPeaks, 0, vectorPeaks.size() - 1, field, order);
	}
	
	
}
