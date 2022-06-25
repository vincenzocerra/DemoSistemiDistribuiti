package Master;

import Client.ServerCallback;
import Worker.WorkerServer;

public class MasterThread extends Thread {
	
	private ServerCallback client;
	private WorkerServer w;

	
	MasterThread(ServerCallback client, WorkerServer w){
		this.client = client;
		this.w = w;
	}
	
	public void run()	{	
   		while (true) {
			try {
				client.getResult(99);
				Thread.sleep(10000);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}//while
	}
}
	
