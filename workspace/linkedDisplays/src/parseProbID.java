import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import domsax.XmlParserException;

import peakml.IPeak;
import peakml.IPeakSet;
import peakml.io.ParseResult;
import peakml.io.peakml.PeakMLParser;

public class parseProbID {

	private static IPeakSet<IPeak> peaks;
	private static ArrayList<Link> linkingData;
	
	public static void addLinkData(String anno, int peakIndex){
		System.out.println("ADDING NEW LINK DATA: " + anno);
		if (anno.contains(";"))
			anno = anno.substring(0, anno.indexOf(';'));
		Link newLink = null;
		String[] splitLine = anno.split(",", 0);
		if (splitLine.length == 3){
			newLink = new Link(splitLine[0], "default", null, Double.parseDouble(splitLine[2]), peakIndex);
		} else if (splitLine.length == 4) {
			newLink = new Link(splitLine[0], splitLine[1], splitLine[2], Double.parseDouble(splitLine[3]), peakIndex);
		}
		linkingData.add(newLink);
		System.out.println(linkingData.size());
	}
	
	public static void getProbabilityAttributes(){
		
		String annotation;
		int peakCounter = 0;
		for (IPeak peak : peaks){
			if (peak.getAnnotation("probabilityIdentification") != null){
				annotation = peak.getAnnotation("probabilityIdentification").getValue();
				addLinkData(annotation, peakCounter);
			} else
				System.out.println("no anno");
			peakCounter++;
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, XmlParserException{
		
		File peakFile = new File("/users/level3/1002858t/SummerProject2013/newStuff/UL10_NEG.peakml.out");
		
		ParseResult result = PeakMLParser.parse(new FileInputStream(peakFile), true);
//    	Header header = result.header;
		peaks = (IPeakSet<IPeak>) result.measurement;
		linkingData = new ArrayList<Link>();
		getProbabilityAttributes();
		
		for (Link link : linkingData){
			System.out.println(link.toString());
		}
//		for (IPeak currentPeak : peaks){
//			if (currentPeak.getClass().equals(IPeakSet.class)){
//				try {
//					System.out.println(currentPeak.getAnnotation("probabilityIdentification").getValue());
//				} catch(Exception e){
//					System.out.println("No probability ID annotation found for peak.");
//				}
//			}
//		}
		
//		System.out.println(peaks.size());
	}
}
