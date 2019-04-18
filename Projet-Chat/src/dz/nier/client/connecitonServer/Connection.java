package dz.nier.client.connecitonServer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import dz.nier.autre.bean.ClientBean;
import dz.nier.autre.inOutThread.BasicThread;
import dz.nier.autre.inOutThread.Distributteur;
import dz.nier.autre.request.RequestKeys;
import dz.nier.autre.request.RequestValue;
import dz.nier.autre.request.Requete;
import dz.nier.autre.utile.Utile;
import dz.nier.client.salon.ChoixSalon;
import dz.nier.client.utile.PasswdField;


public class Connection extends JFrame {
	private static final long serialVersionUID = 1L;
	public static final Dimension DIMENSION = new Dimension(350, 645);
	public static final Dimension MOMBRE_DIMENSION = new Dimension(300, 140);
	public static final Dimension ANONYME_DIMENSION = new Dimension(300, 110);
	public static final Dimension INSCRIPTION_GROUP_DIMENSION = new Dimension(
			300, 255);
	public static final Dimension LABEL_DIM = new Dimension(95, 22);
	public static final Dimension TEXT_FILED_DIM = new Dimension(100, 24);
	public static final Dimension FORM_DIM = new Dimension(215, 35);
	public static final Dimension MESSAGE_GROUP_DIMENSION = new Dimension(300,
			70);
	public static final Dimension MESSAGE_SEREVER_DIMENSION = new Dimension(
			260, 35);

	public static final String NAME = "Connection";
	public static final String MOMBRE_GROUP_NAME = "Mombre";
	public static final String ANONYME_GROUP_NAME = "Anonyme";
	public static final String INSCRIPTION_GROUP_NAME = "Inscription";
	public static final String MESSAGE_GROUP_NAME = "Message du Server";
	public static final String PSEUDO_REGEX = "^.{3,18}$";
	public static final String PASSWD_REGEX = "^.{6,22}$";

	private JTextField pseudo = new JTextField();
	private PasswdField motDePass = new PasswdField();
	private PasswdField motDePassInscription = new PasswdField();
	private PasswdField confirmationMotDePass = new PasswdField();
	private JTextField anonyme = new JTextField();
	private JTextField nom = new JTextField();
	private JTextField prenom = new JTextField();
	private JTextField pseudoInscription = new JTextField();
	private JFormattedTextField messageServer = new JFormattedTextField();
	private JButton connectionMombe = new JButton("connection");
	private JButton connectionAnonyme = new JButton("connection");
	private JButton inscription = new JButton("Inscription");
	private Distributteur distributeur;

	public Connection(Socket s) {
		this.connectToServer(s);
		this.init();
	}

