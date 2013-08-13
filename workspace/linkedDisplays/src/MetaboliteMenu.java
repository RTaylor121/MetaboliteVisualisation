import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class MetaboliteMenu extends JPopupMenu {

    private static JMenuItem showRelated;
    private static JMenuItem showKegg;
    
    public static JMenuItem getShowRelated() {
		return showRelated;
	}

	public static void setShowRelated(JMenuItem showRelated) {
		MetaboliteMenu.showRelated = showRelated;
	}

	public static JMenuItem getShowKegg() {
		return showKegg;
	}

	public static void setShowKegg(JMenuItem showKegg) {
		MetaboliteMenu.showKegg = showKegg;
	}

    public MetaboliteMenu(final String kegg){
    	showRelated = new JMenuItem("Show Related");
//    	showRelated.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//            	System.out.println("show related");
//            }});
    	add(showRelated);
    	showKegg = new JMenuItem("View on KEGG");
    	showKegg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	try {
					open(new URI("http://www.kegg.jp/dbget-bin/www_bget?" + kegg));
				} catch (URISyntaxException e) {
					System.out.println("Invalid URI syntax for: " + kegg);
					e.printStackTrace();
				};
            }});
    	add(showKegg);
    }
    
    private static void open(URI uri) {
	    if (Desktop.isDesktopSupported()) {
	      try {
	        Desktop.getDesktop().browse(uri);
	      } catch (IOException e) { System.out.println(e.toString()); }
	    } else { System.out.println("Desktop not supported"); }
	}
    
    public void setRelatedAction(ActionListener al){
    	showRelated.addActionListener(al);
    }
}
