package dz.nier.client.salon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import dz.nier.autre.bean.ClientBean;
import dz.nier.autre.bean.SalonBean;
import dz.nier.autre.inOutThread.BasicThread;
import dz.nier.autre.inOutThread.ClientBasicThread;
import dz.nier.autre.inOutThread.Distributteur;
import dz.nier.autre.request.RequestKeys;
import dz.nier.autre.request.RequestValue;
import dz.nier.autre.request.Requete;
import dz.nier.autre.utile.Utile;
import dz.nier.autre.utile.WindowClosingLinstener;

public class Chat extends JFrame {
	public static final Dimension DIMENSION = new Dimension(440, 430);
	public static final Dimension MESSAGE_FILED_DIMENSION = new Dimension(380,
			50);
	public static final Dimension MESSAGE_AREA_DIMENSION = new Dimension(380,
			150);
	public static final Dimension INFORMATION_TEXT_AREA_DIMENSION = new Dimension(
			180, 100);
	public static final Dimension MESSAGE_CONT_DIMENSION = new Dimension(400,
			240);
	public static final Dimension INFOMRMATION_GROUP_DIMENSION = new Dimension(
			400, 140);
	public static final Color MESSAGE_BACKGROUND_COLOR = new Color(50, 50, 50);
	public static final Color MESSAGE_TEXT_BACKGROUNDCOLOR = new Color(30, 30,
			30);
	public static final Color MESSAGE_FOREGROUND_COLOR = new Color(250, 230,
			235);
	public static final Dimension BUTTON_DIMENSION = new Dimension(100, 25);

	private BasicThread basicThread;
	private JPanel cont = new JPanel();
	private SalonBean salonBean;
	private MessageArea messageArea;
	private MessageArea message;
	private ClientBean client;
	private JButton envoi = new JButton("Envoyer");
	private JButton vider = new JButton("Vider");
	private JTextArea infoMombre = this.createMessageArea();
	private JTextArea activite = this.createMessageArea();

	public Chat(Distributteur d, long outId, SalonBean salon, ClientBean client) {
		this.client = client;
		this.salonBean = salon;
		this.init();
		this.basicThread = new ChatThread(d, outId);
	}

	private void init() {
		this.setTitle("Salon : " + salonBean.getNom());
		this.addWindowListener(new WindowClosingLinstener() {
			public void actionOnClose() {
				basicThread.desconnect();
			}
		});
		this.cont.setBackground(ChoixSalon.SALONS_PAN_COLOR);
		this.cont.add(this.initInformationGroup());
		this.initMessageGroup();
		this.initButton();
		this.cont.setPreferredSize(DIMENSION);
		this.setContentPane(cont);
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private JPanel initInformationGroup() {
		JPanel res = Utile.creeGroup("Information Salon",
				INFOMRMATION_GROUP_DIMENSION);
		JScrollPane p1 = new JScrollPane(infoMombre);
		JScrollPane p2 = new JScrollPane(activite);
		p1.setPreferredSize(INFORMATION_TEXT_AREA_DIMENSION);
		p2.setPreferredSize(INFORMATION_TEXT_AREA_DIMENSION);
		res.add(p1);
		res.add(p2);
		this.infoMombre.setEditable(false);
		this.activite.setEditable(false);
		return res;
	}

	private void initButton() {
		envoi.setPreferredSize(BUTTON_DIMENSION);
		vider.setPreferredSize(BUTTON_DIMENSION);
		envoi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				basicThread.out(new Requete(RequestKeys.MESSAGE, message
						.getText()));
				client.setNombreMessage(client.getNombreMessage() + 1);
				message.setText("");
			}
		});
		vider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				messageArea.setText("");
			}
		});
		this.cont.add(envoi);
		this.cont.add(vider);
	}

	private void initMessageGroup() {
		JPanel cont = Utile.creeGroup("Message", MESSAGE_CONT_DIMENSION);
		JScrollPane sp = new JScrollPane(messageArea = createMessageArea());
		sp.setPreferredSize(MESSAGE_AREA_DIMENSION);
		cont.add(sp);
		JScrollPane sp2 = new JScrollPane(message = createMessageArea());
		sp2.setPreferredSize(MESSAGE_FILED_DIMENSION);
		cont.add(sp2);
		this.messageArea.setEditable(false);
		this.cont.add(cont);
	}

	private MessageArea createMessageArea() {
		MessageArea messageArea = new MessageArea();
		messageArea.setBackground(MESSAGE_BACKGROUND_COLOR);
		messageArea.setForeground(MESSAGE_FOREGROUND_COLOR);
		messageArea.setCaretColor(MESSAGE_FOREGROUND_COLOR);
		messageArea.setMargin(new Insets(5, 5, 5, 5));
		return messageArea;
	}

	private void printMessage(String message, String auteur) {
		this.messageArea.append(auteur + "  :  " + message + "\n");
	}

	public class ChatThread extends ClientBasicThread {

		public ChatThread(Distributteur d, long outId) {
			super(d, outId, client.getId());
			this.setName("ecouteur message(client): client(" + client.getId()
					+ ") salon(" + salonBean.getId() + ")");
			this.start();
		}

		public void run() {
			Requete r = in();
			messageArea.setText((String) r.getAttribut(RequestKeys.HISTORY));
			activite.setText((String) r.getAttribut(RequestKeys.ACTIVITE));
			infoMombre.setText((String) r
					.getAttribut(RequestKeys.MOMBRE_EN_LIGNE));
			while (true) {
				r = in();
				if (this.connected) {
					if (((RequestValue) r.getAttribut(RequestKeys.RAISON))
							.equals(RequestValue.ENVOI_MESSAGE))
						printMessage(
								(String) r.getAttribut(RequestKeys.MESSAGE),
								(String) r.getAttribut(RequestKeys.AUTEUR));
					else {
						activite.append((String) r
								.getAttribut(RequestKeys.ACTIVITE));
						infoMombre.setText((String) r
								.getAttribut(RequestKeys.MOMBRE_EN_LIGNE));
					}
				} else
					return;
			}
		}

		public void desconnect() {
			super.desconnect();
			dispose();
		}
	}

	@SuppressWarnings("serial")
	class MessageArea extends JTextArea {
		public MessageArea() {
			this.setLineWrap(true);
			this.setFont(new Font("fixedsys", Font.BOLD | Font.ITALIC, 12));
		}

		public void clear() {
			this.setText("");
		}
	}
}
