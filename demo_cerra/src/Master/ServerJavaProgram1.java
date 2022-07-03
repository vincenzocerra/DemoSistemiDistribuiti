package Master;

/**
 * Classe che implementa l'interfaccia ServerProgram ed è pensata per simulare una generica applicazione java che il Master propone come servizio.
 * La durata dell'esecuzione dell'applicazione è simulata tramite una Thread.sleep() che prende in ingresso un valore random tra 20 e 40 secondi.
 * Per semplicità implementativa restituisce come risultato la durata del programma non considerando i parametri di ingresso.
 * @author VincenzoCerra
 *
 */

public class ServerJavaProgram1 implements ServerProgram{

	private static final long serialVersionUID = 1L;
	int id = 1;
	private int maxDuration= 40000;
	private int minDuration= 20000;
	int durataProgramma;

	public ServerJavaProgram1() {
	}
	
	@Override
	public Object run(Object parameters) {
		durataProgramma=(int)(Math.random() * (maxDuration - minDuration) + minDuration);
		try {
			Thread.sleep(durataProgramma);
		} catch (InterruptedException e) {
		}
		
		return ("risultato ServerAPP1 "+durataProgramma);
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}