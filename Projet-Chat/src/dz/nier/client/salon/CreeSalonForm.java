package dz.nier.client.salon;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import dz.nier.autre.bean.SalonBean;
import dz.nier.autre.utile.observator.ObservableClass;
import dz.nier.autre.utile.observator.Observateur;
import dz.nier.client.utile.roundComp.RondBorderPanel;

public class CreeSalonForm extends RondBorderPanel {

	public static final Dimension DIMENSION = new Dimension(500, 140);

	private JButton creeSalon = new JButton("cree Salon");
	private JTextField passwd = new JTextField();
	private JComboBox<SalonBean.Type> type = new JComboBox<SalonBean.Type>(
			SalonBean.Type.values());
	private JTextField sujet = new JTextField();
	private JTextField nom = new JTextField();
	private ObservableClass<Void> observable;

	public CreeSalonForm(Observateur<Void> observateur) {
		this.observable = new ObservableClass<>(observateur);
		this.init();
	}

	private void init() {
		this.setPreferredSize(DIMENSION);
		this.setBorder(BorderFactory.createTitledBorder("Creation Salon"));
		this.setBackground(ChoixSalon.BACKGROUND_COLOR);
		this.add(ChoixSalon.creeForm("Nom(*) : ", this.nom));
		this.add(ChoixSalon.creeForm("Sujet", this.sujet));
		this.add(ChoixSalon.creeForm("Type(*) : ", this.type));
		this.add(ChoixSalon.creeForm("Passwd(*) : ", this.passwd));
		this.add(this.creeSalon);
		this.initAction();
	}

	private void initAction() {
		this.type.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (type.getSelectedItem().equals(SalonBean.Type.PRIVE))
					passwd.setEnabled(true);
				else
					passwd.setEnabled(false);
			}
		});
		this.type.setSelectedIndex(0);
		this.creeSalon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				observable.updateObservateur(null);
			}
		});
	}

	public JButton getCreeSalon() {
		return creeSalon;
	}

	public void setCreeSalon(JButton creeSalon) {
		this.creeSalon = creeSalon;
	}

	public JTextField getPasswd() {
		return passwd;
	}

	public void setPasswd(JTextField passwd) {
		this.passwd = passwd;
	}

	public JComboBox<SalonBean.Type> getType() {
		return type;
	}

	public void setType(JComboBox<SalonBean.Type> type) {
		this.type = type;
	}

	public JTextField getSujet() {
		return sujet;
	}

	public void setSujet(JTextField sujet) {
		this.sujet = sujet;
	}

	public JTextField getNom() {
		return nom;
	}

	public void setNom(JTextField nom) {
		this.nom = nom;
	}
}
