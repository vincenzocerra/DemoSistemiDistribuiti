package Master;

/**
 * Classe che implementa l'interfaccia ServerProgram ed √® pensata per simulare una generica applicazione java che il Master propone come servizio.
 * La durata dell'esecuzione dell'applicazione √® simulata tramite una Thread.sleep() che prende in ingresso un valore random tra 10 e 20 secondi.
 * Per semplicit√† implementativa restituisce come risultato la durata del programma non considerando i parametri di ingresso.
 * @author VincenzoCerra
 *
 */

public class ServerJavaProgram0 implements ServerProgram{

	private static final long serialVersionUID = 1L;
	int id = 0;
	private int maxDuration= 20000;
	private int minDuration= 10000;
	int durataProgramma;

	public ServerJavaProgram0() {
	}
	
	@Override
	public Object run(Object parameters) {
		durataProgramma=(int)(Math.random() * (maxDuration - minDuration) + minDuration);
		try {
			Thread.sleep(durataProgramma);
		} catch (InterruptedException e) {
		}
		
		return ("risultato ServerAPP "+durataProgramma);
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}