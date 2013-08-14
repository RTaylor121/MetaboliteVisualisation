package view;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import model.properIdent;
import peakml.IPeak;
import peakml.IPeakSet;

@SuppressWarnings("serial")
public class newLinkingFrame extends javax.swing.JFrame {
	
	javax.swing.JButton displayLinksButton;
	javax.swing.JButton updatePathButton;
	javax.swing.JButton updatePlotsButton;
	
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuBar menuBar;
    
    private javax.swing.JScrollPane peakScroll;
    private javax.swing.JTable peakTable;
    private PeakTableModel peakTM;
    private ListSelectionModel peakTableSelectionModel;
    private String[] peakColumnNames;
    private int[] currentPlotPeaks;
    private int[] currentLinkPeaks;

    private javax.swing.JScrollPane idScroll;
    private javax.swing.JTable idTable;
    private IdentificationTableModel idTM;
    private ListSelectionModel idTableSelectionModel;
    private String[] idColumnNames;
	private int[] currentPathIdentifications;
	private int[] currentLinkIdentifications;
	
	private ArrayList<Integer> linkIdRows;
	
    public newLinkingFrame() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        peakScroll = new javax.swing.JScrollPane();
        peakTable = new javax.swing.JTable();
        idScroll = new javax.swing.JScrollPane();
        idTable = new javax.swing.JTable();
        updatePlotsButton = new javax.swing.JButton();
        displayLinksButton = new javax.swing.JButton();
        updatePathButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1200, 700));

        currentPlotPeaks = new int[0];
        currentLinkPeaks = new int[0];
        currentPathIdentifications = new int[0];
        currentLinkIdentifications = new int[0];
    	
    	linkIdRows = new ArrayList<Integer>();
