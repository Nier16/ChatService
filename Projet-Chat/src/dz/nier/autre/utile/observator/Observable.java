package dz.nier.autre.utile.observator;


public interface Observable<T> {
	public void addObservateur(Observateur<T> observateur);

	public void deleteObservateur(Observateur<T> observatuer);

	public void updateObservateur(T arg);
}
