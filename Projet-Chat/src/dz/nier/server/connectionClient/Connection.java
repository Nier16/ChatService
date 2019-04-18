package dz.nier.server.connectionClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import dz.nier.autre.inOutThread.Distributteur;
import dz.nier.server.salon.HostSalon;
import dz.nier.server.utile.ServerOut;


public class Connection extends Thread {
	public static boolean connect = true;
	public final Integer port;

	public Connection(int port) {
		this.port = port;
		start();
	}

	public void run() {
		try {
			ServerOut.init();
			HostSalon.getInstance();
			this.setName("Connection Server");
			ServerSocket ss = new ServerSocket(port);
			ServerOut.out("le server(" + ss
					+ ") est connécté et a l'ecoute du port "
					+ ss.getLocalPort());
			Socket socket;
			while (ServerOut.isOpen()) {
				socket = ss.accept();
				ServerOut.out("le client : " + socket
						+ " s'est connecté au server");
				new Acceptation(new Distributteur(socket));
			}
			ss.close();
			ServerOut.out("le server est déconnécté");
		} catch (IOException e) {
			ServerOut.out(e.getMessage());
		}

	}
}
