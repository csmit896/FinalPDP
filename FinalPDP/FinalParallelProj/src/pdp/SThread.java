package pdp;
import java.io.*;
import java.net.*;
import java.lang.Exception;


public class SThread extends Thread
{
    private Object [][] RTable; // routing table
    private BufferedReader in; // reader (for reading from the machine connected to)
    private int ind; // indext in the routing table

    // Constructor
    SThread(Object [][] Table, Socket toClient, int index) throws IOException
    {
        in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
        RTable = Table;
        ind = index;
    }

    // Run method (will run for each machine that connects to the ServerRouter)
    @Override
    public void run()
    {
    	
    	String Coordinator = null;
    	
    	do {

        try
        {
        	Coordinator = in.readLine();
        } catch (IOException e) {
        	System.err.println("Error1!");
        	return;
        }
        if(Coordinator == null) {
        	System.exit(1);
        }
        
        switch (Coordinator) {
        
        
        case "AddMe": {
        	
        	String GivenIP = "";
        	String GivenPort = "";
        	try {
        		GivenIP = in.readLine();
        	} catch (IOException e) {
            	System.err.println("Error2!");
            	System.exit(1);
            }
        	
        	String ClientSplitter[] = GivenIP.split("P", 0);
        	
        	GivenIP = ClientSplitter[0];
        	GivenPort = ClientSplitter[1];
        	
        	RTable[ind][0] = GivenIP;
        	RTable[ind][1] = GivenPort;
        	break;
        }
        
        
        case "ClientToRouter1": {
        	
        	
        	String ClientIP = "";
        	
        	try {
        		ClientIP = in.readLine();
        	}catch (IOException e) {
            	System.err.println("Error3!");
            	System.exit(1);
            }
        	
        	
        	Socket toRouter = null;
        	
        	try {
        		toRouter = new Socket((String) RTable[0][0], 5556);
        	}catch (IOException e) {
            	System.err.println("Error4!");
            	System.exit(1);
            }
        	

        	PrintWriter outToRouter = null;
        	
        	try {
        		outToRouter = new PrintWriter(toRouter.getOutputStream(),true);
        	}catch (IOException e) {
            	System.err.println("Error5!");
            	System.exit(1);
            }
        	
        	
        	outToRouter.println("Router2ToServer");
        	outToRouter.println(ClientIP);
        	
        	
        	try {
        		toRouter.close();
        	}catch (IOException e) {
            	System.err.println("Error6!");
            	System.exit(1);
            }
        	break;        	
        }
        
        
        case "Router2ToServer": {
        	
        	
        	try {
        		Thread.currentThread();
        		Thread.sleep(5000);
        	}catch (InterruptedException e) {
            	System.err.println("Error7!");
            	System.exit(1);
            }
        	
        	
        	String ClientIP = "";
        	
        	try {
        		ClientIP = in.readLine();
        	}catch (IOException e) {
            	System.err.println("Error8!");
            	System.exit(1);
            }

        	
        	Socket toServer = null;
        	
        	try {
                toServer = new Socket("25.70.115.126", 5557);
                System.out.println(toServer);
            }  catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        	
        	
        	PrintWriter outToServer = null;
        	
        	try {
        		outToServer = new PrintWriter(toServer.getOutputStream(), true);
        	}catch (IOException e) {
            	System.err.println("Error10!");
            	System.exit(1);
            }
        	
        	outToServer.println(ClientIP);
        	break;
        	
        }
        	
        }
        
        
        } while (Coordinator != null);
    }
}

