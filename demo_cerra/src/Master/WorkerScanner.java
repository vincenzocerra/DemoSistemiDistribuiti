package Master;

import java.rmi.RemoteException;
import Worker.WorkerServer;

public class WorkerScanner extends Thread {
	
	WorkerServer w;
	MasterImp m;
	int wId;
	
	
	public WorkerScanner(WorkerServer w, int wId, MasterImp master){
		this.w=w;
		this.m=master;
		this.wId= wId;
		
	}
	
	public void run() {
		boolean online=true;
		while(online) {
			try {
				w.isOn();
			} catch (RemoteException e) {
				System.out.println("Master: Il Worker "+wId+" ha improvvisamente smesso di funzionare");
				try {
					m.handleBadDisconnection(w);
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
