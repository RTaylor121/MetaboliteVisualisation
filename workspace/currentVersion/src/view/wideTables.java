package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import model.Ident;
import peakml.IPeak;
import peakml.IPeakSet;

@SuppressWarnings("serial")
public class wideTables extends javax.swing.JFrame {
	
	private PeakTableModel peakMainTM, peakLinksTM;
    private ListSelectionModel peakMainTableSelectionModel;
    private String[] peakColumnNames;
    private int[] currentPlotPeaks;
    private int[] currentLinkPeaks;
	
	private IdentificationTableModel idTM,idLinksTM;
    private ListSelectionModel idTableSelectionModel;
    private String[] idColumnNames;
	private int[] currentPathIdentifications;
	private int[] currentLinkIdentifications;
	
	private ArrayList<Integer> linkIdRows;
	private ArrayList<Integer> linkPeakRows;
	private ArrayList<Integer> highlightedPathButtons;
	private ArrayList<Color> highlightedButtonColours;
	
	private String idSortString, peakSortString;
	private int idSortOrder, peakSortOrder;
	
	int linkPeaksSplit, linkIdsSplit;

    public wideTables() {
        initComponents();
    }

    @SuppressWarnings("serial")
	private void initComponents() {

        idPanel = new javax.swing.JPanel();
        idTabbedPane = new javax.swing.JTabbedPane();
        idMainScroll = new javax.swing.JScrollPane();
        idMainTable = new javax.swing.JTable();
        idLinksScroll = new javax.swing.JScrollPane();
        idLinksTable = new javax.swing.JTable();
        idPathScroll = new javax.swing.JScrollPane();
        idPathTable = new javax.swing.JTable();
        idSortButton = new javax.swing.JButton();
        idPlotsButton = new javax.swing.JButton();
        idComboBox = new javax.swing.JComboBox();
        peakComboBox = new javax.swing.JComboBox();
        updatePathButton = new javax.swing.JButton();
        displayIdLinksButton = new javax.swing.JButton();
        peakPanel = new javax.swing.JPanel();
        peakTabbedPane = new javax.swing.JTabbedPane();
        peakMainScroll = new javax.swing.JScrollPane();
        peakMainTable = new javax.swing.JTable();
        peakLinksScroll = new javax.swing.JScrollPane();
        peakLinksTable = new javax.swing.JTable();
        peakPlotScroll = new javax.swing.JScrollPane();
        peakPlotTable = new javax.swing.JTable();
        peakSortButton = new javax.swing.JButton();
        peakClearButton = new javax.swing.JButton();
//        peakSortSpinner = new javax.swing.JSpinner();
        updatePlotsButton = new javax.swing.JButton();
        peakLinksButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        currentPlotPeaks = new int[0];
        currentLinkPeaks = new int[0];
        currentPathIdentifications = new int[0];
        currentLinkIdentifications = new int[0];
    	
    	linkIdRows = new ArrayList<Integer>();
    	linkPeakRows = new ArrayList<Integer>();
    	highlightedPathButtons = new ArrayList<Integer>();
    	highlightedButtonColours = new ArrayList<Color>();
    	
    	linkPeaksSplit = 0;
    	linkIdsSplit = 0;
    	idSortString = "id";
    	peakSortString = "mass";
    	idSortOrder = 0;
    	peakSortOrder = 0;
    	
        idPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Identifications"));

        idTabbedPane.setPreferredSize(new java.awt.Dimension(700, 450));

        idColumnNames = new String [] {"ID",
        		"Kegg ID",
        		"Name",
                "P1",
                "P2",
                "P3",
                "P4",
                "P5",
                "PCombined",};
        idTM = new IdentificationTableModel(new Object [][] {}, idColumnNames);
        	setIdMainTable(new JTable(idTM){
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
							c.setBackground(Color.GREEN);
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
        
        idTableSelectionModel = getIdMainTable().getSelectionModel();
        getIdMainTable().setSelectionModel(idTableSelectionModel);
        idMainScroll.setViewportView(idMainTable);

        idTabbedPane.addTab("All", idMainScroll);

        idLinksTM = new IdentificationTableModel(new Object [][] {}, idColumnNames);
    	setIdLinksTable(new JTable(idLinksTM){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
				Component c = super.prepareRenderer(renderer, row, column);

				if (isRowSelected(row)){
					c.setBackground(Color.GRAY);
					c.setForeground(Color.WHITE);
				} else if (linkIdsSplit > row){
					c.setBackground(Color.CYAN);
					c.setForeground(Color.BLACK);
				} else {
					c.setBackground(Color.ORANGE);
					c.setForeground(Color.BLACK);
				}
				return c;
    		}
        });
        idLinksScroll.setViewportView(idLinksTable);

        idTabbedPane.addTab("Links", idLinksScroll);

        idPathTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {}, idColumnNames
        ));
        idPathScroll.setViewportView(idPathTable);

