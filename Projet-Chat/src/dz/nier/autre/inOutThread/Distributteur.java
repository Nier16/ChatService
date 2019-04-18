package dz.nier.autre.inOutThread;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Hashtable;

import dz.nier.autre.request.RequestKeys;
import dz.nier.autre.request.RequestValue;
import dz.nier.autre.request.Requete;
import dz.nier.autre.utile.Utile;

public class Distributteur extends Thread {
	private Hashtable<Long, Requete> inRequest = new Hashtable<Long, Requete>();
	private Hashtable<Long, BasicThread> waitter = new Hashtable<Long, BasicThread>();

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private boolean run = false;

	public Distributteur(ObjectInputStream ois, ObjectOutputStream oos) {
		this.ois = ois;
		this.oos = oos;
		this.start();
	}

	public Distributteur(Socket s) {
		this.oos = Utile.creeOOS(s);
		this.ois = Utile.creeOIS(s);
		this.start();
	}

	public void run() {
		Requete r;
		this.setName("Destributteur");
		if (this.run == true)
			return;
		this.run = true;
		while (run) {
			try {
				r = (Requete) ois.readObject();
				addInRequest(r);
			} catch (Exception e) {
				this.finir();
				return;
			}
		}
	}

	public void finir() {
		sendRequest((long) -1, new Requete(RequestKeys.RAISON,
				RequestValue.DESCONNECT_DISTRIBUTTEUR));
		clearAll();
	}

	public void clearAll() {
		this.run = false;
		this.inRequest.clear();
		this.waitter.clear();
	}

	public void desconnect() {
		this.run = false;
	}

	private void addInRequest(Requete r) {
		long id = (long) r.getAttribut(RequestKeys.DISTRIBUTEUR_OUT_ID);
		if (id == -1) {
			clearAll();
			return;
		}
		if (waitter.containsKey(id)) {
			waitter.get(id).stopWait(r);
			waitter.remove(id);
		} else
			inRequest.put(id, r);
	}

	public void addWaitter(BasicThread b) {
		if (inRequest.containsKey(b.getId()))
			b.stopWait(inRequest.remove(b.getId()));
		else
			waitter.put(b.getId(), b);
	}

	public void sendRequest(Long id, Requete requete) {
		requete.ajouterAttribut(RequestKeys.DISTRIBUTEUR_OUT_ID, id);
		try {
			oos.writeObject(requete);
			oos.reset();
		} catch (Exception e) {
			clearAll();
			return;
		}
	}

	public boolean isBasicThreadWaitting(long basicThreadOutId) {
		return this.waitter.containsKey(basicThreadOutId);
	}

	public boolean haveRequestForMe(long basicThreadId) {
		return this.inRequest.containsKey(basicThreadId);
	}
}
