import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JTextPane;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import java.sql.*;

public class CitationInfo extends JPanel{
	
	private String paperTitle = "";
	
		public CitationInfo(String title) {
	        super(new GridLayout(1, 1));
	        
	        paperTitle = title;
	        JTabbedPane tabbedPane = new JTabbedPane();
	        //ImageIcon icon = createImageIcon("images/middle.gif");
	        
	        //
	        DBConn dbconn = new DBConn();
	        String sql   = "";
	        ResultSet rs = null; 
	        DataManagement dm = new DataManagement();
	        try{
	        	sql = "select * from reference where paperTitle = '" + title + "'";
	        	rs  = dbconn.executeQuery(sql);
	        	int count  = 0;
	        	int refPos = 0;
	        	 while(rs.next()){
	        		 count++;
	        		 refPos = Integer.parseInt((rs.getObject("refPos")).toString());
	        		 int start = refPos - 50;
	        		 if(start < 0) start = 0;
	        		 
	        		 int end   = refPos + 50;
	        		 if(end >= dm.getText().length()) end = dm.getText().length();
	        		 
	        		 System.out.println("dm.getText is " + dm.getText());
	        		 
	        		 System.out.println("start = " + start +  ", End = " + end );
	        		 
	        		 String contextContent = dm.getText().substring(start, end - 1 );
	        		 
	        		 JComponent panel = makeTextPanel(contextContent);
	        		 tabbedPane.addTab("[" + count + "]" , panel);
	        		 //tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
	        	 }
	        }catch (Exception e){
	        	e.printStackTrace();
	        }
	       
	        /*JComponent panel1 = makeTextPanel("Citation Postion 1");
	        tabbedPane.addTab("[1]", panel1);
	        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
	        
	        JComponent panel2 = makeTextPanel("Citation Positon 2");
	        tabbedPane.addTab("[2]", panel2);
	        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
	        
	        JComponent panel3 = makeTextPanel("Citation Position 3");
	        tabbedPane.addTab("[3]", panel3);
	        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);*/
	        
	        //Add the tabbed pane to this panel.
	        add(tabbedPane);
	        
	        //The following line enables to use scrolling tabs.
	        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	    }
	    
	    protected JComponent makeTextPanel(String text) {
	        JPanel panel = new JPanel(false);
	        JTextPane textPane = new JTextPane();
	        textPane.setText(text);
	        textPane.setEditable(false);
	        panel.setLayout(new BorderLayout(1, 1));
	        panel.add(textPane);
	        return panel;
	    }
	    
	    /** Returns an ImageIcon, or null if the path was invalid. */
	    protected static ImageIcon createImageIcon(String path) {
	        java.net.URL imgURL = CitationInfo.class.getResource(path);
	        if (imgURL != null) {
	            return new ImageIcon(imgURL);
	        } else {
	            System.err.println("Couldn't find file: " + path);
	            return null;
	        }
	    }
}
