import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JTextArea;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;


public class PaperInfo extends JPanel{
	public PaperInfo() {
        super(new GridLayout(1, 1));
        
        JTabbedPane tabbedPane = new JTabbedPane();
        //ImageIcon icon = createImageIcon("images/middle.gif");
        
        JComponent panel1 = makeTextPanel("View");
        tabbedPane.addTab("View", panel1);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
        JComponent panel2 = makeTextPanel("Some information about the paper could be shown here. Like the sentences that summarizes all the information about what other people has to say before they actually add the paper to their reference.");
        tabbedPane.addTab("Analysis", panel2);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        JComponent panel3 = makeTextPanel("BibInfo");
        tabbedPane.addTab("BibInfo", panel3);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
        
        //Add the tabbed pane to this panel.
        add(tabbedPane);
        
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JTextArea textarea = new JTextArea(text);
        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(textarea);
      
        return panel;
    }
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = PaperInfo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
