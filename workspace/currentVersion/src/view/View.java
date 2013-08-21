package view;

import java.awt.event.ActionListener;

import model.Model;
import model.newPeak;
//import model.properIdent;

import peakml.IPeak;
import peakml.IPeakSet;

public class View {
	
	model.Model model;
	
//	private LinkingFrame linkFrame;
	private newLinkingFrame linkFrame;
	private PeakFrame peakFrame;
	private PathwayFrame pathFrame;
	
	public View(Model theModel) {
		setLinkFrame(new newLinkingFrame());
		setPeakFrame(new PeakFrame());
		setPathFrame(new PathwayFrame());
		getLinkFrame().setVisible(true);
	}

	public void addLoadPeaksListener(ActionListener peakLoadListener){
		getLinkFrame().getJMenuBar().getMenu(0).getItem(0).addActionListener(peakLoadListener);
	}
	
	public void addLoadIdentificationsListener(ActionListener identificationLoadListener){
		getLinkFrame().getJMenuBar().getMenu(0).getItem(1).addActionListener(identificationLoadListener);
	}
	
	public void addDisplayPeakPlotsListener(ActionListener peakPlotsDisplayListener){
		getLinkFrame().updatePlotsButton.addActionListener(peakPlotsDisplayListener);
	}
	
	public void addDisplayLinksListener(ActionListener linksDisplayListener){
		getLinkFrame().displayLinksButton.addActionListener(linksDisplayListener);
	}
	
	public void addLoadPathListener(ActionListener pathLoadListener){
		getLinkFrame().getJMenuBar().getMenu(0).getItem(2).addActionListener(pathLoadListener);
	}
	
//	public void displayPeakList(IPeakSet<newPeak> peakset){
//		getLinkFrame().updatePeakTable(peakset);
//	}
	
	public void displayPeakList(IPeakSet<IPeak> peakset){
		getLinkFrame().updatePeakTable(peakset);
	}
	
	public newLinkingFrame getLinkFrame() {
		return linkFrame;
	}

	public void setLinkFrame(newLinkingFrame linkFrame) {
		this.linkFrame = linkFrame;
	}
	
	public void addUpdatePathIDsListener(ActionListener updatePathListener){
		getLinkFrame().updatePathButton.addActionListener(updatePathListener);
	}
	
	public PeakFrame getPeakFrame() {
		return peakFrame;
	}

	public void setPeakFrame(PeakFrame peakFrame) {
		this.peakFrame = peakFrame;
	}
	
	public PathwayFrame getPathFrame() {
		return pathFrame;
	}

	public void setPathFrame(PathwayFrame pathFrame) {
		this.pathFrame = pathFrame;
	}
	
}
