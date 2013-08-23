package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class IdentificationStore {
	
	private ArrayList<properIdent> identifications;
	private ArrayList<Ident> newIds;
	private String sortField;
	private int sortOrder;
	private static Model theModel;
	private int[] currentLinkIdentifications;
	private int[] currentPathIdentifications;
	private ArrayList<Integer> linkIdRows;
	
	public IdentificationStore(Model model){
		theModel = model;
	}
	// order: 1 for high-low, 0 for low-high //
	public static int partition(
			ArrayList<Ident> array, int left, int right, int pivotIndex, String field, int order){
		
		double pivotDouble = 0;
		String pivotString = null;
		Ident temp = array.get(pivotIndex);
		
		if (field.equalsIgnoreCase("kegg id")){
			pivotString = array.get(pivotIndex).getKegg();
		}
		else if (field.equalsIgnoreCase("id"))
			pivotDouble = Double.parseDouble(array.get(pivotIndex).getId());
		else if (field.equalsIgnoreCase("name"))
			pivotString = array.get(pivotIndex).getName();
		else if (field.equalsIgnoreCase("p1"))
			pivotDouble = array.get(pivotIndex).getProbabilities()[0];
		else if (field.equalsIgnoreCase("p2"))
			pivotDouble = array.get(pivotIndex).getProbabilities()[1];
		else if (field.equalsIgnoreCase("p3"))
			pivotDouble = array.get(pivotIndex).getProbabilities()[2];
		else if (field.equalsIgnoreCase("p4"))
			pivotDouble = array.get(pivotIndex).getProbabilities()[3];
		else if (field.equalsIgnoreCase("p5"))
			pivotDouble = array.get(pivotIndex).getProbabilities()[4];
		else if (field.equalsIgnoreCase("pcombined"))
			pivotDouble = array.get(pivotIndex).getCombinedProb();
		
		array.set(pivotIndex, array.get(right));
		array.set(right, temp);
		
		int storeIndex = left;
		double comparisonDouble = 0;
		String comparisonString = null;
		for (int i = left; i < right; i++){
			if (field.equalsIgnoreCase("kegg id"))
				comparisonString = array.get(i).getKegg();
			else if (field.equalsIgnoreCase("id"))
				comparisonDouble = Double.parseDouble(array.get(i).getId());
			else if (field.equalsIgnoreCase("name"))
				comparisonString = array.get(i).getName();
			else if (field.equalsIgnoreCase("p1"))
				comparisonDouble = array.get(i).getProbabilities()[0];
			else if (field.equalsIgnoreCase("p2"))
				comparisonDouble = array.get(i).getProbabilities()[1];
			else if (field.equalsIgnoreCase("p3"))
				comparisonDouble = array.get(i).getProbabilities()[2];
			else if (field.equalsIgnoreCase("p4"))
				comparisonDouble = array.get(i).getProbabilities()[3];
			else if (field.equalsIgnoreCase("p5"))
				comparisonDouble = array.get(i).getProbabilities()[4];
			else if (field.equalsIgnoreCase("pcombined"))
				comparisonDouble = array.get(i).getCombinedProb();
			int stringComparison = 0;
			boolean comparison = false;
			if (order == 1){
				if (field.equalsIgnoreCase("name") || field.equalsIgnoreCase("kegg id")){
					stringComparison = comparisonString.compareToIgnoreCase(pivotString);
				}
				else {
					comparison = comparisonDouble > pivotDouble;
				}
			}
			else {
				if (field.equalsIgnoreCase("name") || field.equalsIgnoreCase("kegg id")){
					stringComparison = pivotString.compareToIgnoreCase(comparisonString);
				}
				else {
					comparison = comparisonDouble < pivotDouble;
				}
			}
			if (field.equalsIgnoreCase("name") || field.equalsIgnoreCase("kegg id")){
				if (stringComparison < 0){
					temp = array.get(i);
					array.set(i, array.get(storeIndex));
					array.set(storeIndex, temp);
					storeIndex++;
				}
			}
			else {
				if (comparison){
					temp = array.get(i);
					array.set(i, array.get(storeIndex));
					array.set(storeIndex, temp);
					storeIndex++;
				}
			}
		}
		
		temp = array.get(storeIndex);
		array.set(storeIndex, array.get(right));
		array.set(right, temp);
		
		return storeIndex;
	}
	
	
	// order: 1 for high-low, 0 for low-high //
	public void sortIdents(
			ArrayList<Ident> array, int left, int right, String field, int order){
		if (left < right) {
			int pivotIndex = left + (right-left)/2;
			int pivotNewIndex = partition(array, left, right, pivotIndex, field, order);
			sortIdents(array, left, pivotNewIndex - 1, field, order);
			sortIdents(array, pivotNewIndex + 1, right, field, order);
		}
	}
	
	// order: 1 for high-low, 0 for low-high //
	public void sortInit(String field, int order){
		sortIdents(newIds, 0, newIds.size() - 1, field, order);
	}
	
	////////////////////////////////////////////////////////////////////
	
	public ArrayList<Ident> newparseInputFile(File file){
		ArrayList<Ident> returnVals = null;
		returnVals = new ArrayList<Ident>();
		BufferedReader br = null;
		String[] splitLine;
		
		try {
			
			String sCurrentLine;
			int j;
			br = new BufferedReader(new FileReader(file));
			sCurrentLine = br.readLine();
			while ((sCurrentLine = br.readLine()) != null) {
				j = 0;
				splitLine = sCurrentLine.split("[\t]", 0);
				double[] probs = new double[splitLine.length - 4];
				while(j < splitLine.length - 4){
					probs[j] = new Double(splitLine[j + 3]);
					j++;
				}
				if (probs[0] > 0)
					returnVals.add(new Ident(
							splitLine[0], splitLine[2], splitLine[1], probs, Double.parseDouble(splitLine[splitLine.length - 1]), -1));
			}
 
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sortIdents(returnVals, 0, returnVals.size() - 1, "kegg", 0);
		sortField = "kegg";
		sortOrder = 1;
		
		newIds = returnVals;
		return returnVals;
	}
	
	public int findIdent(ArrayList<Ident> array, int key, int min, int max){
		if (max < min)
			return -1;
		else {
			int mid = min + (max-min)/2;
			System.out.println(Integer.parseInt(array.get(mid).getKegg().substring(1)) + " vs " + key);
			if (Integer.parseInt(array.get(mid).getKegg().substring(1)) > key)
				return (findIdent(array, key, min, mid -1));
			else if (Integer.parseInt(array.get(mid).getKegg().substring(1)) < key)
				return (findIdent(array, key, mid + 1, max));
			else
				return mid;
		}
	}

	public ArrayList<properIdent> getIdentifications() {
		return identifications;
	}

	public void setIdentifications(ArrayList<properIdent> identifications) {
		this.identifications = identifications;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	public ArrayList<Ident> getNewIds() {
		return newIds;
	}
	public void setNewIds(ArrayList<Ident> newIds) {
		this.newIds = newIds;
	}
	public int[] getCurrentLinkIdentifications() {
		return currentLinkIdentifications;
	}
	public void setCurrentLinkIdentifications(int[] currentLinkIdentifications) {
		this.currentLinkIdentifications = currentLinkIdentifications;
	}
	public int[] getCurrentPathIdentifications() {
		return currentPathIdentifications;
	}
	public void setCurrentPathIdentifications(int[] currentPathIdentifications) {
		this.currentPathIdentifications = currentPathIdentifications;
	}
	public ArrayList<Integer> getLinkIdRows() {
		return linkIdRows;
	}
	public void setLinkIdRows(ArrayList<Integer> linkIdRows) {
		this.linkIdRows = linkIdRows;
	}
	
	
}