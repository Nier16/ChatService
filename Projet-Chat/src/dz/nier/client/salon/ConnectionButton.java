package dz.nier.client.salon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import dz.nier.autre.bean.ClientBean;
import dz.nier.autre.bean.SalonBean;
import dz.nier.autre.inOutThread.BasicThread;

class ConnectButton extends JButton implements ActionListener {
	private static final long serialVersionUID = 1L;
	private SalonBean salon;
	private BasicThread bt;
	private ClientBean client;

	public ConnectButton(SalonBean salon, BasicThread bt, ClientBean client) {
		super("Connect");
		this.client = client;
		this.salon = salon;
		this.addActionListener(this);
		this.bt = bt;
	}

	public void actionPerformed(ActionEvent arg0) {
		new ConnectionSalonThread(bt.getDistributteur(), this.salon, client,
				bt.getOutId());
	}

}