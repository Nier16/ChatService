package dz.nier.client.utile.roundComp;
import java.awt.Graphics;

import javax.swing.JLabel;

public class RondBorderLabel extends JLabel {
	protected int arcX = 15;
	protected int arcY = 15;

	public RondBorderLabel(String text) {
		super(text);
	}

	public RondBorderLabel(int arcX, int arcY, String text) {
		super(text);
		this.arcX = arcX;
		this.arcY = arcY;
	}

	protected void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRoundRect(0, 0, getWidth(), getHeight(), this.arcX, this.arcY);
	}
}
