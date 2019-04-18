package dz.nier.client.salon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import dz.nier.autre.bean.ClientBean;
import dz.nier.autre.bean.SalonBean;
import dz.nier.autre.inOutThread.ClientBasicThread;
import dz.nier.autre.inOutThread.ClientBasicThreadCont;
import dz.nier.autre.inOutThread.Distributteur;
import dz.nier.autre.request.RequestKeys;
import dz.nier.autre.request.RequestValue;
import dz.nier.autre.request.Requete;
import dz.nier.autre.utile.Utile;
import dz.nier.autre.utile.WindowClosingLinstener;
import dz.nier.autre.utile.observator.Observateur;
import dz.nier.client.connecitonServer.Connection;
import dz.nier.client.utile.ConnectionConfirmation;

public class ChoixSalon extends JFrame implements Observateur<Void> {
	public static final Color BACKGROUND_COLOR = new Color(200, 200, 230);
	public static final Color SALONS_PAN_COLOR = new Color(220, 220, 250);
	public static final Dimension CON_DIMENSION = new Dimension(520, 605);
	public static final Color TEXT_AREA_BACKGROUND_COLOR = new Color(220, 240,
			245);
	public static final Dimension CHOIX_SALON_DIMENSION = new Dimension(500,
			300);
	public static final Dimension PSEUDO_PANEL_DIMENSION = new Dimension(500,
			65);
	public static final Dimension SALON_PAN_DIMENSION = new Dimension(400, 150);
	public static final Dimension TEXT_AREA_DIMESNION = new Dimension(380, 60);
	public static final Dimension SUCEE_INFO_PAN_DIMENSION = new Dimension(400,
			60);
	public static final Dimension PROGRESS_BAR_DIMESNION = new Dimension(150,
			30);
	public static final Dimension LABEL_DIMENSION = new Dimension(60, 20);
	public static final Dimension FIELD_DIMENSION = new Dimension(100, 22);
	public static final Dimension FORM_DIMENSION = new Dimension(200, 30);
	public static final Dimension MESSAGE_SERVER_GROUP_DIMENSION = new Dimension(
			380, 70);
	public static final Dimension MESSAGE_SERVER_DIMENSION = new Dimension(360,
			30);
	public static final Dimension ACTION_GROUP_DIMENSION = new Dimension(110,
			70);

	public final static String NAME = "Choix Du Salon";
	public final static String PUBLIC_GROUP_NAME = "Salons publics";
	public final static String PRIVATE_GROUP_NAME = "Salons priveés";
	public final static String OTHER_GROUP_NAME = "Mombres en ligne";
	public final static String CREATE_SALON_GROUP_NAME = "Crée un Salon";
	public final static String ACTION_GROUP_NAME = "Actions";

	private ClientBasicThread bt;
	private ClientBean client;
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JFormattedTextField serverMessage = new JFormattedTextField();
	private JButton refresh = new JButton("Rafréshir");
	private JLabel pseudo = new JLabel();
	private ConnectionConfirmation connectionConfirmation;
	private CreeSalonForm creeSalonForm = new CreeSalonForm(this);

