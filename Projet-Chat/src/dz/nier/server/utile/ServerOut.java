package dz.nier.server.utile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public abstract class ServerOut {
	public static final Dimension DIMENSION = new Dimension(500, 400);
	public static final String NAME = "Chat Server Message";

	private static JFrame screen;
	private static JTextArea message;

	public static void init() {
		screen = new JFrame();
		screen.setName(NAME);
		screen.setSize(DIMENSION);
		screen.setLocationRelativeTo(null);
		message = new JTextArea();
		message.setBackground(Color.black);
		message.setForeground(Color.white);
		message.setMargin(new Insets(8, 8, 8, 8));
		message.setLineWrap(true);
		message.setEditable(false);
		screen.setContentPane(new JScrollPane(message));
		screen.setTitle("ServerOut");
		screen.setVisible(true);
	}

	public static void out(String text) {
		message.append("-" + text + "\n");
	}

	public static void clear() {
		message.setText("");
	}

	public static boolean isOpen() {
		return screen.isVisible();
	}
}
