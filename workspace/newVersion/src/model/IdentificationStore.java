package model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class IdentificationStore {
	
	private static properIdent[] identifications;
	
	public properIdent[] getIdentifications() {
		return identifications;
	}

	public static void setIdentifications(properIdent[] identifications) {
		IdentificationStore.identifications = identifications;
	}

	public static int partition(properIdent[] array, int left, int right, int pivotIndex){
		properIdent temp = array[pivotIndex];
		int pivotValue = Integer.parseInt(array[pivotIndex].getKegg().substring(1));
		array[pivotIndex] = array[right];
		array[right] = temp;
		int storeIndex = left;
		for (int i = left; i < right; i++){
			if (Integer.parseInt(array[i].getKegg().substring(1)) < pivotValue){
				temp = array[i];
				array[i] = array[storeIndex];
				array[storeIndex] = temp;
				storeIndex++;
			}
		}
		temp = array[storeIndex];
		array[storeIndex] = array[right];
		array[right] = temp;
		return storeIndex;
	}
	
	public static void sortIdents(properIdent[] array, int left, int right){
		if (left < right) {
			int pivotIndex = left + (right-left)/2;
			System.out.println(pivotIndex);
			int pivotNewIndex = partition(array, left, right, pivotIndex);
			sortIdents(array, left, pivotNewIndex - 1);
			sortIdents(array, pivotNewIndex + 1, right);
		}
	}

	public static int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	
	public properIdent[] parseInputFile(File file){
		
		properIdent[] returnVals = null;
		try {
			returnVals = new properIdent[countLines(file.getAbsolutePath()) - 1];
		} catch (IOException e1) {
			System.out.println("Problem when counting lines in csv");
			e1.printStackTrace();
		}
		BufferedReader br = null;
		String[] splitLine;
		
		try {
			
			String sCurrentLine;
			int i = 0;
			int j;
			br = new BufferedReader(new FileReader(file));
			sCurrentLine = br.readLine();
			while ((sCurrentLine = br.readLine()) != null) {
				j = 0;
				splitLine = sCurrentLine.split(",", 0);
				double[] probs = new double[splitLine.length - 2];
				while(j < splitLine.length - 2){
					probs[j] = new Double(splitLine[j + 2]);
					j++;
				}
				returnVals[i] = new properIdent(splitLine[1], probs);
				System.out.println("dealt with " + (i+1) + " entries in csv");
				i++;
			}
 
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sortIdents(returnVals, 0, returnVals.length - 1);
		
		identifications = returnVals;
		return returnVals;
	}

	public int findIdent(properIdent[] array, int key, int min, int max){
		if (max < min)
			return -1;
		else {
			int mid = min + (max-min)/2;
			if (Integer.parseInt(array[mid].getKegg().substring(1)) > key)
				return (findIdent(array, key, min, mid -1));
			else if (Integer.parseInt(array[mid].getKegg().substring(1)) < key)
				return (findIdent(array, key, mid + 1, max));
			else
				return mid;
		}
	}

}