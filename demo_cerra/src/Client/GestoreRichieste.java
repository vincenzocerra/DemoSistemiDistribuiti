package Client;

import java.rmi.RemoteException;

import Master.MasterServer;

public class GestoreRichieste extends Thread {
	ClientTest c;
	private Job job;
	private MasterServer master;
	private int richiesteCount;
	boolean check;
	
	
	public GestoreRichieste(ClientTest c, MasterServer master, int richiesteCount) {
		check = true;
		this.c = c;
		this.master = master;
		this.richiesteCount=richiesteCount;
		job = new JavaProgram();
		
		
	}
	
	public void run()	{
		int counter=0;
		while (check) {
		try {
			int durataProgramma=(int)(Math.random() * (60000 - 10000) + 10000);
			master.startRequest(c,job,durataProgramma);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			int timeForNewRequest=(int)(Math.random() * (30000 - 2000) + 2000);
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
