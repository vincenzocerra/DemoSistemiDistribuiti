package Client;

public class JavaProgram implements Job {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id = 0;
	
	public Object run(Object parameters) {
		
		return (99);
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