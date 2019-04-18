package dz.nier.autre.request;

import java.io.Serializable;
import java.util.Hashtable;

public class Requete implements Serializable {
	private static final long serialVersionUID = 1L;
	private Hashtable<RequestKeys, Object> requete = new Hashtable<RequestKeys, Object>();

	public Requete() {

	}

	public Requete(RequestKeys clee, Object valeur) {
		this.ajouterAttribut(clee, valeur);
	}

	public Requete(RequestKeys[] keys, Object[] values) {
		this.ajouterAttributs(keys, values);
	}

	public Requete(Object[][] arg) {
		this.ajouterAttributs(arg);
	}

	public void ajouterAttribut(RequestKeys clee, Object valeur) {
		this.requete.put(clee, valeur);
	}

	public Object getAttribut(RequestKeys raison) {
		return this.requete.get(raison);
	}

	public boolean contain(RequestKeys key) {
		return this.requete.containsKey(key);
	}

	public void ajouterAttributs(Object[][] arg) {
		for (Object[] e : arg) {
			this.ajouterAttribut((RequestKeys) e[0], e[1]);
		}
	}

	public void ajouterAttributs(RequestKeys[] keys, Object[] objects) {
		for (int i = 0; i < keys.length; i++)
			this.ajouterAttribut(keys[i], objects[i]);
	}
}
