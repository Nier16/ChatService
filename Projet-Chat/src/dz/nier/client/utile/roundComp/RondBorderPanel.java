package dz.nier.client.utile.roundComp;

import java.awt.Graphics;

import javax.swing.JPanel;

public class RondBorderPanel extends JPanel {

	protected int arcX = 20;
	protected int arcY = 20;

	public RondBorderPanel() {

	}

	public RondBorderPanel(int arcX, int arcY) {
		this.arcX = arcX;
		this.arcY = arcY;
	}

	protected void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRoundRect(0, 0, getWidth(), getHeight(), this.arcX, this.arcY);
	}

}
