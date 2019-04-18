package dz.nier.autre.inOutThread;

import dz.nier.autre.request.RequestKeys;
import dz.nier.autre.request.RequestValue;
import dz.nier.autre.request.Requete;

public class BasicThread extends Thread {
	public static long ID = 5;
	protected Distributteur distributeur;
	protected long id = BasicThread.ID++;
	protected long out_id = 0;
	protected Requete inRequete;
	protected boolean wait = false;
	protected boolean connected = true;

	public BasicThread(Distributteur d) {
		this.distributeur = d;
	}

	public BasicThread(Distributteur d, long outId) {
		this.distributeur = d;
		this.out_id = outId;
		out(new Requete(RequestKeys.OUT_ID, this.id));
	}

	public Requete in() {
		this.wait = true;
		this.distributeur.addWaitter(this);
		synchronized (this) {
			try {
				if (this.wait)
					this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Requete res = inRequete;
		inRequete = null;
		return res;
	}

	public void stopWait(Requete r) {
		this.inRequete = r;
		this.wait = false;
		synchronized (this) {
			this.notify();
		}
	}

	public void out(Requete requete) {
		this.distributeur.sendRequest(this.out_id, requete);
	}

	public long getId() {
		return this.id;
	}

	public long setOutId() {
		return this.out_id;
	}

	public void setOutId(long id) {
		this.out_id = id;
	}

	public Distributteur getDistributteur() {
		return this.distributeur;
	}

	public long getOutId() {
		return this.out_id;
	}

	protected boolean nonConnecter(Requete r) {
		r.ajouterAttribut(RequestKeys.REPENCE, RequestValue.ERREUR);
		out(r);
		return false;
	}

	public void desconnect() {
		this.connected = false;
		this.stopWait(null);
	}

	public void start() {
		this.connected = true;
		super.start();
	}

	public boolean isConnected() {
		return this.connected;
	}

}
