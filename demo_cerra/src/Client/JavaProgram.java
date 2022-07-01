package Client;

/**
 * Classe che implementa l'interfaccia Job ed è pensata per simulare una generica applicazione java che il client vuole far eseguire al master
 * La durata dell'esecuzione dell'applicazione è simulata tramite una Thread.sleep() che prende in ingresso un valore random tra 10 secondi e 1 minuto
 * Per semplicità implementativa restituisce come risultato la durata del programma.
 * 
 * @author VincenzoCerra
 *
 */

public class JavaProgram implements ClientApp {

	private static final long serialVersionUID = 1L;
	int id = 0;
	private int maxDuration= 60000;
	private int minDuration= 10000;
	int durataProgramma;
	
	public JavaProgram() {
		durataProgramma=(int)(Math.random() * (maxDuration - minDuration) + minDuration);

	}
	
	@Override
	public Object run(Object parameters) {
		
		try {
			Thread.sleep(durataProgramma);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return (durataProgramma);
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}
}