package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import org.xml.sax.Attributes;

import model.properIdent;

@SuppressWarnings("serial")
public class PathwayFrame extends JFrame{

	static PathwayPanel pathPanel;
	private static ArrayList<RoundButton> pathButtons;
	static TransparentLabel relatedLabel;
	private static ArrayList<ArrayList<Integer>> relatedGroups;
	private static int prevGroup;
	private static int prevButton;
	private static Color prevColours[];
	Dimension imageSize;
	
	public PathwayFrame() {
        initComponents();
    }
	
	private void initComponents() {

//    	setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
		pathButtons = new ArrayList<RoundButton>();
	    
	    relatedLabel = new TransparentLabel("");
	    relatedLabel.setBackground(Color.BLUE);
	    prevGroup = -1;
	    prevButton = -1;
		
		pathPanel = new PathwayPanel();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		pathPanel.setPreferredSize(new Dimension((int)Math.round(width*0.75), (int)Math.round(height*0.75)));
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
		}
		
		// set up visual details of a button
		public void buttonGraphics(Attributes att, JButton button, double xScale, double yScale, double probability){
			
			button.setText("");
	    	button.setBorderPainted(true);
	    	button.setName(att.getValue("name"));
	    	double w = new Integer(att.getValue("width"));
	    	double h = new Integer(att.getValue("height"));
    		if (probability == 0)
    			button.setVisible(false);
    		else 
    			button.setBackground(new Color((int)(255*(1-probability)), (int)(255*probability), 0));
    		w = w*(1+probability);
			h = h*(1+probability);
	   		if (probability == -1)
	   			button.setBackground(Color.WHITE);

	    	BigDecimal x = new BigDecimal((new Integer(att.getValue("x")) - w/2)*xScale).setScale(0, RoundingMode.HALF_UP);
	    	BigDecimal y = new BigDecimal((new Integer(att.getValue("y")) - h/2)*yScale).setScale(0, RoundingMode.HALF_UP);
	    	BigDecimal adjustedWidth = new BigDecimal((w)*xScale).setScale(0, RoundingMode.HALF_EVEN);
	    	BigDecimal adjustedHeight = new BigDecimal((h)*yScale).setScale(0, RoundingMode.HALF_EVEN);
	    	
	    	button.setBounds(x.intValueExact(), y.intValueExact(), adjustedWidth.intValueExact(), adjustedHeight.intValueExact());
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

		public ArrayList<ArrayList<Integer>> getRelatedGroups() {
			return relatedGroups;
		}

		public static void setRelatedGroups(ArrayList<ArrayList<Integer>> relatedGroups) {
			PathwayFrame.relatedGroups = relatedGroups;
		}
		
		// find group a given button (index) is in
		public static int findGroup(int regIndex){
			for (ArrayList<Integer> group : relatedGroups){
				for (Integer member : group){
					if (member == regIndex){
						return relatedGroups.indexOf(group);
					}
				}
			}
			return -1;
		}
		
		// find index of given button name in pathButtons
		public static int findInButtons(String target){
			for (int i = 0; i < pathButtons.size(); i++){
				if (target.equals(pathButtons.get(i).getName())){
					return i;
				}
			}
			return -1;
		}
		
		public static void updateRelated(String buttonID){
			
			relatedLabel.setVisible(false);
			int x1, y1, x2, y2;
			JButton currentButton;
			
//			System.out.println(prevGroup);
			if (prevGroup != -1){
				for (int i = 0; i < relatedGroups.get(prevGroup).size(); i++){
					System.out.println(pathButtons.get(relatedGroups.get(prevGroup).get(i)).getName());
					System.out.println(prevColours[i]);
					pathButtons.get(relatedGroups.get(prevGroup).get(i)).setBackground(prevColours[i]);
				}
			} else if (prevButton != -1){
				pathButtons.get(prevButton).setBackground(prevColours[0]);
			}
			
			System.out.println("///////////////////////////");
			int index = findInButtons(buttonID);
			int group = findGroup(index);
			if (group == -1){
				prevButton = index;
				prevColours = new Color[1];
				currentButton = pathButtons.get(index);
				prevColours[0] = currentButton.getBackground();
				relatedLabel.setBounds(currentButton.getX()-3, currentButton.getY()-3,
						currentButton.getWidth()+6, currentButton.getHeight()+6);
				relatedLabel.setVisible(true);
				pathButtons.get(index).setBackground(Color.ORANGE);
				prevGroup = -1;
			} else {
				prevGroup = group;
				prevColours = new Color[relatedGroups.get(group).size()];
				currentButton = pathButtons.get(relatedGroups.get(group).get(0));
//				System.out.println(group);
				prevColours[0] = currentButton.getBackground();
				pathButtons.get(relatedGroups.get(group).get(0)).setBackground(Color.ORANGE);
//				System.out.println(currentButton.getName());
				x1 = currentButton.getX();
				y1 = currentButton.getY();
				x2 = x1 + currentButton.getWidth();
				y2 = y1 + currentButton.getHeight();
				
				for (int i = 1; i < relatedGroups.get(group).size(); i++){
					currentButton = pathButtons.get(relatedGroups.get(group).get(i));
					System.out.println(currentButton.getName());
					System.out.println(currentButton.getBackground());
					prevColours[i] = currentButton.getBackground();
					pathButtons.get(relatedGroups.get(group).get(i)).setBackground(Color.ORANGE);
					if (x1 > currentButton.getX())
						x1 = currentButton.getX();
					if (y1 > currentButton.getY())
						y1 = currentButton.getY();
					if (currentButton.getX() + currentButton.getWidth() > x2)
						x2 = currentButton.getX() + currentButton.getWidth();
					if (currentButton.getY() + currentButton.getHeight() > y2)
						y2 = currentButton.getY() + currentButton.getHeight();
				}
				
				relatedLabel.setBounds(x1 - 3, y1 - 3, x2 - x1 + 6, y2 - y1 + 6);
				relatedLabel.setVisible(true);
				prevButton = -1;
			}
			
			System.out.println("///////////////////////////");
		}
}