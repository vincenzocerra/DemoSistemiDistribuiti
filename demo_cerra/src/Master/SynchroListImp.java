package Master;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Classe ausiliaria accessibile dal Master che serve a garantire sia una esecuzione FIFO delle applicazioni sia la consistenza in caso di accesso simultaneo sulle liste
 * tramite l'utilizzo di una Queue Volatile e dell'implementazione dei metodi synchronized
 * @author VincenzoCerra
 *
 * @param <T>
 */

public class SynchroListImp<T>{

private volatile Queue<T> queue;
	
	public SynchroListImp() {
		queue = new LinkedList<T>();
	}

/**
 * Medoto synchronized che Inserisce in coda alla lista l'oggetto in ingresso
 * @param t oggetto da inserire in coda
 */
	public synchronized void put(T t){
		queue.offer(t);
	}
/**
 * Medoto synchronized che preleva dalla lista l'oggetto in testa
 * @return oggetto in testa alla lista
 */
	public synchronized T get(){
		return queue.poll();
	}
/**
 * Medoto synchronized che restituisce la dimensione della lista
 * @return	dimensione della lista
 */
	public synchronized int size(){
		return queue.size();
	}
/**
 * Medoto synchronized che rimuove dalla lista l'oggetto in ingresso
 * @param t oggetto da rimuovere
 */
	public synchronized void remove(T t){
		queue.remove(t);
	}
	
/**
 * Medoto synchronized che restituisce l'oggetto in testa senza rimuoverlo dalla lista
 * @return oggetto in testa alla lista
 */
	public synchronized T getFirst(){
		return queue.peek();
	}


}
