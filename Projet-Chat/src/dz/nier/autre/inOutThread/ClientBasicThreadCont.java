package dz.nier.autre.inOutThread;

import java.util.ArrayList;
import java.util.Hashtable;

public class ClientBasicThreadCont {
	private Hashtable<Long, ArrayList<ClientBasicThread>> ClientBasicThreads = new Hashtable<Long, ArrayList<ClientBasicThread>>();
	private static volatile ClientBasicThreadCont single;

	private ClientBasicThreadCont() {

	}

	public Hashtable<Long, ArrayList<ClientBasicThread>> getClientBasicThreads() {
		return ClientBasicThreads;
	}

	public void setClientBasicThreads(
			Hashtable<Long, ArrayList<ClientBasicThread>> ClientBasicThreads) {
		this.ClientBasicThreads = ClientBasicThreads;
	}

	public void desconnectClient(Long clientId) {
		if (this.ClientBasicThreads.containsKey(clientId))
			for (int i = 0; i < this.ClientBasicThreads.get(clientId).size(); i++)
				this.ClientBasicThreads.get(clientId).get(i).desconnect();
	}

	public void addClientBasicThread(long clientId, ClientBasicThread bt) {
		if (!this.ClientBasicThreads.containsKey(clientId))
			this.ClientBasicThreads.put(clientId,
					new ArrayList<ClientBasicThread>());
		this.ClientBasicThreads.get(clientId).add(bt);
	}

	public void removeClientBasicThread(long clientId, ClientBasicThread bt) {
		if (this.ClientBasicThreads.containsKey(clientId))
			this.ClientBasicThreads.get(clientId).remove(bt);
	}

	public static ClientBasicThreadCont getInstance() {
		if (single == null) {
			synchronized (ClientBasicThreadCont.class) {
				single = new ClientBasicThreadCont();
			}
		}
		return single;
	}
}