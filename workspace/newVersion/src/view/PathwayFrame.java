package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import org.xml.sax.Attributes;

import model.properIdent;

@SuppressWarnings("serial")
public class PathwayFrame extends JFrame{

	static PathwayPanel pathPanel;
	TransparentLabel relatedLabel;
	static ArrayList<RoundButton> pathButtons;
	private static ArrayList<properIdent> buttonDetails;
	Dimension imageSize;
	
	public PathwayFrame() {
        initComponents();
    }
	
	private void initComponents() {

//    	setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
//        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		pathButtons = new ArrayList<RoundButton>();
		buttonDetails = new ArrayList<properIdent>();
	    
	    relatedLabel = new TransparentLabel("");
	    relatedLabel.setBackground(Color.BLUE);
		
		pathPanel = new PathwayPanel();
		pathPanel.setPreferredSize(new Dimension(900, 650));
	    pathPanel.setLayout(null);
	    pathPanel.setOpaque(false);
	    
		pathPanel.add(relatedLabel, JLayeredPane.DEFAULT_LAYER);
	    this.add(pathPanel);
		
        pack();
    }
	
	// initialize a button with the given attributes
		public void initButton(Attributes att, final JButton button){
			button.setBorderPainted(false);
			button.setMargin(new Insets(0,0,0,0));
			button.setFont(new Font("Dialog", 0, 10));
//			button.addActionListener(new ActionListener(){
//		        @Override
//		        public void actionPerformed(ActionEvent evt) {
//		        	try {
//		        		boolean found = false;
//		        		int count = 0;
//		        		if (!pathLink.isEmpty()){
//							regButtons.get(pathSelected).setBackground(pathLinkColour);
//						}
//		        		pathLink.clear();
//		        		while(count < linkingData.size()){
//		        			for (Link link : linkingData.get(count)){
//		        				if (link.getKeggID().equals(button.getName())){
//			    					for (JButton button : regButtons){
//			    						if (button.getName().equalsIgnoreCase(link.getKeggID())){
//			    							pathLinkColour = button.getBackground();
//			    							pathSelected = findInButtons(button.getName());
//			    							button.setBackground(Color.CYAN);
//			    							pathLink.add(link.getPeakIndex());
//			    						}
//			    					}
//			    					found = true;
//			    				}
//		        			}
//		        			count++;
//		        		}
//		    			if (!found){
//		    				System.out.println("No linking data found for button: " + button.getName());
//		    			}
//					} catch (Exception e) {	// create actual handling
//						System.out.println("Error when updating display of related nodes");
//						e.printStackTrace();
//					};
//		        }
//		    });
		}
		
		// set up visual details of a button
		public void buttonGraphics(Attributes att, JButton button, double xScale, double yScale, properIdent pI){
			
			button.setText("");
	    	button.setBorderPainted(true);
	    	button.setName(att.getValue("name"));
	    	double w = new Integer(att.getValue("width"));
	    	double h = new Integer(att.getValue("height"));
	    	double probability = pI.getProbabilities()[0];
    		if (probability == 0)
    			button.setVisible(false);
    		else 
    			button.setBackground(new Color((int)(255*(1-probability)), (int)(255*probability), 0));
    		w = w*(1+probability);
			h = h*(1+probability);
	    		buttonDetails.add(pI);
	   		if (probability == -1)
	   			button.setBackground(Color.WHITE);
	    		buttonDetails.add(null);

	    	BigDecimal x = new BigDecimal((new Integer(att.getValue("x")) - w/2)*xScale).setScale(0, RoundingMode.HALF_UP);
	    	BigDecimal y = new BigDecimal((new Integer(att.getValue("y")) - h/2)*yScale).setScale(0, RoundingMode.HALF_UP);
	    	BigDecimal adjustedWidth = new BigDecimal((w)*xScale).setScale(0, RoundingMode.HALF_EVEN);
	    	BigDecimal adjustedHeight = new BigDecimal((h)*yScale).setScale(0, RoundingMode.HALF_EVEN);
	    	
	    	button.setBounds(x.intValueExact(), y.intValueExact(), adjustedWidth.intValueExact(), adjustedHeight.intValueExact());
		
		}
		
		public static void addPopupMenus(){
	    	MetaboliteMenu mm;
	    	for (int i = 0; i < pathButtons.size(); i++){
	    		if (buttonDetails.get(i) != null){
	    			final RoundButton rb = (RoundButton) pathButtons.get(i);
	    			mm = new MetaboliteMenu(buttonDetails.get(i).getKegg());
	    			ActionListener al = new java.awt.event.ActionListener() {
	    	            public void actionPerformed(java.awt.event.ActionEvent evt) {
//	                	updateRelated(rb.getName());
	                }};
	    			mm.setRelatedAction(al);
	    			pathButtons.get(i).setComponentPopupMenu(mm);
	    		}
	    	}
	    }

		public PathwayPanel getPathPanel() {
			return pathPanel;
		}

		public static void setPathPanel(PathwayPanel pathPanel) {
			PathwayFrame.pathPanel = pathPanel;
		}

		public TransparentLabel getRelatedLabel() {
			return relatedLabel;
		}

		public void setRelatedLabel(TransparentLabel relatedLabel) {
			this.relatedLabel = relatedLabel;
		}

		public ArrayList<RoundButton> getPathButtons() {
			return pathButtons;
		}

		public void setPathButtons(ArrayList<RoundButton> pathButtons) {
			this.pathButtons = pathButtons;
		}

		public Dimension getImageSize() {
			return imageSize;
		}

		public void setImageSize(Dimension imageSize) {
			this.imageSize = imageSize;
		}
		
}