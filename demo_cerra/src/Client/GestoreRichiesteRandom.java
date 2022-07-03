package Client;

import java.rmi.RemoteException;
import java.util.Random;

import Master.MasterServer;

/**
 * Questa classe si occupa della creazione di un Thread lato Client che ha il compito di inoltrare la richiesta
 * di esecuzione dell'applicazione al Master. In caso di richieste multiple garantisce casualità nei tempi di invio
 * attraverso l'implementazione di una sleep randomica tra 2 e 30 secondi.
 * 
 * @author VincenzoCerra
 *
 */
public class GestoreRichiesteRandom extends Thread {
	Client c;
	private ClientApp job;
	private MasterServer master;
	private int richiesteCount;
	boolean check;
	private int maxSleep= 30000;
	private int minSleep= 2000;
	private Random random;
	
	
	public GestoreRichiesteRandom(Client c, MasterServer master, int richiesteCount) {
		check = true;
		this.c = c;
		this.master = master;
		this.richiesteCount=richiesteCount;
		random= new Random();
	}
	
	public void run()	{
		int counter=0;
		while (check) {
			if(random.nextBoolean()) {
				try {
					job = new JavaProgram();
					job.setId(counter);
					master.execClientApp(c,job,"parameters");
					System.out.println("C"+c.id+"->M request Client APP "+job.getId());
				} catch (RemoteException e) {
					System.out.println("Il Master non e' piu' disponibile");
					System.exit(-1);
				}
			}
			else {
				int service=(int)(Math.random() * (3 - 0) + 0);
				try {
					master.execServerApp(c,service,"parameters");
					System.out.println("C"+c.id+"->M request Server APP "+service);
				} catch (RemoteException e) {
					System.out.println("Il Master non e' piu' disponibile");
					System.exit(-1);
				}
			}
			counter++;
			if(counter == richiesteCount)check=false;
			else {
				try {
					int timeForNewRequest=(int)(Math.random() * (maxSleep - minSleep) + minSleep);
					Thread.sleep(timeForNewRequest);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
