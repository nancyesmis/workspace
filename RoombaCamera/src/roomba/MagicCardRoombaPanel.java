package roomba;
//
// RoombaPanel.java
//

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;



/**
 * Modified by Shen to add methods to control the port number
 */
public final class MagicCardRoombaPanel extends JComponent {

	//set this value to false when we want to run with real roombas
	public static final boolean DEBUG_GUI = false;
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	private static final boolean FULL_SCREEN = false;
	private static final int DEFAULT_WIDTH = 640;
	private static final int DEFAULT_HEIGHT = 480;
	private static final int DEFAULT_RADIUS = 200;

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// application specific data
	//private static final String PORT = "COM47";
	private static String PORT = "";

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	private RoombaPanelListener listener;
	private JFrame frame;
	//private Image paintBuffer;

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// application specific data
	private MagicCardRoombaController roomba;

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// keyboard state
	private boolean up = false;
	private boolean down = false;
	private boolean left = false;
	private boolean right = false;

	private int current_speed = 200;

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	
	public MagicCardRoombaPanel(String port) {
		PORT = port;
		if(!DEBUG_GUI)
			listener = new RoombaPanelListener(this);
		//frame = new Frame(getClass().getName());
		//paintBuffer = createImage(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		//this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		if(!DEBUG_GUI){
			roomba = new MagicCardRoombaController(PORT);
			roomba.setRoombaPanel(this);
		}

		setBackground(Color.white);

