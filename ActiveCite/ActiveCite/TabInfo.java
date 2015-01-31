import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import javax.swing.text.StyledDocument;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.util.Vector;

public class TabInfo extends JPanel{
	

	private String paperTitle = "";
	
	public TabInfo(String title) {
        super(new GridLayout(1, 1));
        
        JTabbedPane tabbedPane = new JTabbedPane();
        paperTitle = title;
        //ImageIcon icon = createImageIcon("images/middle.gif");

        String sql = "select * from paper where title = '" + paperTitle + "'";
        //Paper related Information
        String analysisContent = "Analysis content showing why the paper is cited and how it is cited here";
        String abstractContent = "Abstract Information about one paper";
        String BibInfo = "Title: " + title + "\n";
        String citationContent = "";
        Vector citationList    = new Vector();
        
        try{
			System.out.println("SQL: = " + sql);
				
			DBConn dbconn = new DBConn();
			
			ResultSet rs  = dbconn.executeQuery(sql);
			if(rs.next()){
				analysisContent = rs.getObject("Analysis").toString();
				abstractContent  = rs.getObject("abstract").toString();
				BibInfo = BibInfo + "Authors: " + rs.getObject("author").toString() + "\n";
				BibInfo = BibInfo + "Conference: " + rs.getObject("conference").toString() + "\n";
				BibInfo = BibInfo + "publication year: " + rs.getObject("publication_year").toString() + "\n";
			}
			sql = "select sourcePaper from citedInfo where targetPaper = '" + paperTitle + "'";
			ResultSet rset = dbconn.executeQuery(sql);
			
			//暂时先一行一行的写出来，等到合适的时候再将其列成一张表
			int numberOfCitation = 0;
			
			while(rset.next()){
				numberOfCitation ++;
				String head = "[" + numberOfCitation + "]  ";
				citationList.add(rset.getObject("sourcePaper").toString());
				
				citationContent = citationContent + head + rset.getObject("sourcePaper").toString() + "\n";
			}
			}catch (Exception ee){
				System.out.println("Find the Exception while assining value");
				ee.printStackTrace();
			}
			
        
        JComponent panel2 = makeTextPanel(analysisContent);
        tabbedPane.addTab("Analysis", panel2);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
        JComponent abstractInfo = makeTextPanel(abstractContent);
        tabbedPane.addTab("Abstract", abstractInfo);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        JComponent panel3 = makeTextPanel(citationContent);
        tabbedPane.addTab("CitationList", panel3);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
        
        JComponent panel4 = makeTextPanel(BibInfo);
        tabbedPane.addTab("BibInfo", panel4);
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
        
        JComponent panel1 = makeTextPanel("View");
        tabbedPane.addTab("The link to the pdf file", panel1);
        tabbedPane.setMnemonicAt(4, KeyEvent.VK_5);
        
        /*JComponent panel4 = makeTextPanel(
                "Panel #4 (has a preferred size of 410 x 50).");
        panel4.setPreferredSize(new Dimension(410, 50));
        tabbedPane.addTab("Tab 4", icon, panel4,
                "Does nothing at all");
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
        */
        //Add the tabbed pane to this panel.
        add(tabbedPane);
        
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JTextPane textPane = new JTextPane();
        //javax.swing.JScrollPane panel = new javax.swing.JScrollPane(textPane);
        textPane.setText(text);
        textPane.setEditable(false);
        panel.setLayout(new BorderLayout(1, 1));
        panel.add(textPane);
        return panel;
    }
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = TabInfo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    /*private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TabbedPaneDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Add content to the window.
        frame.add(new TabbedPaneDemo(), BorderLayout.CENTER);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		createAndShowGUI();
            }
        });
    }
    */

}
