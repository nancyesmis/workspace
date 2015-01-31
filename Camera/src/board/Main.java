package board;
import java.awt.event.*;
import java.awt.*;
import java.applet.*;
import java.util.*;
public class Main {

  

    // independent application
    public static Frame f;
		public static void main(String args[]) {
			
			
		f = new Frame("Main");

		f.addWindowListener(
		  new WindowAdapter(){
			public void windowClosing(WindowEvent e) {System.exit(0);}
			public void windowActivated(WindowEvent e) {
				if (VisionClient.calibrate())
					System.out.println("Calibrated.");
			}
			});


		Panel panel = new Panel();
		
		panel.setLayout(new BorderLayout());
		mainPanel = new MainPanel();
		panel.add("Center", mainPanel);
		panel.add("South",new Buttons(mainPanel));
		
		f.add("Center", panel);
		f.setSize(1600, 800);
		f.setVisible(true);



		
	}
	static	MainPanel mainPanel;
	

	static public Image loadImage( String pathname )
	{
		Image image = f.getToolkit().getImage( pathname );
		if (image != null)
		{
			MediaTracker mt = new MediaTracker(f);
			try
			{
				mt.addImage(image, 0);
				mt.waitForAll();
			}	catch (InterruptedException ie) {}
		}
		return image;
	}	

}