	public ChoixSalon(long outId, Distributteur d, ClientBean c) {
		connectionConfirmation = new ConnectionConfirmation(d, c.getId());
		this.client = c;
		this.setContentPane(init(outId, d));
		this.pack();
		this.setLocationRelativeTo(null);
		this.setTitle(NAME);
		this.setResizable(false);
		this.addWindowListener(new WindowClosingLinstener() {
			public void actionOnClose() {
				bt.desconnect();
				connectionConfirmation.desconnect();
				ClientBasicThreadCont.getInstance().desconnectClient(
						client.getId());
			}
		});
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public JPanel init(long outId, Distributteur d) {
		JPanel res = new JPanel();
		res.setPreferredSize(CON_DIMENSION);
		tabbedPane.setBackground(BACKGROUND_COLOR);
		tabbedPane.setPreferredSize(CHOIX_SALON_DIMENSION);
		this.initBasicThread(outId, d);
		this.initChoixSalon();
		res.add(initPseudoPanel());
		res.add(this.tabbedPane);
		res.add(this.creeSalonForm);
		res.add(initMessageServer());
		res.add(initActionGroup());
		this.initAction();
		res.setBackground(SALONS_PAN_COLOR);
		res.repaint();
		return res;
	}

	private JPanel initPseudoPanel() {
		JPanel res = Utile.creeGroup("vos Information", PSEUDO_PANEL_DIMENSION);
		res.setBackground(BACKGROUND_COLOR);
		this.pseudo.setText(this.client.getPseudo());
		this.pseudo.setForeground(new Color(120, 150, 120));
		this.pseudo.setFont(new Font("areal", Font.ITALIC, 25));
		res.add(pseudo);
		return res;
	}

	private JPanel initMessageServer() {
		JPanel res = Utile.creeGroup(Connection.MESSAGE_GROUP_NAME,
				MESSAGE_SERVER_GROUP_DIMENSION);
		res.add(new JScrollPane(this.serverMessage = Utile
				.initMessageServer(MESSAGE_SERVER_DIMENSION)));
		return res;
	}

	private void initAction() {
		this.refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bt.out(new Requete(RequestKeys.RAISON,
						RequestValue.REFRESH_SALON_LIST));
			}
		});
	}

	private JPanel initActionGroup() {
		JPanel res = Utile.creeGroup(ACTION_GROUP_NAME, ACTION_GROUP_DIMENSION);
		res.add(this.refresh);
		this.refresh.setMnemonic('R');
		return res;
	}

	private void initBasicThread(long outId, Distributteur d) {
		bt = new ClientBasicThread(d, outId, this.client.getId());
		bt.setName("Choix salon client");
	}

	@SuppressWarnings("unchecked")
	private void initChoixSalon() {
		Thread update = new Thread(new Runnable() {
			public void run() {
				do {
					Requete r = bt.in();
					if (!bt.isConnected())
						return;
					tabbedPane.removeAll();
					tabbedPane.revalidate();
					ArrayList<SalonBean> salonPublic = (ArrayList<SalonBean>) r
							.getAttribut(RequestKeys.SALON_PUBLIC);
					ArrayList<SalonBean> salonPrivee = (ArrayList<SalonBean>) r
							.getAttribut(RequestKeys.SALON_PRIVEE);
					ArrayList<SalonBean> salonMombre = (ArrayList<SalonBean>) r
							.getAttribut(RequestKeys.MOMBRES_EN_LIGNES);
					tabbedPane.add(
							new JScrollPane(creeGroupSalon(salonPublic)),
							PUBLIC_GROUP_NAME, 0);
					tabbedPane.add(
							new JScrollPane(creeGroupSalon(salonPrivee)),
							PRIVATE_GROUP_NAME, 1);
					tabbedPane.add(
							new JScrollPane(creeGroupSalon(salonMombre)),
							OTHER_GROUP_NAME, 2);
				} while (isVisible());
			}
		});
		update.setName("Client Refresh Salon list");
		update.start();
	}

	private JPanel creeGroupSalon(ArrayList<SalonBean> salons) {
		JPanel res = new JPanel();
		res.setPreferredSize(new Dimension(SALON_PAN_DIMENSION.width + 20,
				(SALON_PAN_DIMENSION.height + 5) * salons.size() + 10));
		for (SalonBean salon : salons)
			res.add(creeChoix(salon));
		res.setBackground(SALONS_PAN_COLOR);
		return res;
	}

	private JPanel creeChoix(SalonBean salon) {
		JPanel res = creeSalonInfo(salon);
		res.add(new ConnectButton(salon, bt, client));
		if (salon.getcreateur().equals(client))
			res.add(new SuprimerSalonButton(salon, bt));
		return res;
	}

	public static JPanel creeSalonInfo(SalonBean salon) {
		JPanel res = Utile.creeGroup("Salon(" + salon.getId() + ")",
				SALON_PAN_DIMENSION);
		JTextArea textArea = new JTextArea(salon.toString());
		textArea.setEditable(false);
		textArea.setMargin(new Insets(5, 5, 5, 5));
		textArea.setBackground(TEXT_AREA_BACKGROUND_COLOR);
		JScrollPane scrollArea = new JScrollPane(textArea);
		scrollArea.setPreferredSize(TEXT_AREA_DIMESNION);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setPreferredSize(PROGRESS_BAR_DIMESNION);
		progressBar.setMinimum(0);
		progressBar.setMaximum(64);
		progressBar.setValue(salon.getNombreMombre());
		progressBar.setStringPainted(true);

		res.add(scrollArea);
		res.add(new JLabel("mombres : " + salon.getNombreMombre() + " / " + 64));
		res.add(progressBar);
		return res;
	}

	public static JPanel creeForm(String labelText, JComponent field) {
		JPanel res = Utile.creeForm(labelText, field, LABEL_DIMENSION,
				FIELD_DIMENSION, FORM_DIMENSION);
		res.setBackground(SALONS_PAN_COLOR);
		return res;
	}

	public void update(Void arg) {
		new CreeSalonThread(this.bt.getDistributteur(), bt.getOutId(),
				creeSalonForm, client);
	}
}
