package Master;

import java.rmi.RemoteException;
import java.util.ArrayList;
import Worker.WorkerServer;

public class WorkerScanner extends Thread {
	
	MasterImp master;
	 ArrayList<WorkerServer> workers;
	
	
	public WorkerScanner( MasterImp master) {
		this.master=master;
		this.workers=master.workers;
		
	}
	
	public void run() {
		boolean allon=true;
		while(allon) {
			if(workers.size()>0) {
				for(WorkerServer w: workers) {
					try {
						w.isOn();
					} catch (RemoteException e) {
						System.out.println("Un worker ha improvvisamente smesso di funzionare");
						try {
							master.handleDisconnection(w);
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//riassegnazione del job
					
					}
				}
			}
			try {
				this.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	

}
