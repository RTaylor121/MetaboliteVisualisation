package view;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.math.plot.Plot2DPanel;

import peakml.BackgroundIon;
import peakml.IPeak;
import peakml.IPeakSet;
import peakml.MassChromatogram;

@SuppressWarnings("serial")
public class PeakFrame extends JFrame{
	
	JTabbedPane peakTabbedPane;
	private static Plot2DPanel chromPlot;
	private static Plot2DPanel specPlot;
	private static Plot2DPanel relatedPlot;
	
	public PeakFrame() {
        initComponents();
    }
	
	private void initComponents() {

		peakTabbedPane = new JTabbedPane();
    	chromPlot = new Plot2DPanel();
    	specPlot = new Plot2DPanel();
    	relatedPlot = new Plot2DPanel();

        chromPlot.setAxisLabels("RT", "Intensity");
		chromPlot.addLegend("EAST");
		chromPlot.removePlotToolBar();
		peakTabbedPane.addTab("Chromatogram",chromPlot);
		
		specPlot.setAxisLabels("m/z", "Intensity");
		specPlot.addLegend("EAST");
		specPlot.removePlotToolBar();
		peakTabbedPane.addTab("Mass Spectrum", specPlot);
		
		relatedPlot.setAxisLabels("RT", "Intensity");
		relatedPlot.addLegend("EAST");
		relatedPlot.removePlotToolBar();
		peakTabbedPane.addTab("Related Peaks", relatedPlot);
		
		peakTabbedPane.setPreferredSize(new Dimension(900, 650));
		
		add(peakTabbedPane);
    }
	
	public static void drawPlot(Vector<IPeak> selectedPeaks, IPeak original, char type){
		
		int count = 0;
		double[] xPoints = new double[selectedPeaks.size()];
		double[] yPoints = new double[selectedPeaks.size()];
		for (IPeak currentPeak : selectedPeaks){
			if (type == 'c' || type == 'r')
				xPoints[count] = currentPeak.getRetentionTime();
//			else if (type == 's')
//				xPoints[count] = currentPeak.getMass();
			yPoints[count] = currentPeak.getIntensity();
			count++;
		}
		Double mass = original.getMass();
		if (type == 'c')
			chromPlot.addLinePlot(mass.toString(), xPoints, yPoints);
		else if (type == 's')
			specPlot.addBarPlot(mass.toString(), xPoints, yPoints);
		else if (type == 'r')
			relatedPlot.addLinePlot(mass.toString(), xPoints, yPoints);
	}
	
	@SuppressWarnings("unchecked")
	public void recursive(IPeak peak, char type){
		
		Class<? extends IPeak> cls = peak.getClass();
		if (cls.equals(IPeakSet.class))
		{
			for (IPeak p : (IPeakSet<IPeak>) peak){
				recursive(p, type);
			}
		}
		else if (cls.equals(MassChromatogram.class)){
			MassChromatogram mc = (MassChromatogram) peak;
			if (type == 'c'){
				drawPlot(mc.getPeaks(), peak, type);
			} else if (type == 'r') {
				drawPlot(mc.getPeaks(), peak, type);
			}
			else if (cls.equals(BackgroundIon.class))
				;
		}
	}
	
	public void clearPlots(){
		chromPlot.removeAllPlots();
		specPlot.removeAllPlots();
		relatedPlot.removeAllPlots();
	}

	public static Plot2DPanel getChromPlot() {
		return chromPlot;
	}

	public static void setChromPlot(Plot2DPanel chromPlot) {
		PeakFrame.chromPlot = chromPlot;
	}

	public Plot2DPanel getSpecPlot() {
		return specPlot;
	}

	public static void setSpecPlot(Plot2DPanel specPlot) {
		PeakFrame.specPlot = specPlot;
	}

	public static Plot2DPanel getRelatedPlot() {
		return relatedPlot;
	}

	public static void setRelatedPlot(Plot2DPanel relatedPlot) {
		PeakFrame.relatedPlot = relatedPlot;
	}
	
}
