package board;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class ViewFrame extends Frame{
	Image image;
	ArrayList<Blob> blobs;
	ViewFrame(String name, int x0, int y0, int w, int h){
		super(name);
		setBounds(x0,y0, w, h);
		setVisible(true);

		addWindowListener(
				  new WindowAdapter(){
					public void windowClosing(WindowEvent e) {System.exit(0);}}
					);
	
	}
	void setImageAndBlobs(Image image, ArrayList<Blob> blobs){
		this.image = image;
		this.blobs = blobs;
		repaint();
	}
	
	
	
	Image buffered_image = null;
	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {

			//double buffering
			if (buffered_image == null)
				buffered_image = createImage(getSize().width, getSize().height);
			Graphics bg = buffered_image.getGraphics();

			// clear
			//bg.setColor(Color.white);
			//bg.fillRect(0,0, getSize().width, getSize().height);

			paint_main(bg);
			
			bg.dispose();
      
			g.drawImage(buffered_image,0,0,this);
			//g.drawImage(buffered_image,0,0,800,800,0,0,200,200,this);

			
	}
	
	

	public void paint_main(Graphics g) {
		VisionClient.paint(g, image, blobs, 0);
	}
	

}
