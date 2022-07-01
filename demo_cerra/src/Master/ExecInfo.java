package Master;
import Client.Job;
import Client.ServerCallback;


/**
 * Classe di servizio utilizzata solo dal MasterServer per poter memorizzare in un'unica istanza tutte le informazioni 
 * relative ad una richiesta di esecuzione ricevuta dal client. Nello specifico contiene il riferimento al Client che ha richiesto l'esecuzione, 
 * all'applicazione java da eseguire e ai parametri di quest'ultima.
 * 
 * @author VincenzoCerra
 *
 */
public class ExecInfo {
	
	

	public ServerCallback sc;
	public Job j;
	public Object parameters;

	public ExecInfo(ServerCallback sc, Job j, Object parameters) {
		this.sc=sc;
		this.j=j;
		this.parameters=parameters;		
	}
	/**
	 * Metodo di servizio per ottere il riferimento al Client
	 * @return	riferimento al Client
	 */

	public ServerCallback getSc() {
		return sc;
	}
	/**
	 * Metodo di servizione per la modifica del riferimento al Client
	 * @param sc	riferimento al client
	 */

	public void setSc(ServerCallback sc) {
		this.sc = sc;
	}
	/**
	 * Metodo di servizio per ottenere l'applicazione associata all'oggetto
	 * @return	applicazione Java
	 */

	public Job getJ() {
		return j;
	}
	/**
	 * Metodo di servizio per la motidica dell'applicazione
	 * @param j
	 */

	public void setJ(Job j) {
		this.j = j;
	}
	
	/**
	 * Metodo di servizio per ottenere i paramentri dell'oggetto
	 * @param parameters parametri dell'oggetto
	 */

	public Object getParameters() {
		return parameters;
	}
	
	/**
	 * Metodo di servizio per la motidica dei paramentri
	 * @param parameters
	 */

	public void setParameters(Object parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Metodo per la stampa dell'oggetto
	 */
	
	@Override
	public String toString() {
		return "ExecInfo [Client=" + sc + ", App=" + j + ", parameters=" + parameters + "]";
	}
	

}
