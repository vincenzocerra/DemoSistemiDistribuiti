package Master;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;

public class SynchroListImp<T> implements SynchroList<T> {

private volatile Queue<T> queue;
	
	public SynchroListImp() throws RemoteException{
		// TODO Auto-generated constructor stub
		queue = new LinkedList<T>();
	}
	
	/**
	 * Use of lock-synchronization to keep consistency 
	 */
	
	@Override
	public synchronized void put(T t) throws RemoteException {
		// TODO Auto-generated method stub
		queue.offer(t);
	}

	@Override
	public synchronized T get() throws RemoteException {
		// TODO Auto-generated method stub
		return queue.poll();
	}

	@Override
	public synchronized int size() throws RemoteException {
		// TODO Auto-generated method stub
		return queue.size();
	}
	@Override
	public synchronized void remove(T t) throws RemoteException {
		// TODO Auto-generated method stub
		queue.remove(t);
	}
	@Override
	public synchronized T getFirst() throws RemoteException {
		// TODO Auto-generated method stub
		return queue.peek();
	}


}
