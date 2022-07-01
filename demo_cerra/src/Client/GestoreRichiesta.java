package Client;
import java.rmi.RemoteException;

import Master.MasterServer;



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

	public void run() {
		if(type==0 ) {
			try {
				master.startRequest2(client,service,"parameters");
				System.out.println("C"+client.id+"->M request Server APP "+service);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			try {
				job = new JavaProgram();
				master.startRequest(client,job,"parameters");
				System.out.println("C"+client.id+"->M request Client APP "+job.id);
			} catch (RemoteException e) {
				e.printStackTrace();
			}	
		}
	}
	
	
}
