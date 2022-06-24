package Client;

import java.io.Serializable;

//interfaccia che serve a far conoscere al server come è strutturato un task

public interface Job extends Serializable {
	
	public Object run(Object parameters); 
	
	public int getId();
	
	public void setId(int id);
}