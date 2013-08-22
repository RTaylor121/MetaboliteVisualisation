package view;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import model.Model;
import model.newPeak;
//import model.properIdent;

import peakml.IPeak;
import peakml.IPeakSet;

public class View {
	
	model.Model model;
	
//	private LinkingFrame linkFrame;
//	private newLinkingFrame linkFrame;
	private wideTables linkFrame;
	private PeakFrame peakFrame;
	private PathwayFrame pathFrame;
	
	public View(Model theModel) {
		setLinkFrame(new wideTables());
//		setLinkFrame(new newLinkingFrame());
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
		getLinkFrame().getUpdatePlotsButton().addActionListener(peakPlotsDisplayListener);
	}
	
	public void addDisplayIdLinksListener(ActionListener idLinksDisplayListener){
		getLinkFrame().getDisplayIdLinksButton().addActionListener(idLinksDisplayListener);
	}
	
	public void addDisplayPeakLinksListener(ActionListener peakLinksDisplayListener){
		getLinkFrame().getPeakLinksButton().addActionListener(peakLinksDisplayListener);
	}
	
	public void addLoadPathListener(ActionListener pathLoadListener){
		getLinkFrame().getJMenuBar().getMenu(0).getItem(2).addActionListener(pathLoadListener);
	}
	
	public void addSortIdListener(ActionListener idSortListener){
		getLinkFrame().getIdSortButton().addActionListener(idSortListener);
	}
	
//	public void displayPeakList(IPeakSet<newPeak> peakset){
//		getLinkFrame().updatePeakTable(peakset);
//	}
	
//	public void displayPeakList(Vector<IPeak> peakset){
//		getLinkFrame().updatePeakTable(peakset, 0);
//	}
	
	public void displayPeakList(ArrayList<IPeak> peakset){
		getLinkFrame().updatePeakTable(peakset, 0);
	}
	
//	public newLinkingFrame getLinkFrame() {
//		return linkFrame;
//	}
//
//	public void setLinkFrame(newLinkingFrame linkFrame) {
//		this.linkFrame = linkFrame;
//	}
	
	public void addUpdatePathIDsListener(ActionListener updatePathListener){
		getLinkFrame().getUpdatePathButton().addActionListener(updatePathListener);
	}
	
	public wideTables getLinkFrame() {
		return linkFrame;
	}

	public void setLinkFrame(wideTables linkFrame) {
		this.linkFrame = linkFrame;
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
