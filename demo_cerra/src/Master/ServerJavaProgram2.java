package Master;

public class ServerJavaProgram2 implements ServerProgram{

	private static final long serialVersionUID = 1L;
	int id = 2;
int durataProgramma = 40000;
	
	public ServerJavaProgram2() {

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