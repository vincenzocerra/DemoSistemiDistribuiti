package Master;

public class ServerJavaProgram0 implements ServerProgram{

	private static final long serialVersionUID = 1L;
	int id = 0;
	int durataProgramma = 4000;
	
	public ServerJavaProgram0() {

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