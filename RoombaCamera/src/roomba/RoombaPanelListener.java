package roomba;
//
// RoombaPanelListener.java
//

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
public final class RoombaPanelListener implements ComponentListener,
		KeyListener, MouseListener, MouseMotionListener, WindowListener {

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	private MagicCardRoombaPanel rp;

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	RoombaPanelListener(MagicCardRoombaPanel rp) {
		this.rp = rp;
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void componentHidden(ComponentEvent ce) {
	}

	public void componentMoved(ComponentEvent ce) {
	}

	public void componentResized(ComponentEvent ce) {
		if (ce.getComponent() == rp) {
			rp.resized();
			rp.repaint();
		}
	}

	public void componentShown(ComponentEvent ce) {
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void keyPressed(KeyEvent ke) {
		int code = ke.getKeyCode();
		if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_Q) {
			rp.quit();
			return;
		}

		rp.keyPressed(code);
	}

	public void keyReleased(KeyEvent ke) {
		rp.keyReleased(ke.getKeyCode());
	}

	public void keyTyped(KeyEvent ke) {
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void mousePressed(MouseEvent me) {
		if ((me.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
			rp.quit();
			return;
		}

		rp.mousePressed(me.getX(), me.getY());
	}

	public void mouseReleased(MouseEvent me) {
	}

	public void mouseClicked(MouseEvent me) {
	}

	public void mouseEntered(MouseEvent me) {
	}

	public void mouseExited(MouseEvent me) {
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void mouseMoved(MouseEvent me) {
	}

	public void mouseDragged(MouseEvent me) {
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void windowOpened(WindowEvent we) {
		rp.repaint();
	}

	public void windowClosed(WindowEvent we) {
	}

	public void windowClosing(WindowEvent we) {
		rp.quit();
	}

	public void windowActivated(WindowEvent we) {
		rp.repaint();
	}

	public void windowDeactivated(WindowEvent we) {
	}

	public void windowIconified(WindowEvent we) {
	}

	public void windowDeiconified(WindowEvent we) {
		rp.repaint();
	}
}
