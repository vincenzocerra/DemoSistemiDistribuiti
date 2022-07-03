package Master;

import java.util.concurrent.BlockingQueue;
import Client.ServerCallback;
import Worker.WorkerServer;

/**
 * Questa classe si occupa della creazione di un Thread lato Server. Ogni Thread ha il compito di associare un'applicazione
 * ad un worker richiedendone l'esecuzione rispettando un ordine FIFO e gestendo eventuali problemi di comunicazione.
 * @author VincenzoCerra
 *
 */

public class MasterThread extends Thread {
	
	private ServerCallback client;
	private Object j;
	private Object parameters;
	private BlockingQueue<WorkerServer> availableWorker;
	private WorkerServer w;
	
	private ExecInfo info;
	private MasterImp master;
	
	MasterThread(ExecInfo info, MasterImp master){
		this.info=info;
		this.client = info.getSc();
		this.j =info.getJ();
		this.master=master;
		this.parameters=info.getParameters();
		this.availableWorker = master.availableWorkers2;		
	}
	
	/**
	 * Il metodo utilizza la lista availableWorker mettendo in attesa del proprio turno attraverso
	 * l'istruzione take(). Tale ascolto non è un busy waiting in quanto non viene utilizzata la cpu,
	 * il thread infatti è "addormentato" e verrà svegliato direttamente dalla lista quando sarà il suo turno.
	 */
	
	public void run()	{	
		try {
			w =availableWorker.take();
			master.inEsecuzione.put(w, info);
			try {
			System.out.println("M->W"+w.getId()+" execute app ");
			w.execute(client, j, parameters, info.getType());
			}catch(Exception e){
				master.handleBadDisconnection(w);
			}
			} catch (Exception e) {
			}
	}
}