//        peakScroll.setPreferredSize(new java.awt.Dimension(400, 616));

        peakColumnNames = new String [] {"No.",
    			"Mass",
                "Intensity",
                "Retention Time",
                "Link"};
        peakTM = new PeakTableModel(new Object [][] {}, peakColumnNames);
        	setPeakTable(new JTable(peakTM){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
				Component c = super.prepareRenderer(renderer, row, column);

//				if (pathLink.contains(row)){
//					c.setBackground(Color.CYAN);
//				} else if (isRowSelected(row)){
				if (isRowSelected(row)){
					c.setBackground(Color.GRAY);
					c.setForeground(Color.WHITE);
				} else {
					for (int i : currentPlotPeaks){
						if (row == i){
							c.setBackground(Color.ORANGE);
							c.setForeground(Color.BLACK);
							return c;
						}
					}
					for (int i : currentLinkPeaks){
						if (row == i){
							c.setBackground(Color.ORANGE);
							c.setForeground(Color.BLACK);
							return c;
						}
					}
					c.setBackground(Color.WHITE);
					c.setForeground(Color.BLACK);
				}
				return c;
    		}
        });
        
        peakTableSelectionModel = getPeakTable().getSelectionModel();
        getPeakTable().setSelectionModel(peakTableSelectionModel);
        peakScroll.setViewportView(peakTable);

        idColumnNames = new String [] {"No.",
        		"Kegg ID",
                "Probability"};
        idTM = new IdentificationTableModel(new Object [][] {}, idColumnNames);
        	setIdTable(new JTable(idTM){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
				Component c = super.prepareRenderer(renderer, row, column);

				if (isRowSelected(row)){
					c.setBackground(Color.GRAY);
					c.setForeground(Color.WHITE);
				} else if (linkIdRows.contains(row)){
					c.setBackground(Color.ORANGE);
					c.setForeground(Color.BLACK);
				} else {
					for (int i : currentPathIdentifications){
						if (row == i){
							c.setBackground(Color.CYAN);
							c.setForeground(Color.BLACK);
							return c;
						}
					}
					for (int i : currentLinkIdentifications){
						if (row == i){
							c.setBackground(Color.CYAN);
							c.setForeground(Color.BLACK);
							return c;
						}
					}
					c.setBackground(Color.WHITE);
					c.setForeground(Color.BLACK);
				}
				return c;
    		}
        });
        
        idTableSelectionModel = getIdTable().getSelectionModel();
        getIdTable().setSelectionModel(idTableSelectionModel);
        
        idScroll.setViewportView(idTable);

        updatePlotsButton.setText("Update Plots");

        displayLinksButton.setText("DisplayLinks");

        updatePathButton.setText("Show on Pathway");

        fileMenu.setText("File");
        
        JMenuItem openPeakMLItem = new JMenuItem("Open PeakML File");
		fileMenu.add(openPeakMLItem);
		
		JMenuItem openIdentificationItem = new JMenuItem("Open Identification File");
		fileMenu.add(openIdentificationItem);
		
		JMenuItem loadPathItem = new JMenuItem("Load Path");
		fileMenu.add(loadPathItem);
		
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	System.exit(0);
            }
		});
		fileMenu.add(exitMenuItem);

		menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(peakScroll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 350, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(updatePlotsButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 152, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(updatePathButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(displayLinksButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 152, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(85, 85, 85)))
                .add(idScroll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 350, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(updatePlotsButton)
                            .add(updatePathButton))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(displayLinksButton))
                    .add(peakScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(idScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }                        

//    public static void main(String args[]) {
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(newLinkingFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(newLinkingFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(newLinkingFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(newLinkingFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new newLinkingFrame().setVisible(true);
//            }
//        });
//    }   
    
    public void updatePeakTable(IPeakSet<IPeak> peakset){
		
		IPeak current;
		Object[][] tableData = new Object[peakset.size()][5];
		for(int i = 0; i < peakset.size(); i++){
			current = peakset.get(i);
			tableData[i][0] = i;
			tableData[i][1] = current.getMass();
			tableData[i][2] = current.getIntensity();
			tableData[i][3] = current.getRetentionTime();
			try{
				tableData[i][4] = current.getAnnotation("probabilityIdentification").getValue();
			}
			catch(Exception e){
				tableData[i][4] = "n/a";
			}
		}
		getPeakTable().setModel(new DefaultTableModel(tableData, peakColumnNames));
	}
	
	public void updateIdTable(properIdent[] identifications){
		
		properIdent current;
		Object[][] tableData = new Object[identifications.length][3];
		for(int i = 0; i < identifications.length; i++){
			current = identifications[i];
			tableData[i][0] = i + 1;
			tableData[i][1] = current.getKegg();
			tableData[i][2] = current.getProbabilities()[0];
//			try{
//				tableData[i][3] = current.getAnnotation("probabilityIdentification").getValue();
//			}
//			catch(Exception e){
//				tableData[i][3] = "n/a";
//			}
		}
		getIdTable().setModel(new DefaultTableModel(tableData, idColumnNames));
	}

	public javax.swing.JTable getPeakTable() {
		return peakTable;
	}

	public void setPeakTable(javax.swing.JTable peakTable) {
		this.peakTable = peakTable;
	}

	public javax.swing.JTable getIdTable() {
		return idTable;
	}

	public void setIdTable(javax.swing.JTable idTable) {
		this.idTable = idTable;
	}

	public ArrayList<Integer> getLinkIdRows() {
		return linkIdRows;
	}

	public void setLinkIdRows(ArrayList<Integer> linkIdRows) {
		this.linkIdRows = linkIdRows;
	}

	public int[] getCurrentPeakRows() {
		return currentPlotPeaks;
	}

	public void setCurrentPeakRows(int[] currentPeakRows) {
		this.currentPlotPeaks = currentPeakRows;
	}

	public int[] getCurrentIdentificationRows() {
		return currentPathIdentifications;
	}

	public void setCurrentIdentificationRows(int[] currentIdentificationRows) {
		this.currentPathIdentifications = currentIdentificationRows;
	}

	public int[] getCurrentPlotPeaks() {
		return currentPlotPeaks;
	}

	public void setCurrentPlotPeaks(int[] currentPlotPeaks) {
		this.currentPlotPeaks = currentPlotPeaks;
	}

	public int[] getCurrentLinkPeaks() {
		return currentLinkPeaks;
	}

	public void setCurrentLinkPeaks(int[] currentLinkPeaks) {
		this.currentLinkPeaks = currentLinkPeaks;
	}

	public int[] getCurrentPathIdentifications() {
		return currentPathIdentifications;
	}

	public void setCurrentPathIdentifications(int[] currentPathIdentifications) {
		this.currentPathIdentifications = currentPathIdentifications;
	}

	public int[] getCurrentLinkIdentifications() {
		return currentLinkIdentifications;
	}

	public void setCurrentLinkIdentifications(int[] currentLinkIdentifications) {
		this.currentLinkIdentifications = currentLinkIdentifications;
	}
	
	
	
}