        idTabbedPane.addTab("On Path", idPathScroll);
        
        JComboBox idComboBox = new JComboBox(idColumnNames);
        idComboBox.addItemListener(new ItemListener(){

            public void itemStateChanged(ItemEvent e) {
                String s = (String)e.getItem();
                idSortString = s;
            }
        });
        
        idSortButton.setText("Sort By");
        idSortButton.setPreferredSize(new java.awt.Dimension(130, 29));

        idPlotsButton.setText("Plot Linked Peaks");
        idPlotsButton.setPreferredSize(new java.awt.Dimension(130, 29));

        updatePathButton.setText("Show On Path");
        updatePathButton.setPreferredSize(new java.awt.Dimension(130, 29));

        displayIdLinksButton.setText("Show Links");
        displayIdLinksButton.setPreferredSize(new java.awt.Dimension(130, 29));

        org.jdesktop.layout.GroupLayout idPanelLayout = new org.jdesktop.layout.GroupLayout(idPanel);
        idPanel.setLayout(idPanelLayout);
        idPanelLayout.setHorizontalGroup(
            idPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(idPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(idTabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(idPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, updatePathButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, displayIdLinksButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(idPlotsButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(idSortButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(idComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 96, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        idPanelLayout.setVerticalGroup(
            idPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(idPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(idPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(idPanelLayout.createSequentialGroup()
                        .add(0, 147, Short.MAX_VALUE)
                        .add(displayIdLinksButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(updatePathButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(idPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(idSortButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(idComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(idPlotsButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(16, 16, 16))
                    .add(idTabbedPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        peakPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Peaks"));

        peakTabbedPane.setPreferredSize(new java.awt.Dimension(700, 450));
        
        peakColumnNames = new String [] {"Mass",
                "Intensity",
                "Retention Time",
                "Link"};
        peakMainTM = new PeakTableModel(new Object [][] {}, peakColumnNames);
        	setPeakMainTable(new JTable(peakMainTM){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
				Component c = super.prepareRenderer(renderer, row, column);

				if (isRowSelected(row)){
					c.setBackground(Color.GRAY);
					c.setForeground(Color.WHITE);
				} else if (linkPeakRows.contains(row)){
					c.setBackground(Color.CYAN);
					c.setForeground(Color.BLACK);
				} else {
					for (int i : currentPlotPeaks){
						if (row == i){
							c.setBackground(Color.GREEN);
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
        	
    	peakMainTableSelectionModel = getPeakMainTable().getSelectionModel();
        getPeakMainTable().setSelectionModel(peakMainTableSelectionModel);

        peakMainScroll.setViewportView(peakMainTable);

        peakTabbedPane.addTab("All", peakMainScroll);

        peakLinksTM = new PeakTableModel(new Object [][] {}, peakColumnNames);
    	setPeakLinksTable(new JTable(peakLinksTM){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
				Component c = super.prepareRenderer(renderer, row, column);

				if (isRowSelected(row)){
					c.setBackground(Color.GRAY);
					c.setForeground(Color.WHITE);
				} else if (linkPeaksSplit > row){
					c.setBackground(Color.ORANGE);
					c.setForeground(Color.BLACK);
				} else {
					c.setBackground(Color.CYAN);
					c.setForeground(Color.BLACK);
				}
				return c;
    		}
        });
        peakLinksScroll.setViewportView(peakLinksTable);

        peakTabbedPane.addTab("Links", peakLinksScroll);

        peakPlotTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {}, peakColumnNames
        ));
        peakPlotScroll.setViewportView(peakPlotTable);

        peakTabbedPane.addTab("On Plots", peakPlotScroll);
        
        JComboBox peakComboBox = new JComboBox(peakColumnNames);
        peakComboBox.addItemListener(new ItemListener(){

            public void itemStateChanged(ItemEvent e) {
                String s = (String)e.getItem();
                peakSortString = s;
            }
        });

        peakSortButton.setText("Sort By");
        peakSortButton.setPreferredSize(new java.awt.Dimension(130, 29));

        peakClearButton.setText("Clear");
        peakClearButton.setPreferredSize(new java.awt.Dimension(130, 29));
        peakClearButton.setEnabled(false);

        updatePlotsButton.setText("Show On Plots");
        updatePlotsButton.setPreferredSize(new java.awt.Dimension(130, 29));

        peakLinksButton.setText("Show Links");
        peakLinksButton.setPreferredSize(new java.awt.Dimension(130, 29));

        org.jdesktop.layout.GroupLayout peakPanelLayout = new org.jdesktop.layout.GroupLayout(peakPanel);
        peakPanel.setLayout(peakPanelLayout);
        peakPanelLayout.setHorizontalGroup(
            peakPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(peakPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(peakTabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(peakPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(peakLinksButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(peakPanelLayout.createSequentialGroup()
                        .add(peakPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, updatePlotsButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, peakSortButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, peakClearButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(peakComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 96, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        peakPanelLayout.setVerticalGroup(
            peakPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(peakPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(peakTabbedPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .add(peakPanelLayout.createSequentialGroup()
                .addContainerGap(159, Short.MAX_VALUE)
                .add(peakLinksButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(updatePlotsButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(peakPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(peakComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(peakSortButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 0, 0)
                .add(peakClearButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(16, 16, 16))
        );

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
        
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(peakPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(idPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(idPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(peakPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }
    
    public javax.swing.JButton getUpdatePathButton() {
		return updatePathButton;
	}

	public void setUpdatePathButton(javax.swing.JButton updatePathButton) {
		this.updatePathButton = updatePathButton;
	}

	public javax.swing.JButton getUpdatePlotsButton() {
		return updatePlotsButton;
	}

	public void setUpdatePlotsButton(javax.swing.JButton updatePlotsButton) {
		this.updatePlotsButton = updatePlotsButton;
	}

  		
	public void updatePeakTable(ArrayList<IPeak> peakset, int table ){
  		IPeak current;
  		Object[][] tableData;
  		if (peakset.size() < 1)
  			tableData = new Object[0][0];
  		else {
  			tableData = new Object[peakset.size()][4];
	  		for(int i = 0; i < peakset.size(); i++){
	  			current = peakset.get(i);
	  			tableData[i][0] = current.getMass();
	  			tableData[i][1] = current.getIntensity();
	  			tableData[i][2] = current.getRetentionTime();
	  			try{
	  				tableData[i][3] = current.getAnnotation("probabilityIdentification").getValue();
	  			}
	  			catch(Exception e){
	  				tableData[i][3] = "n/a";
	  			}
	  		}
  		}
  		
  		if (table == 0)
  			getPeakMainTable().setModel(new DefaultTableModel(tableData, peakColumnNames));
  		else if (table == 1)
  			getPeakLinksTable().setModel(new DefaultTableModel(tableData, peakColumnNames));
  		else
  			getPeakPlotTable().setModel(new DefaultTableModel(tableData, peakColumnNames));
	}
  	
      public void updateIdTable(ArrayList<Ident> identifications, int table){
  		
  		Ident current;
  		Object[][] tableData;
  		if (identifications.isEmpty())
  			tableData = new Object[0][0];
  		else {
  			tableData = new Object[identifications.size()][9];
	  		for(int i = 0; i < identifications.size(); i++){
	  			current = identifications.get(i);
	  			tableData[i][0] = current.getId();
	  			tableData[i][1] = current.getKegg();
	  			tableData[i][2] = current.getName();
	  			tableData[i][3] = current.getProbabilities()[0];
	  			tableData[i][4] = current.getProbabilities()[1];
	  			tableData[i][5] = current.getProbabilities()[2];
	  			tableData[i][6] = current.getProbabilities()[3];
	  			tableData[i][7] = current.getProbabilities()[4];
	  			tableData[i][8] = current.getCombinedProb();
	  		}
  		}
  		if (table == 0)
  			getIdMainTable().setModel(new DefaultTableModel(tableData, idColumnNames));
  		else if (table == 1)
  			getIdLinksTable().setModel(new DefaultTableModel(tableData, idColumnNames));
  		else
  			getIdPathTable().setModel(new DefaultTableModel(tableData, idColumnNames));
  	}

    public PeakTableModel getPeakTM() {
		return peakMainTM;
	}

	public void setPeakTM(PeakTableModel peakTM) {
		this.peakMainTM = peakTM;
	}

	public ListSelectionModel getPeakTableSelectionModel() {
		return peakMainTableSelectionModel;
	}

	public void setPeakTableSelectionModel(
			ListSelectionModel peakTableSelectionModel) {
		this.peakMainTableSelectionModel = peakTableSelectionModel;
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

	public IdentificationTableModel getIdTM() {
		return idTM;
	}

	public void setIdTM(IdentificationTableModel idTM) {
		this.idTM = idTM;
	}

	public ListSelectionModel getIdTableSelectionModel() {
		return idTableSelectionModel;
	}

	public void setIdTableSelectionModel(ListSelectionModel idTableSelectionModel) {
		this.idTableSelectionModel = idTableSelectionModel;
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

	public ArrayList<Integer> getLinkIdRows() {
		return linkIdRows;
	}

	public void setLinkIdRows(ArrayList<Integer> linkIdRows) {
		this.linkIdRows = linkIdRows;
	}

	public ArrayList<Integer> getLinkPeakRows() {
		return linkPeakRows;
	}

	public void setLinkPeakRows(ArrayList<Integer> linkPeakRows) {
		this.linkPeakRows = linkPeakRows;
	}

	public ArrayList<Integer> getHighlightedPathButtons() {
		return highlightedPathButtons;
	}

	public void setHighlightedPathButtons(ArrayList<Integer> highlightedPathButtons) {
		this.highlightedPathButtons = highlightedPathButtons;
	}

	public ArrayList<Color> getHighlightedButtonColours() {
		return highlightedButtonColours;
	}

	public void setHighlightedButtonColours(
			ArrayList<Color> highlightedButtonColours) {
		this.highlightedButtonColours = highlightedButtonColours;
	}

	public javax.swing.JTable getIdLinksTable() {
		return idLinksTable;
	}

	public void setIdLinksTable(javax.swing.JTable idLinksTable) {
		this.idLinksTable = idLinksTable;
	}

	public javax.swing.JTable getIdMainTable() {
		return idMainTable;
	}

	public void setIdMainTable(javax.swing.JTable idMainTable) {
		this.idMainTable = idMainTable;
	}

	public javax.swing.JTable getIdPathTable() {
		return idPathTable;
	}

	public void setIdPathTable(javax.swing.JTable idPathTable) {
		this.idPathTable = idPathTable;
	}

	public javax.swing.JTable getPeakLinksTable() {
		return peakLinksTable;
	}

	public void setPeakLinksTable(javax.swing.JTable peakLinksTable) {
		this.peakLinksTable = peakLinksTable;
	}

	public javax.swing.JTable getPeakMainTable() {
		return peakMainTable;
	}

	public void setPeakMainTable(javax.swing.JTable peakMainTable) {
		this.peakMainTable = peakMainTable;
	}

	public javax.swing.JTable getPeakPlotTable() {
		return peakPlotTable;
	}

	public void setPeakPlotTable(javax.swing.JTable peakPlotTable) {
		this.peakPlotTable = peakPlotTable;
	}
	
	public javax.swing.JButton getDisplayIdLinksButton() {
		return displayIdLinksButton;
	}

	public void setDisplayIdLinksButton(javax.swing.JButton displayIdLinksButton) {
		this.displayIdLinksButton = displayIdLinksButton;
	}

	public javax.swing.JButton getPeakLinksButton() {
		return peakLinksButton;
	}

	public void setPeakLinksButton(javax.swing.JButton peakLinksButton) {
		this.peakLinksButton = peakLinksButton;
	}

	public javax.swing.JButton getIdClearButton() {
		return idPlotsButton;
	}

	public void setIdClearButton(javax.swing.JButton idClearButton) {
		this.idPlotsButton = idClearButton;
	}

	public javax.swing.JButton getIdSortButton() {
		return idSortButton;
	}

	public void setIdSortButton(javax.swing.JButton idSortButton) {
		this.idSortButton = idSortButton;
	}

//	public javax.swing.JButton getPeakClearButton() {
//		return peakClearButton;
//	}
//
//	public void setPeakClearButton(javax.swing.JButton peakClearButton) {
//		this.peakClearButton = peakClearButton;
//	}

	public javax.swing.JButton getPeakSortButton() {
		return peakSortButton;
	}

//	public void setPeakSortButton(javax.swing.JButton peakSortButton) {
//		this.peakSortButton = peakSortButton;
//	}
	
	public String getSortString() {
		return idSortString;
	}

	public void setSortString(String sortString) {
		this.idSortString = sortString;
	}

	public int getLinkPeaksSplit() {
		return linkPeaksSplit;
	}

	public void setLinkPeaksSplit(int linkPeaksSplit) {
		this.linkPeaksSplit = linkPeaksSplit;
	}

	public int getLinkIdsSplit() {
		return linkIdsSplit;
	}

	public void setLinkIdsSplit(int linkIdsSplit) {
		this.linkIdsSplit = linkIdsSplit;
	}
	
	public String getIdSortString() {
		return idSortString;
	}

	public void setIdSortString(String idSortString) {
		this.idSortString = idSortString;
	}

	public String getPeakSortString() {
		return peakSortString;
	}

	public void setPeakSortString(String peakSortString) {
		this.peakSortString = peakSortString;
	}

	public int getIdSortOrder() {
		return idSortOrder;
	}

	public void setIdSortOrder(int idSortOrder) {
		this.idSortOrder = idSortOrder;
	}

	public int getPeakSortOrder() {
		return peakSortOrder;
	}

	public void setPeakSortOrder(int peakSortOrder) {
		this.peakSortOrder = peakSortOrder;
	}



	private javax.swing.JButton displayIdLinksButton;
	private javax.swing.JButton idPlotsButton;
	private javax.swing.JScrollPane idLinksScroll;
	private javax.swing.JTable idLinksTable;
	private javax.swing.JScrollPane idMainScroll;
	private javax.swing.JTable idMainTable;
	private javax.swing.JPanel idPanel;
	private javax.swing.JScrollPane idPathScroll;
	private javax.swing.JTable idPathTable;
	private javax.swing.JButton idSortButton;
	private javax.swing.JComboBox idComboBox;
	private javax.swing.JComboBox peakComboBox;
	private javax.swing.JTabbedPane idTabbedPane;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton peakClearButton;
    private javax.swing.JButton peakLinksButton;
    private javax.swing.JScrollPane peakLinksScroll;
    private javax.swing.JTable peakLinksTable;
    private javax.swing.JScrollPane peakMainScroll;
    private javax.swing.JTable peakMainTable;
    private javax.swing.JPanel peakPanel;
    private javax.swing.JScrollPane peakPlotScroll;
    private javax.swing.JTable peakPlotTable;
    private javax.swing.JButton peakSortButton;
    private javax.swing.JTabbedPane peakTabbedPane;
    private javax.swing.JButton updatePathButton;
    private javax.swing.JButton updatePlotsButton;
    // End of variables declaration//GEN-END:variables
}
