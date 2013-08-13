package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
//import javax.swing.JLayeredPane;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import model.properIdent;

import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import peakml.IPeak;

public class Controller {
    
	private static model.Model theModel;
    private static view.View  theView;
    
    private static String pathID = "tbr01100";
    
    public Controller(model.Model model, view.View view) {
        this.theModel = model;
        this.theView  = view;
        
        theView.addLoadPeaksListener(new LoadPeaksListener());
        theView.addDisplayPeakPlotsListener(new DisplayPeakPlotsListener());
        theView.addLoadIdentificationsListener(new LoadIdentificationsListener());
        theView.addLoadPathListener(new LoadPathListener());
        theView.addDisplayLinksListener(new DisplayLinksListener());
    }

    class LoadPeaksListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		final JFileChooser fc = new JFileChooser("Choose PeakML File");
    		int returnVal = fc.showOpenDialog((Component) e.getSource());
    		if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                theView.displayPeakList(theModel.loadPeaks(file));
    		}
    		// else ?
    	}
    }
    
    class DisplayPeakPlotsListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		int[] selected = theView.getLinkFrame().getPeakTable().getSelectedRows();
    		Vector<IPeak> selectedPeaks = theModel.getPeakStore().getSelectedPeaks(selected);
    		for (IPeak peak : selectedPeaks){
    			theView.getPeakFrame().recursive(peak, 'c');
    			// draw other tab plots
    		}
    		theView.getPeakFrame().setVisible(true);
    		theView.getPeakFrame().pack();
    	}
    }
    
    class LoadIdentificationsListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		final JFileChooser fc = new JFileChooser("Chooose Identification File");
    		int returnVal = fc.showOpenDialog((Component) e.getSource());
    		if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                theModel.loadIdentifications(file);
    		}
    		if (theModel.isPathLoaded()){
    			parseKGML(pathID, theView.getPathFrame().getImageSize().getWidth(), theView.getPathFrame().getImageSize().getHeight());
    			theView.getPathFrame().setVisible(true);
                theView.getPathFrame().pack();
    		}
    		theModel.setIdsLoaded(true);
    		theView.getLinkFrame().updateIdTable(theModel.getIdStore().getIdentifications());
    	}
    }
    
    class LoadPathListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		// get path id
    		System.out.println("into actionPerformed");
			try{
				URL pathImgUrl = new URL("http://rest.kegg.jp/get/" + pathID + "/image");
				BufferedImage pathImg = ImageIO.read(pathImgUrl);
				theView.getPathFrame().getPathPanel().setImage(pathImg, theView.getPathFrame().getPathPanel().getWidth(),
						theView.getPathFrame().getPathPanel().getHeight());
				theView.getPathFrame().setImageSize(new Dimension(pathImg.getWidth(), pathImg.getHeight()));
				if (theModel.isIdsLoaded()){
					System.out.println("going into parsing");
					parseKGML(pathID, pathImg.getWidth(), pathImg.getHeight());
					System.out.println("out of parsing");
				}
				theView.getPathFrame().setVisible(true);
			} catch (MalformedURLException urlException) {
				System.out.println("URL is malformed: http://rest.kegg.jp/get/" + pathID + "/image");
				urlException.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
//    		try {
//				theView.getPathFrame().getImg(pathID);
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
    	}
    }
    
    class DisplayLinksListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		int count;
    		boolean found;
    		theView.getLinkFrame().setLinkIdRows(new ArrayList<Integer>());
    		int[] sel = theView.getLinkFrame().getPeakTable().getSelectedRows();
//    		Vector<IPeak> selected = theModel.getPeakStore().getSelectedPeaks(
//    				theView.getLinkFrame().getPeakTable().getSelectedRows());
    		for (int i = 0; i < sel.length; i++){
    			IPeak currentPeak = theModel.getPeakStore().getPeakset().get(sel[i]);
    			count = 0;
    			int idCount = 0;
    			found = false;
    			///////////////////////////////////////////////////////////////////////////////////////			
    			while (count < theModel.getPeakStore().getLinkingData().size()){
    				for (model.Link link : theModel.getPeakStore().getLinkingData().get(count)){
    					if (link.getPeakIndex() == sel[i]){
    						for (properIdent id: theModel.getIdStore().getIdentifications()){
    							if (id.getKegg().equals(link.getKeggID())){
    								theView.getLinkFrame().getLinkIdRows().add(idCount);
    							}
    							idCount++;
    						}
//    						for (JButton button : theView.getPathFrame().getPathButtons()){
//    							if (button.getName().equalsIgnoreCase(link.getKeggID())){
////    								currentPathEntries.add(regButtons.indexOf(button));
////    								currentPathEntryColours.add(button.getBackground());
//    								button.setBackground(Color.YELLOW);
//    								System.out.println("Link found for: " + link.getKeggID());
//    							}
//    						}
    						found = true;
    					}
    				}
    				count++;
    			}
    		}
    	}
    }
    
    public static void parseKGML(String pID, double imgW, double imgH){
		
		try {
			 
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			
			DefaultHandler handler = new kgmlHandler(theView.getPathFrame().getPathPanel().getWidth(), theView.getPathFrame().getPathPanel().getHeight(),
					imgW, imgH, theModel, theView);
			
			saxParser.parse(new InputSource(new URL("http://rest.kegg.jp/get/" + pID + "/kgml").openStream()), handler);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}