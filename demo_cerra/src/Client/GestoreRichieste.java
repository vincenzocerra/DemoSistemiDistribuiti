package Client;

import java.rmi.RemoteException;

import Master.MasterServer;

public class GestoreRichieste extends Thread {
	ClientTest c;
	private Job job;
	private MasterServer master;
	private int richiesteCount;
	boolean check;
	private int maxSleep= 30000;
	private int minSleep= 2000;
	
	
	public GestoreRichieste(ClientTest c, MasterServer master, int richiesteCount) {
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
			System.out.println("Client "+c.id+": ho richiesto l'esecuzione dell'app "+job.getId()+" al Master");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			int timeForNewRequest=(int)(Math.random() * (maxSleep - minSleep) + minSleep);
			Thread.sleep(timeForNewRequest);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		counter++;
		if(counter == richiesteCount)check=false;
		}
		
	}

}