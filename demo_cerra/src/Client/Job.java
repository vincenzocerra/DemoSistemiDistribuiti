package Client;

import java.io.Serializable;

/**
 * Interfaccia Serializzabile che viene utilizzata per permettere al master e ai worker di
 * invocare dei metodi senza conoscerne l'effettiva implementazione
 * 
 * @author VincenzoCerra
 *
 */

public interface Job extends Serializable {
	
	/**
	 * Metodo accessibile dal Master e dai Worker che consente di eseguire una generica applicazione java senza
	 * conoscerne l'effettiva implementazione
	 * @param parameters un Object che verrà poi castizzato all'interno dell'implementazione
	 * @return Un Object che verrà castizzato all'interno dell'implementazione
	 */
	
	public Object run(Object parameters); 
	
	/**
	 *  Metodo accessibile dal Master e dai Worker che consente di ottenere l'id dell'applicazione
	 * @return id dell'applicazione
	 */
	
	public int getId();
	
	/**
	 * Metodo accessibile dal Master e dai Worker che consente di settare l'id dell'applicazione
	 * @param id
	 */
	
	public void setId(int id);
}