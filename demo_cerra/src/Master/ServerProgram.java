package Master;
import java.io.Serializable;

public interface ServerProgram extends Serializable{
	
	public Object run(Object parameters); 
	
	public int getId();
}
