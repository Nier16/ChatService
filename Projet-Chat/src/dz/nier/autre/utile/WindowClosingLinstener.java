package dz.nier.autre.utile;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public abstract class WindowClosingLinstener implements WindowListener {
	public void windowActivated(WindowEvent arg0) {
	}

	public void windowClosed(WindowEvent arg0) {
	}

	public void windowClosing(WindowEvent arg0) {
		this.actionOnClose();
	}

	public void windowDeactivated(WindowEvent arg0) {
	}

	public void windowDeiconified(WindowEvent arg0) {
	}

	public void windowIconified(WindowEvent arg0) {
	}

	public void windowOpened(WindowEvent arg0) {
	}

	abstract public void actionOnClose();
}