	public void connectToServer(final Socket s) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				ObjectOutputStream oos = Utile.creeOOS(s);
				ObjectInputStream ois = Utile.creeOIS(s);
				distributeur = new Distributteur(ois, oos);
			}
		});
		t.start();
	}

	private void init() {
		this.setTitle(NAME);
		JPanel pan = new JPanel();
		this.setSize(DIMENSION);
		this.setLocationRelativeTo(null);
		pan.setBackground(ChoixSalon.SALONS_PAN_COLOR);
		pan.add(this.initMombreGroup());
		pan.add(this.initAnonymeGroup());
		pan.add(this.initInscriptionGroup());
		pan.add(this.creeMessageServer());
		pan.setPreferredSize(DIMENSION);
		this.initAction();
		this.setContentPane(pan);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private JPanel initMombreGroup() {
		JPanel res = Utile.creeGroup(MOMBRE_GROUP_NAME, MOMBRE_DIMENSION);
		res.add(Connection.creeForm("pseudo : ", this.pseudo));
		res.add(Connection.creeForm("mot de pass : ", this.motDePass));
		res.add(this.connectionMombe);
		return res;
	}

	private JPanel initAnonymeGroup() {
		JPanel res = Utile.creeGroup(ANONYME_GROUP_NAME, ANONYME_DIMENSION);
		res.add(Connection.creeForm("Pseudo : ", this.anonyme));
		res.add(this.connectionAnonyme);
		return res;
	}

	private JPanel initInscriptionGroup() {
		JPanel res = Utile.creeGroup(INSCRIPTION_GROUP_NAME,
				INSCRIPTION_GROUP_DIMENSION);
		res.add(Connection.creeForm("Nom : ", this.nom));
		res.add(Connection.creeForm("Prenom : ", this.prenom));
		res.add(Connection.creeForm("Pseudo(*) : ", this.pseudoInscription));
		res.add(Connection.creeForm("mot de pass(*) : ",
				this.motDePassInscription));
		res.add(Connection.creeForm("confirmation(*) : ",
				this.confirmationMotDePass));
		res.add(this.inscription);
		return res;
	}

	private JPanel creeMessageServer() {
		JPanel res = Utile.creeGroup(MESSAGE_GROUP_NAME,
				MESSAGE_GROUP_DIMENSION);
		res.add(new JScrollPane(this.messageServer = Utile
				.initMessageServer(MESSAGE_SEREVER_DIMENSION)));
		messageServer.setText("veuillez vous connecter ou bien vous inscrire");
		return res;
	}

	private void initAction() {
		this.connectionMombe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (checkPasswd(motDePass.getText())
						&& checkPseudo(pseudo.getText()))
					new ConnectThread(
							distributeur,
							"mombreConnection",
							new Requete(
									new Object[][] {
											{
													RequestKeys.RAISON,
													RequestValue.CONNECTION_MOMBRE },
											{ RequestKeys.PSEUDO,
													pseudo.getText() },
											{ RequestKeys.PASSWD,
													motDePass.getText() } }));
			}
		});
		this.connectionAnonyme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (checkPseudo(anonyme.getText()))
					new ConnectThread(
							distributeur,
							"anonymeConnection",
							new Requete(new Object[][] {
									{ RequestKeys.RAISON,
											RequestValue.CONNECTION_ANONYME },
									{ RequestKeys.PSEUDO, anonyme.getText() } }));
			}
		});
		this.inscription.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checkPseudo(pseudoInscription.getText())
						&& checkPasswd(motDePassInscription.getText())
						&& checkConfirmationMotDePass())
					new ConnectThread(distributeur, "inscription", new Requete(
							new RequestKeys[] { RequestKeys.RAISON,
									RequestKeys.PSEUDO, RequestKeys.PASSWD,
									RequestKeys.NOM, RequestKeys.PRENOM },
							new Object[] { RequestValue.INSCRIPTION,
									pseudoInscription.getText(),
									motDePassInscription.getText(),
									nom.getText(), prenom.getText() }));
			}
		});
	}

	private boolean checkPseudo(String pseudo) {
		if (Pattern.compile(PSEUDO_REGEX).matcher(pseudo).matches())
			return true;
		this.printMessage("le pseudo n'est pas valide: il doit contenir entre 3 et 18 caracteres("
				+ PSEUDO_REGEX + ")");
		return false;
	}

	private boolean checkPasswd(String passwd) {
		if (Pattern.compile(PASSWD_REGEX).matcher(passwd).matches())
			return true;
		this.printMessage("le mot de pass n'est pas valide il doit contenir entre 6 et 22 caracteres("
				+ PASSWD_REGEX + ")");
		return false;
	}

	private boolean checkConfirmationMotDePass() {
		if (this.confirmationMotDePass.getText().equals(
				this.motDePassInscription.getText()))
			return true;
		this.printMessage("la confirmation du mot de pass ne corespend pas au mot de pass");
		return false;
	}

	public static JPanel creeForm(String labelText, JComponent field) {
		return Utile.creeForm(labelText, field, LABEL_DIM, TEXT_FILED_DIM,
				FORM_DIM);
	}

	public void printMessage(String message) {
		this.messageServer.setText(message);
	}

	class ConnectThread extends BasicThread {
		private Requete outRequest;

		public ConnectThread(Distributteur d, String name, Requete outRequete) {
			super(d);
			this.id = 1;
			this.out_id = 0;
			this.setName(name);
			this.outRequest = outRequete;
			this.start();
		}

		public void run() {
			out(outRequest);
			Requete repence = in();
			if (!this.isConnected())
				return;
			if (repence.getAttribut(RequestKeys.REPENCE)
					.equals(RequestValue.OK)) {
				new ChoixSalon((Long) repence.getAttribut(RequestKeys.OUT_ID),
						distributeur,
						(ClientBean) repence.getAttribut(RequestKeys.CLIENT));
				setVisible(false);
			} else
				printMessage((String) repence.getAttribut(RequestKeys.ERREUR));
		}

		public void start(Requete r) {
			this.outRequest = r;
		}
	}
}
