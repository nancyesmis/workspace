package board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

class MainPanel extends Panel implements ButtonsInterface {

	MainPanel() {
		MouseDispatcher mouseDispatcher = new MouseDispatcher();
		addMouseListener(mouseDispatcher);
		addMouseMotionListener(mouseDispatcher);

		VisionClient.connect();
		SendServer.start_server();
	}

	TextField textField = new TextField("images\\photo1.png", 20);

	public void initializeButtons(Buttons buttons) {
		buttons.add(textField);
		buttons.addButton("load");
		buttons.addButton("detect");
	}

	public void buttonPressed(String label) {
		if (label.equals("load")) {
			// Detector.image = Main.loadImage(textField.getText());
			repaint();

		}
		if (label.equals("detect")) {
			// Detector.detect();
			// Detector.detect_video();
			repaint();
		}
	}

	Image buffered_image = null;

	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {

		// double buffering
		if (buffered_image == null)
			buffered_image = createImage(getSize().width, getSize().height);
		Graphics bg = buffered_image.getGraphics();

		// clear‚†ff
		bg.setColor(Color.white);
		bg.fillRect(0, 0, getSize().width, getSize().height);

		// Detector.paint(bg);
		// if (VisionClient.image != null)
		// bg.drawImage(VisionClient.image, 0,0, null);
		// VisionClient.paint(bg);

		// double buffering
		bg.dispose();

		g.drawImage(buffered_image, 0, 0, this);
		// g.drawImage(buffered_image,0,0,800,800,0,0,200,200,this);

	}

	public int operation_status;
	public static final int NONE = 0;

	//   
	// central event dispatcher
	//
	public class MouseDispatcher extends MouseAdapter implements
			MouseMotionListener {

		public void mouseMoved(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			Point p = e.getPoint();
			boolean right_button = (e.getModifiers() & e.BUTTON3_MASK) != 0;

			repaint();
		}

		public void mouseDragged(MouseEvent e) {
			Point p = e.getPoint();

			switch (operation_status) {
			}

		}

		public void mouseReleased(MouseEvent e) {
			Point p = e.getPoint();

			switch (operation_status) {
			}
		}

		public void mouseClicked(MouseEvent e) {
		}
	}
}
