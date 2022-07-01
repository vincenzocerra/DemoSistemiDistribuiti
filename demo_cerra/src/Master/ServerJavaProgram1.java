package Master;

public class ServerJavaProgram1 implements ServerProgram{

	private static final long serialVersionUID = 1L;
	int id = 1;
	
	int durataProgramma = 10000;
	
	public ServerJavaProgram1() {

	}
	
	@Override
	public Object run(Object parameters) {
		
		try {
			Thread.sleep(durataProgramma);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return ("risultato ServerAPP "+id);
	}


	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}