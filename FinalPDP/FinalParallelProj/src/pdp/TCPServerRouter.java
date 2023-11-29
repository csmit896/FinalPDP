package pdp;
import java.net.*;
import java.io.*;

public class TCPServerRouter {
    //@SuppressWarnings("static-access")
    public static void main(String[] args) throws IOException {
        Socket clientSocket = null; // socket for the thread
        Object [][] RoutingTable = new Object [101][2]; // routing table
        Boolean Running = true;
        int ind = 0; // index in the routing table
        Boolean RouterConnected = false;

        //Accepting connections
        ServerSocket serverSocket = null; // server socket for accepting connections
        try {
            serverSocket = new ServerSocket(5556);
            System.out.println("ServerRouter is Listening on port: 5556.");
        }
        catch (IOException e) {
            System.err.println("Could not listen on port: 5556.");
            System.exit(1);
        } 
              
        
        // Creating threads with accepted connections
        while (Running == true)
        {
            try {
                clientSocket = serverSocket.accept();
                SThread t = new SThread(RoutingTable, clientSocket, ind); // creates a thread with a random port
                t.start(); // starts the thread
                ind++; // increments the index
                System.out.println("ServerRouter connected with Client/Server: " + clientSocket.getInetAddress().getHostAddress());
                
                if(!RouterConnected) {
                	System.out.println("runnin");
                    Socket socket = new Socket("25.66.101.63", 5555);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("AddMe");
                    out.println("25.3.126.86P5556");
                    RouterConnected = true;
                }
                
            }
            catch (IOException e) {
                System.err.println("Client/Server failed to connect.");
                System.exit(1);
            }
        }//end while

        //closing connections
        clientSocket.close();
        serverSocket.close();
        

    }
}