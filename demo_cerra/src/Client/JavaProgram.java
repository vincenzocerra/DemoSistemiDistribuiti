package Client;

public class JavaProgram implements Job {

	private static final long serialVersionUID = 1L;
	int id = 0;
	private int maxDuration= 60000;
	private int minDuration= 10000;
	int durataProgramma;
	
	public JavaProgram() {
		durataProgramma=(int)(Math.random() * (maxDuration - minDuration) + minDuration);

	}
	
	public Object run(Object parameters) {
		
		try {
			Thread.sleep(durataProgramma);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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