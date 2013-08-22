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
import javax.swing.JFileChooser;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import model.Ident;
import model.properIdent;

import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import peakml.IPeak;
 import view.MetaboliteMenu;
import view.RoundButton;

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
        theView.addDisplayIdLinksListener(new DisplayIdLinksListener());
        theView.addDisplayPeakLinksListener(new DisplayPeakLinksListener());
        theView.addUpdatePathIDsListener(new UpdatePathIDsListener());
        theView.addSortIdListener(new sortIdListener());
        theView.addSortPeakListener(new sortPeakListener());
    }

    class LoadPeaksListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		final JFileChooser fc = new JFileChooser("Choose PeakML File");
    		int returnVal = fc.showOpenDialog((Component) e.getSource());
    		if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                theView.displayPeakList(theModel.loadPeaks(file));
                theModel.setPeaksLoaded(true);
    		}
    		// else ?
    	}
    }
    
    class DisplayPeakPlotsListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		theView.getPeakFrame().clearPlots();
    		int[] selected = theView.getLinkFrame().getPeakMainTable().getSelectedRows();
    		theView.getLinkFrame().setCurrentPlotPeaks(selected);
    		ArrayList<IPeak> selectedPeaks = new ArrayList<IPeak>();
    		for (int i : selected)
    			selectedPeaks.add(theModel.getPeakStore().getPeakset().get(i));
    		double[] barPlotX = new double[1];
    		double[] barPlotY = new double[1];
    		if (selectedPeaks.size() >= 1)
	    		for (IPeak peak : selectedPeaks){
	    			theView.getPeakFrame().recursive(peak, 'c');
	    			barPlotX[0] = peak.getMass();
	    			barPlotY[0] = peak.getIntensity();
	    			theView.getPeakFrame().getSpecPlot().addBarPlot("test", barPlotX, barPlotY);
	    			try{
	        			String anno = (peak.getAnnotation("relation.id")).getValue();
	        			if (!anno.equals("-1")){
	        				for (IPeak p: theModel.getPeakStore().getPeakset()){
	        					if (p.getAnnotation("relation.id").getValue().equals(anno)){
	        						theView.getPeakFrame().recursive(p, 'r');
	        					}
	        				}
	        			}
	    			} catch (NullPointerException npe){
	    				System.out.println(npe);
	    			}
	    		}
    		theView.getLinkFrame().updatePeakTable(selectedPeaks, 2);
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
    		theView.getLinkFrame().updateIdTable(theModel.getIdStore().getNewIds(), 0);
    	}
    }
    
    class LoadPathListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		System.out.println("into actionPerformed");
			try{
				URL pathImgUrl = new URL("http://rest.kegg.jp/get/" + pathID + "/image");
				BufferedImage pathImg = ImageIO.read(pathImgUrl);
				theView.getPathFrame().getPathPanel().setImage(pathImg, theView.getPathFrame().getPathPanel().getWidth(),
						theView.getPathFrame().getPathPanel().getHeight());
				theView.getPathFrame().setImageSize(new Dimension(pathImg.getWidth(), pathImg.getHeight()));
				if (theModel.isIdsLoaded()){
					System.out.println("going into parsing");
					
					theModel.getIdStore().sortInit("kegg id", 1);
					parseKGML(pathID, pathImg.getWidth(), pathImg.getHeight());
					theModel.getIdStore().sortInit(theModel.getIdStore().getSortField(), theModel.getIdStore().getSortOrder());
					System.out.println("out of parsing");
				}
				addPathPopupMenus();
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
    
    class DisplayPeakLinksListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		int count;
    		theView.getLinkFrame().setLinkIdRows(new ArrayList<Integer>());
    		theModel.getIdStore().setLinkIdRows(new ArrayList<Integer>());
    		int[] sel = theView.getLinkFrame().getPeakMainTable().getSelectedRows();
    		ArrayList<IPeak> linkPeaks = new ArrayList<IPeak>();
    		for (int i : sel)
    			linkPeaks.add(theModel.getPeakStore().getPeakset().get(i));
    		ArrayList<Ident> linkIds = new ArrayList<Ident>();
    		for (int i = 0; i < theView.getLinkFrame().getLinkIdsSplit(); i++)
    			linkIds.add(theModel.getLinkIds().get(i));
    		theView.getLinkFrame().setCurrentLinkPeaks(sel);
    		for (int i = 0; i < sel.length; i++){
    			count = 0;
    			model.Link[] theLinks;
    			if ((theLinks = theModel.getPeakStore().getLinkingData().get(sel[i])) != null){
	    			for (model.Link link : theLinks){
	    				count = 0;
	    				for (Ident id : theModel.getIdStore().getNewIds()){
	    					if(link.getKeggID().equals(id.getKegg())){
	    						theView.getLinkFrame().getLinkIdRows().add(count);
	    						linkIds.add(id);
	    						theModel.getIdStore().getLinkIdRows().add(count);
	    					}
	    					count++;
	    				}
	    			}
    			}
    		}
    		if (theModel.getLinkPeaks().size() > 0) {
	    		for (int i = theView.getLinkFrame().getLinkPeaksSplit(); i < theModel.getLinkPeaks().size(); i++){
	    			linkPeaks.add(theModel.getLinkPeaks().get(i));
	    		}
    		}
    		theView.getLinkFrame().setLinkPeaksSplit(sel.length);
    		theModel.setLinkPeaks(linkPeaks);
    		theView.getLinkFrame().updatePeakTable(linkPeaks, 1);
    		
    		
    		theModel.setLinkIds(linkIds);
    		theView.getLinkFrame().updateIdTable(linkIds, 1);
    	}
    }
    	
	 class DisplayIdLinksListener implements ActionListener {
	    	public void actionPerformed(ActionEvent e) {
    		int count;
    		theView.getLinkFrame().setLinkPeakRows(new ArrayList<Integer>());
    		int[] sel = theView.getLinkFrame().getIdMainTable().getSelectedRows();
    		ArrayList<Ident> linkIds = new ArrayList<Ident>();
    		for (int i : sel)
    			linkIds.add(theModel.getIdStore().getNewIds().get(i));
    		ArrayList<IPeak> linkPeaks = new ArrayList<IPeak>();
    		for (int i = 0; i < theView.getLinkFrame().getLinkPeaksSplit(); i++)
    			linkPeaks.add(theModel.getLinkPeaks().get(i));
    		theView.getLinkFrame().setCurrentLinkIdentifications(sel);
    		theModel.getIdStore().setCurrentLinkIdentifications(sel);
    		for (int i = 0; i < sel.length; i++){
    			Ident currentId = theModel.getIdStore().getNewIds().get(sel[i]);
    			count = 0;
    			while (count < theModel.getPeakStore().getLinkingData().size()){
    				if (theModel.getPeakStore().getLinkingData().get(count) != null)
	    				for (model.Link link : theModel.getPeakStore().getLinkingData().get(count)){
	    					if (link.getKeggID().equals(currentId.getKegg())){
	    						theView.getLinkFrame().getLinkPeakRows().add(count);
	    						linkPeaks.add(theModel.getPeakStore().getPeakset().get(count));
	    					}
	    				}
    				count++;
    			}
    		}
    		if (theModel.getLinkIds().size() > 0){
	    		for (int i = theView.getLinkFrame().getLinkIdsSplit(); i < theModel.getLinkIds().size(); i++)
	    			linkIds.add(theModel.getLinkIds().get(i));
    		}
    		theView.getLinkFrame().setLinkIdsSplit(sel.length);
    		theModel.setLinkIds(linkIds);
    		theView.getLinkFrame().updateIdTable(linkIds, 1);
    		
    		
    		theModel.setLinkPeaks(linkPeaks);
    		theView.getLinkFrame().updatePeakTable(linkPeaks, 1);
    	}
    }
    
    class UpdatePathIDsListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		
    		int[] sel = theView.getLinkFrame().getIdMainTable().getSelectedRows();
    		theView.getLinkFrame().setCurrentPathIdentifications(sel);
    		
    		if (!theView.getLinkFrame().getHighlightedPathButtons().isEmpty()){
    			for (int i = 0; i < theView.getLinkFrame().getHighlightedPathButtons().size(); i++) {
    				theView.getPathFrame().getPathButtons().get(
    						theView.getLinkFrame().getHighlightedPathButtons().get(i)).setBackground(
    								theView.getLinkFrame().getHighlightedButtonColours().get(i));
    			}
    		}
    		
    		theView.getLinkFrame().getHighlightedPathButtons().clear();
    		theView.getLinkFrame().getHighlightedButtonColours().clear();
    		
    		for (int i : sel){
    			System.out.println("kegg: " + theModel.getIdStore().getNewIds().get(i).getKegg());
    			if (theModel.getIdStore().getNewIds().get(i).getButtonIndex() != -1) {
    				theView.getLinkFrame().getHighlightedPathButtons().add(theModel.getIdStore().getNewIds().get(i).getButtonIndex());
	    			theView.getLinkFrame().getHighlightedButtonColours().add(
	    					theView.getPathFrame().getPathButtons().get(
	    							theModel.getIdStore().getNewIds().get(i).getButtonIndex()).getBackground());
	    			theView.getPathFrame().getPathButtons().get(
	    					theModel.getIdStore().getNewIds().get(i).getButtonIndex()).setBackground(Color.CYAN);
    			}
    		}
    	}
    }
    
    class sortIdListener implements ActionListener{
    	public void actionPerformed(ActionEvent e) {
    		theModel.getIdStore().sortInit(
    				theView.getLinkFrame().getIdSortString(), 0);
    		theView.getLinkFrame().updateIdTable(
    				theModel.getIdStore().getNewIds(), 0);
    	}
    }
    
    class sortPeakListener implements ActionListener{
    	public void actionPerformed(ActionEvent e) {
    		theModel.getPeakStore().sortInit(
    				theView.getLinkFrame().getPeakSortString(), 0);
    		ArrayList<IPeak> alPeaks = new ArrayList<IPeak>();
    		for (IPeak peak : theModel.getPeakStore().getPeakset())
    			alPeaks.add(peak);
    		theView.getLinkFrame().updatePeakTable(alPeaks, 0);
    	}
    }
    
    public void addPathPopupMenus(){
    	MetaboliteMenu mm;
    	for (Ident pI: theModel.getIdStore().getNewIds()){
    		if (pI.getButtonIndex() != -1){
				final RoundButton rb = (RoundButton) theView.getPathFrame().getPathButtons().get(pI.getButtonIndex());
				mm = new MetaboliteMenu(pI.getKegg());
				ActionListener al = new java.awt.event.ActionListener() {
		            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                	theView.getPathFrame().updateRelated(rb.getName());
	            }};
				mm.setRelatedAction(al);
				theView.getPathFrame().getPathButtons().get(pI.getButtonIndex()).setComponentPopupMenu(mm);
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