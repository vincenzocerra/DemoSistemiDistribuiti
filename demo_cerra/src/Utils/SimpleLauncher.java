package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;

import Client.ClientTest;
import Master.MasterImp;
import Worker.WorkerImp;

public class SimpleLauncher {
	
	private static final String localHost = "127.0.0.1";
	static int startingPort = 4000;

    public static void main(String[] args) throws NotBoundException {
    	boolean running = true;
    	int masterNumber = 1 ;
    	HashMap<Integer,Integer> workerNumber = new HashMap<Integer,Integer>();
    	String line;
    	System.out.println("CONSOLE");
		System.out.println("Questo Launcher e' stato Creato per facilitare l'esecuzione di Master e Worker e Client");
		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
	
		while(running) {
			try {
				System.out.println("Selezionare il numero ragionevole di Master da eseguire >");
				boolean ok = false;
				while (ok != true) {
					try {
						int g = Integer.parseInt(bReader.readLine());
						if (g>0)masterNumber = g;
					}catch(NumberFormatException e) {
						System.out.println("Bisogna inserire un numero!");
					}
					ok = true;	
				}// while
				
				for(int i = 0; i< masterNumber ; i++) {
					System.out.println("Selezionare il numero ragionevole di Worker da eseguire sul master "+i+" >");
					boolean ok2 = false;
					while (ok2 != true) {
						try {
							int g = Integer.parseInt(bReader.readLine());
							if (g>0)workerNumber.put(i,g);
						}catch(NumberFormatException e) {
							System.out.println("Bisogna inserire un numero!");
						}
						ok2 = true;	
					}// while	
				}
				for(int m = 0; m < masterNumber ; m++) {
					try {
						new MasterImp(startingPort+m);
						int all = workerNumber.get(m);
						for(int w = 0; w < all ; w++) {
							try {
								new WorkerImp(localHost, startingPort+m );
					        } catch (RemoteException e) {
					            System.err.println(e);   
							}
						}
							
			        } catch (RemoteException e) {
			            System.err.println(e);   
				}
					
				}
				
				running=false;
	
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
        
    }

}
