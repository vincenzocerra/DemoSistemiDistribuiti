package Client;

import java.util.Vector;

public class JavaProgram implements Job {
	int id = 0;
	
	public Object run(Object parameters) {
		
		return new Integer(99);
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