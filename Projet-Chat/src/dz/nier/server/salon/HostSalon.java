package dz.nier.server.salon;

import java.util.ArrayList;
import java.util.Hashtable;

import dz.nier.autre.bean.ClientBean;
import dz.nier.autre.bean.SalonBean;
import dz.nier.autre.inOutThread.Distributteur;
import dz.nier.autre.request.RequestKeys;
import dz.nier.autre.request.Requete;
import dz.nier.server.bdd.ConnectionBDD;
import dz.nier.server.bdd.dao.SalonBeanDAO;
import dz.nier.server.utile.ServerOut;


public class HostSalon extends Thread {
	private Hashtable<Long, Salon> salons = new Hashtable<Long, Salon>();
	private ArrayList<SalonBean> publicSalon = new ArrayList<SalonBean>();
	private ArrayList<SalonBean> privateSalon = new ArrayList<SalonBean>();
	private ArrayList<SalonBean> mombres = new ArrayList<SalonBean>();
	private volatile static HostSalon single;

	private HostSalon() {
		this.setName("Host salon");
		this.start();
	}

	public static HostSalon getInstance() {
		if (single == null) {
			synchronized (ConnectionBDD.class) {
				if (single == null)
					single = new HostSalon();
			}
		}
		return single;
	}

	public Hashtable<Long, Salon> getSalons() {
		return salons;
	}

	public void setSalons(Hashtable<Long, Salon> salons) {
		this.salons = salons;
	}

	public void initSalons() {

	}

	public void run() {
		ServerOut.out("chargement de la list des salons depuis la BDD");
		long debut = System.currentTimeMillis();
		ArrayList<SalonBean> allSalonBean = new SalonBeanDAO().selectAll();
		for (SalonBean e : allSalonBean) {
			if (e.getType().equals(SalonBean.Type.PUBLIC))
				publicSalon.add(e);
			else if (e.getType().equals(SalonBean.Type.PRIVE))
				privateSalon.add(e);
		}
		ServerOut
				.out("le chargement de la liste des salons s'est correctemet déroulé("
						+ (System.currentTimeMillis() - debut) + "ms)");
	}

	public Long addClientToSalon(ClientBean client, SalonBean salon, Distributteur d) {
		ServerOut.out("le client" + client.toSimpleString()
				+ " s'est connecté au salon" + salon.toSimpleString());
		if (this.salons.containsKey(salon.getId()))
			return this.salons.get(salon.getId()).addClient(client, d);
		Salon salonThread = new Salon(salon);
		this.salons.put(salon.getId(), salonThread);
		return salonThread.addClient(client, d);
	}

	public void addClient(ClientBean client) {
		SalonBean salon = new SalonBeanDAO().insertObj(new SalonBean(client));
		ServerOut.out("le salon" + salon.toSimpleString()
				+ " a été crée pour le client" + client.toSimpleString());
		this.mombres.add(salon);
	}

	public Requete getAllSalon() {
		return new Requete(new Object[][] {
				{ RequestKeys.SALON_PUBLIC, this.publicSalon },
				{ RequestKeys.SALON_PRIVEE, this.privateSalon },
				{ RequestKeys.MOMBRES_EN_LIGNES, this.mombres } });
	}

	public boolean deleteSalon(SalonBean salon) {
		Salon sbt = this.salons.remove(salon.getId());
		if (sbt != null)
			sbt.close();
		if (!this.publicSalon.remove(salon))
			if (!this.privateSalon.remove(salon))
				if (!this.mombres.remove(salon))
					return false;
		ServerOut.out("le salon" + salon.toSimpleString() + " a été suprimé");
		new SalonBeanDAO().delete(salon.getId());
		return true;
	}

	public boolean findSalon(SalonBean salon) {
		if (this.salons.containsKey(salon.getId())
				|| this.publicSalon.contains(salon)
				|| this.privateSalon.contains(salon)
				|| this.mombres.contains(salon))
			return true;
		return false;
	}

	public Salon addSalon(SalonBean salonbean) {
		Salon s = new Salon(new SalonBeanDAO().insertObj(salonbean));
		this.salons.put(s.getSalon().getId(), s);
		if (s.getSalon().getType().equals(SalonBean.Type.PRIVE))
			this.privateSalon.add(s.getSalon());
		else
			this.publicSalon.add(s.getSalon());
		ServerOut.out("le salon" + s.getSalon().toSimpleString()
				+ " a été crée");
		return s;
	}
}
