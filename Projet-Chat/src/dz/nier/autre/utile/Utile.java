package dz.nier.autre.utile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dz.nier.autre.request.Requete;
import dz.nier.client.salon.ChoixSalon;
import dz.nier.client.utile.roundComp.RondBorderPanel;

public abstract class Utile {

	public static final int CONFIRMATION_DELAY = 18000;
	public static final int ERROR_DELAY = 500;
	public static final int NOMBRE_BLAC_PETIT_TAB = 5;

	public static JPanel creeGroup(String name, Dimension dim) {
		JPanel res = new RondBorderPanel();
		res.setPreferredSize(dim);
		res.setBorder(BorderFactory.createTitledBorder(name));
		res.setBackground(ChoixSalon.BACKGROUND_COLOR);
		return res;
	}

	public static JPanel creeForm(String labelText, JComponent field,
			Dimension labelDim, Dimension filedDim, Dimension formDimension) {
		JPanel res = new RondBorderPanel();
		res.setPreferredSize(formDimension);
		JLabel label = new JLabel(labelText);
		label.setPreferredSize(labelDim);
		field.setPreferredSize(filedDim);
		res.add(label);
		res.add(field);
		res.setBackground(ChoixSalon.SALONS_PAN_COLOR);
		return res;
	}

	public static void showErrorDialog(String text, String titre) {
		JOptionPane.showMessageDialog(null, text, titre,
				JOptionPane.ERROR_MESSAGE);
	}

	public static JFormattedTextField initMessageServer(Dimension dim) {
		JFormattedTextField messageServer = new JFormattedTextField();
		messageServer.setPreferredSize(dim);
		messageServer.setEditable(false);
		messageServer.setBackground(Color.black);
		messageServer.setForeground(Color.white);
		messageServer.setMargin(new Insets(4, 8, 4, 4));
		return messageServer;
	}

	public static void sendRequest(ObjectOutputStream oos, Requete r) {
		try {
			oos.writeObject(r);
			oos.flush();
		} catch (IOException e) {
			Utile.showErrorDialog("impossible de contacter le server", "erreur");
			e.printStackTrace();
		}
	}

	public static Requete takeRequest(ObjectInputStream ois) {
		Requete res = null;
		try {
			try {
				res = (Requete) ois.readObject();
			} catch (ClassNotFoundException e) {
				Utile.showErrorDialog("impossible de contacter le server",
						"erreur");
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
			Utile.showErrorDialog("impossible de contacter le server", "erreur");
		}
		return res;
	}

	public static ObjectOutputStream creeOOS(Socket s) {
		try {
			return new ObjectOutputStream(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ObjectInputStream creeOIS(Socket s) {
		try {
			return new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String tab(int nombreBlanc) {
		String res = new String();
		for (int i = 0; i < nombreBlanc; i++)
			res += " ";
		return res;
	}

	public static String petitTab() {
		return tab(NOMBRE_BLAC_PETIT_TAB);
	}
}
