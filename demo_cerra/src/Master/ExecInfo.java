package Master;
import Client.Job;
import Client.ServerCallback;

public class ExecInfo {
	
	public ServerCallback sc;
	public Job j;
	public Object parameters;

	public ExecInfo(ServerCallback sc, Job j, Object parameters) {
		this.sc=sc;
		this.j=j;
		this.parameters=parameters;		
	}

	public ServerCallback getSc() {
		return sc;
	}

	public void setSc(ServerCallback sc) {
		this.sc = sc;
	}

	public Job getJ() {
		return j;
	}

	public void setJ(Job j) {
		this.j = j;
	}

	public Object getParameters() {
		return parameters;
	}

	public void setParameters(Object parameters) {
		this.parameters = parameters;
	}
	
	

}
