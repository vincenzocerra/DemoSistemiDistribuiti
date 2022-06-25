package Client;

public class JavaProgram implements Job {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id = 0;
	
	public Object run(Object parameters) {
		
		System.out.println("ESECUZIONE DI UNA GENERICA APPLICAZIONE JAVA");
		try {
			Thread.sleep((int) parameters);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (parameters);
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