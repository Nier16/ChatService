package dz.nier.client.utile.roundComp;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

public class RondBorderScrollPane extends JScrollPane {
	protected int arcX = 15;
	protected int arcY = 15;

	public RondBorderScrollPane(JComponent comp) {
		super(comp);
	}

	public RondBorderScrollPane(int arcX, int arcY, JComponent comp) {
		super(comp);
		this.arcX = arcX;
		this.arcY = arcY;
	}

	protected void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRoundRect(0, 0, getWidth(), getHeight(), this.arcX, this.arcY);
	}
}
