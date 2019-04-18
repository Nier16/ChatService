package dz.nier.autre.utile.observator;

import java.util.ArrayList;

public class ObservableClass<T> implements Observable<T> {

	private ArrayList<Observateur<T>> observateurs = new ArrayList<Observateur<T>>();

	public ObservableClass(Observateur<T> observateur) {
		this.addObservateur(observateur);
	}

	public void addObservateur(Observateur<T> observateur) {
		this.observateurs.add(observateur);
	}

	public void deleteObservateur(Observateur<T> observateur) {
		this.observateurs.remove(observateur);
	}

	public void updateObservateur(T arg) {
		for (Observateur<T> e : this.observateurs)
			e.update(arg);
	}

	public void updateObservateur(Observateur<T> observateur, T arg) {
		observateur.update(arg);
	}

}