		addComponentListener(listener);
		//keyboard listener will not be used, because we have several roombas
		//I need to figure out a way to make the keyboard sensitive to different
		//roombas
		//addKeyListener(listener);
		addMouseMotionListener(listener);
		addMouseListener(listener);
	}
	

	/**
	 * example for comm port number is "COM46"
	 * However, setting the PORT number will not be used
	 * since this number is used in the constructor 
	 * @param comm port number, e.g., "COM46"
	 */
	private void setCommPort(String comm){
		PORT = comm;
	}
	
	public String getCommPort(){
		return PORT;
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void quit() {
		if (frame != null)
			frame.dispose();

		//if (paintBuffer != null)
		//	paintBuffer.flush();

		roomba.quit();

		System.exit(0);
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void resized() {
		//paintBuffer = createImage(getWidth(), getHeight());
		repaint();
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	private void checkKey() {
		if (up) {
			if (left)
				drive(current_speed, DEFAULT_RADIUS);
				//turnLeft();
			else if (right)
				drive(current_speed, -DEFAULT_RADIUS);
				//turnRight();
			else goForward();
		} else if (down) {
			if (left)
				drive(-current_speed, DEFAULT_RADIUS);
			else if (right)
				drive(-current_speed, -DEFAULT_RADIUS);
			else goBackward();
		}
		else if (left)
			spinLeft();
		else if (right)
			spinRight();
		else
			stop();
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void keyPressed(int code) {
		switch (code) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_K:
			up = true;
			break;

		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_J:
			down = true;
			break;

		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_H:
			left = true;
			break;

		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_L:
			right = true;
			break;

		case KeyEvent.VK_C:
			control();
			break;
			
		case KeyEvent.VK_V:
			clean();
			break;

		case KeyEvent.VK_S:
			stop();
			break;

		case KeyEvent.VK_P:
			spot();
			break;

		default:
			break;
		}
		checkKey();
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void keyReleased(int code) {
		switch (code) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_K:
			up = false;
			break;

		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_J:
			down = false;
			break;

		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_H:
			left = false;
			break;

		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_L:
			right = false;
			break;

		case KeyEvent.VK_0:
			current_speed = 200;
			setSpeed(current_speed);
			break;

		case KeyEvent.VK_1:
			current_speed = 500;
			setSpeed(current_speed);
			break;

		default:
			break;
		}
		checkKey();
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void mousePressed(int x, int y) {
		if (y < getHeight() / 4) {
			goForward();
		} else if (y < getHeight() / 2) {
			if (x < getWidth() / 3)
				spinLeft();
			else if (x < getWidth() * 2 / 3)
				stop();
			else
				spinRight();
		} else if (y < getHeight() * 3 / 4) {
			goBackward();
		} else {
			if (x < getWidth() / 3)
				clean();
			else if ((x > getWidth() / 3)&&(x < 2*getWidth() / 3))
				control();
			else
				spot();
		}
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	/*public void update(Graphics g) {
		Graphics gbuf = paintBuffer.getGraphics();

		updatePanel(gbuf);
		for (Component c : getComponents())
			c.update(gbuf);

		paint(g);
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void paint(Graphics g) {
		g.drawImage(paintBuffer, 0, 0, this);
	}*/
	
	public void paint(Graphics g) {
		updatePanel(g);		
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	private void updatePanel(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.clearRect(0, 0, getWidth(), getHeight());

		g2d.drawLine(0, getHeight() / 4, getWidth(), getHeight() / 4);
		g2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
		g2d.drawLine(0, getHeight() * 3 / 4, getWidth(), getHeight() * 3 / 4);
		g2d.drawLine(getWidth() / 3, getHeight() / 4, getWidth() / 3,
				getHeight() / 2);
		g2d.drawLine(getWidth() * 2 / 3, getHeight() / 4, getWidth() * 2 / 3,
				getHeight() / 2);
		g2d.drawLine(getWidth() / 2, getHeight() * 3 / 4, getWidth() / 2,
				getHeight());

		int xOffset = 3;
		int yOffset = -3;
		g2d.drawString("goForward", 0 + xOffset, getHeight() / 4 + yOffset);
		g2d.drawString("spinLeft", 0 + xOffset, getHeight() / 2 + yOffset);
		g2d.drawString("stop", getWidth() / 3 + xOffset, getHeight() / 2
				+ yOffset);
		g2d.drawString("spinRight", getWidth() * 2 / 3 + xOffset, getHeight() / 2
				+ yOffset);
		g2d.drawString("goBackward", 0 + xOffset, getHeight() * 3 / 4 + yOffset);
		g2d.drawString("clean", 0 + xOffset, getHeight() + yOffset);
		g2d.drawString("control", getWidth() / 3 + xOffset, getHeight()
						+ yOffset);
		g2d.drawString("spot", 2*getWidth() / 3 + xOffset, getHeight()
				+ yOffset);
		
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	private void drive(int v, int s) {
		roomba.drive(v, s);
	}

	private void setSpeed(int s) {
		roomba.setSpeed(s);
	}

	private void goForward() {
		roomba.goForward();
	}

	private void goBackward() {
		roomba.goBackward();
	}

	private void spinLeft() {
		roomba.spinLeft();
	}

	private void spinRight() {
		roomba.spinRight();
	}

	private void turnLeft() {
		roomba.turnLeft();
	}

	private void turnRight() {
		roomba.turnRight();
	}

	private void stop() {
		roomba.stop();
	}

	private void clean() {
		roomba.clean();
		roomba.pause(30);
		roomba.clean();
		roomba.pause(30);
	}

	private void spot() {
		roomba.spot();
		roomba.pause(30);
		roomba.spot();
		roomba.pause(30);
	}

	private void control() {
		roomba.control();
		roomba.pause(30);
	}
	

	public MagicCardRoombaController getRoombaController() {
		return roomba;
	}

	public void setRoombaController(MagicCardRoombaController roomba) {
		this.roomba = roomba;
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public static void main(String args[]) {
		MagicCardRoombaPanel rp = new MagicCardRoombaPanel("COM41");
		
		JFrame frame = new JFrame();
	     
        // Add button to the frame.
        frame.getContentPane().add(rp, BorderLayout.CENTER);
   
        // Set initial size.
        frame.setSize(300,
           300);
    
        // Show the frame.
        frame.setVisible(true);
	}
}
