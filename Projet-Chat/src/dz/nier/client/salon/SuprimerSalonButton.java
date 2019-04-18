package dz.nier.client.salon;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import dz.nier.autre.bean.SalonBean;
import dz.nier.autre.inOutThread.BasicThread;
import dz.nier.autre.request.RequestKeys;
import dz.nier.autre.request.RequestValue;
import dz.nier.autre.request.Requete;

class SuprimerSalonButton extends JButton {
	public static final Dimension DIMENSION = new Dimension(380, 20);

	private SalonBean salon;
	private BasicThread bt;

	public SuprimerSalonButton(SalonBean s, BasicThread b) {
		super("Suprimer");
		this.bt = b;
		this.setPreferredSize(DIMENSION);
		this.salon = s;
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bt.out(new Requete(new Object[][] {
						{ RequestKeys.RAISON, RequestValue.SUPPRIMER_SALON },
						{ RequestKeys.SALON, salon } }));
			}
		});
	}
}