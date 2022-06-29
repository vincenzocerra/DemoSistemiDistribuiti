package Master;

import java.rmi.RemoteException;
import Worker.WorkerServer;

public class WorkerScanner extends Thread {
	
	WorkerServer w;
	MasterImp m;
	int id;
	
	
	public WorkerScanner(WorkerServer w, MasterImp master) throws RemoteException {
		this.w=w;
		this.m=master;
		this.id = w.getId();
		
	}
	
	public void run() {
		boolean online=true;
		while(online) {
			try {
				w.isOn();
			} catch (RemoteException e) {
				System.out.println("Master: Il Worker "+id+" ha improvvisamente smesso di funzionare");
				try {
					m.handleDisconnection(w);
					online=false;
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	

}
