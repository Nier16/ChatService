package dz.nier.client.connecitonServer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dz.nier.autre.utile.Utile;


public class ConnectionServer extends JFrame {
	public static final Dimension DIMENSION = new Dimension(300, 250);
	public static final Dimension CONNECTION_GROUP_DIMENSION = new Dimension(
			260, 140);
	public static final Dimension MESSAGE_GROUP_DIMENSION = new Dimension(260,
			65);
	public static final Dimension MESSAGE_DIMENSION = new Dimension(200, 30);
	public static final String NAME = "Connection Server";
	public static final String CONNECTION_GROUP_NAME = "Connection";
	public static final String MESSAGE_GROUP_NAME = "Message";
	public static final long WAIT_FOR_SERVER_CONNECTION_DELAY = 3000;
	public static final Integer DEFAULT_LOCAL_SERVER_PORT = 2009;
	public static final Integer MAX_PORT_VALUE = 65000;
	public static final Integer MIN_PORT_VALUE = 255;

	private JTextField serverAdress = new JTextField();
	private JTextField serverPort = new JTextField();
	private JButton connectionServer = new JButton("Connection");
	private JButton connectionServerLocal = new JButton("Server Local");
	private JFormattedTextField message = Utile
			.initMessageServer(MESSAGE_DIMENSION);

	public ConnectionServer() {
		this.setSize(DIMENSION);
		this.setName(NAME);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setTitle(NAME);
		this.setContentPane(init());
		this.setVisible(true);
	}

	private JPanel init() {
		JPanel cont = new JPanel();
		cont.setPreferredSize(DIMENSION);
		cont.add(initConnectionGroup());
		cont.add(initMessageGroup());
		initAction();
		return cont;
	}

	private JPanel initConnectionGroup() {
		JPanel res = Utile.creeGroup(CONNECTION_GROUP_NAME,
				CONNECTION_GROUP_DIMENSION);
		res.add(Connection.creeForm("adress du server", this.serverAdress));
		res.add(Connection.creeForm("port du server", this.serverPort));
		res.add(connectionServer);
		res.add(connectionServerLocal);
		return res;
	}

	private JPanel initMessageGroup() {
		JPanel res = Utile.creeGroup(MESSAGE_GROUP_NAME,
				MESSAGE_GROUP_DIMENSION);
		res.add(this.message);
		return res;
	}

	private void initAction() {
		this.connectionServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					new Connection(new Socket(serverAdress.getText(), Integer
							.parseInt(serverPort.getText())));
					out("connection au server réussite");
				} catch (NumberFormatException e) {
					out("le numero de port n'est pas valide");
				} catch (UnknownHostException e) {
					out("l'adress du server n'est pas valide");
				} catch (IOException e) {
					out("impossible de contacter le server");
				}

			}
		});
		this.connectionServerLocal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int port = DEFAULT_LOCAL_SERVER_PORT;
					if (checkPort())
						port = Integer.parseInt(serverPort.getText());
					new dz.nier.server.connectionClient.Connection(port);
					new Connection(new Socket(InetAddress.getLocalHost(), port));
					out("connection au server réussite avec le port(" + port
							+ ").");
				} catch (IOException e) {
					out("probleme lors de la connection au server");
					return;
				}
			}
		});
	}

	private boolean checkPort() {
		int port;
		try {
			port = Integer.parseInt(this.serverPort.getText());
		} catch (Exception e) {
			out("le port du server n'est pas valide(il ne doit contenir que des chifres.");
			return false;
		}
		if (port > MAX_PORT_VALUE || port < MIN_PORT_VALUE) {
			out("le port doit etre entre " + MIN_PORT_VALUE + " et "
					+ MAX_PORT_VALUE);
			return false;
		}
		return true;
	}

	public void out(String message) {
		this.message.setText(message);
	}

}
