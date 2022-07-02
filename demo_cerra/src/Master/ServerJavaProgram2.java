package Master;

/**
 * Classe che implementa l'interfaccia ServerProgram ed è pensata per simulare una generica applicazione java che il Master propone come servizio.
 * La durata dell'esecuzione dell'applicazione è simulata tramite una Thread.sleep() che prende in ingresso un valore random tra 40 e 80 secondi.
 * Per semplicità implementativa restituisce come risultato la durata del programma non considerando i parametri di ingresso.
 * @author VincenzoCerra
 *
 */

public class ServerJavaProgram2 implements ServerProgram{

	private static final long serialVersionUID = 1L;
	int id = 0;
	private int maxDuration= 80000;
	private int minDuration= 40000;
	int durataProgramma;

	public ServerJavaProgram2() {
		durataProgramma=(int)(Math.random() * (maxDuration - minDuration) + minDuration);
	}
	
	@Override
	public Object run(Object parameters) {
		
		try {
			Thread.sleep(durataProgramma);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return ("risultato ServerAPP2 "+durataProgramma);
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}