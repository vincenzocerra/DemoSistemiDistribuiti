package Client;

import java.io.Serializable;

public interface Job extends Serializable {
	public Object run(Object parameters);
	public int getId();
	public void setId(int id);
}