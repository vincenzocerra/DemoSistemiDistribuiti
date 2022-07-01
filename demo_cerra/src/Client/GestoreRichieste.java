package Client;

import java.rmi.RemoteException;
import Master.MasterServer;

/**
 * Questa classe si occupa della creazione di un Thread lato Client che ha il compito di inoltrare la richiesta
 * di esecuzione dell'applicazione al Master. In caso di richieste multiple garantisce casualità nei tempi di invio
 * attraverso l'implementazione di una sleep randomica tra 2 e 30 secondi.
 * 
 * @author VincenzoCerra
 *
 */
public class GestoreRichieste extends Thread {
	Client c;
	private ClientApp job;
	private MasterServer master;
	private int richiesteCount;
	boolean check;
	private int maxSleep= 30000;
	private int minSleep= 2000;
	
	
	public GestoreRichieste(Client c, MasterServer master, int richiesteCount) {
		check = true;
		this.c = c;
		this.master = master;
		this.richiesteCount=richiesteCount;
	}
	
	public void run()	{
		int counter=0;
		while (check) {
		try {
			job = new JavaProgram();
			job.setId(counter);
			master.startRequest(c,job,1);
			System.out.println("C"+c.id+"->M request "+job.getId());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		try {
			int timeForNewRequest=(int)(Math.random() * (maxSleep - minSleep) + minSleep);
			Thread.sleep(timeForNewRequest);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		counter++;
		if(counter == richiesteCount)check=false;
		}
		
	}

}
