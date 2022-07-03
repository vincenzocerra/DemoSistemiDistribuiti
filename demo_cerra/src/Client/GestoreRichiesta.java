package Client;
import java.rmi.RemoteException;

import Master.MasterServer;

/**
 * Questa classe si occupa della creazione di un Thread lato Client. Ogni Thread ha il compito di invocare 
 * la funzione execClientApp/execServerApp presenti nell'interfaccia del master
 * @author VincenzoCerra
 *
 */

public class GestoreRichiesta extends Thread {
	private MasterServer master;
	private Client client;
	private int service;
	private int type;
	boolean check;
	private JavaProgram job;
	
	public GestoreRichiesta(Client client, MasterServer master, int service) {
		this.master=master;
		this.client=client;
		this.service=service;
		this.type=0;
	}
	
	public GestoreRichiesta(Client client, MasterServer master) {
		this.master=master;
		this.client=client;
		this.type=1;
		
	}
	
	/**
	 * Per utilizzare un unico Gestore che supportasse entrambe le 2 tipologie di richieste di esecuzione
	 * si è pensato di utilizzare un parametro type che varia in base al tipo di costruttore invocato.
	 */

	public void run() {
		if(type==0 ) {
			try {
				master.execServerApp(client,service,"parameters");
				System.out.println("C"+client.id+"->M request Server APP "+service);
			} catch (RemoteException e) {
				System.out.println("Il Master non e' piu' disponibile");
				System.exit(-1);

			}
		}
		else {
			try {
				job = new JavaProgram();
				master.execClientApp(client,job,"parameters");
				System.out.println("C"+client.id+"->M request Client APP "+job.id);
			} catch (RemoteException e) {
				System.out.println("Il Master non e' piu' disponibile");
				System.exit(-1);
			}	
		}
	}
	
	
}
