package Master;
import java.io.Serializable;

/**
 * Interfaccia Serializzabile che viene utilizzata per permettere al worker di
 * invocare dei metodi senza conoscerne l'effettiva implementazione
 * @author VincenzoCerra
 *
 */
public interface ServerProgram extends Serializable{
	
	/**
	 * Metodo accessibile al Worker che consente di eseguire una generica applicazione java senza
	 * conoscerne l'effettiva implementazione
	 * @param parameters parametri dell'applicazione
	 * @return Oggetto risultato
	 */
	public Object run(Object parameters); 
	
	/**
	 * Metodo per ottenere l'id del programma
	 * @return id del Programma
	 */
	public int getId();
}
