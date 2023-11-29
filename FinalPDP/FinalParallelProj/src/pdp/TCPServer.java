package pdp;
import java.io.*;
import java.net.*;



public class TCPServer {
	private static final String FILETOSEND = "p1.png";// This file should be sent

	
	//building each ip section & port for easy access
	static String routerIP = "25.3.113.131";
	static String thisIP;

//   private static final String FILENAME = "panda.jpg";
	public static void main(String args[]) throws IOException {
		Socket socketToClient = null;
		thisIP = InetAddress.getLocalHost().getHostAddress();

		Socket clientSocket = new Socket("CANNON-PC6",5555);
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

		// THE ONLY WAY TO FIX THIS IS TO PARSE DATA IN THE THREAD!
		// The client needs to be linear in this order:
		/*
		 * Become built in the router / logged in the table. C->R1 Then request to see
		 * the server. C->R1->R2->S, S->C Finally a communication loop between these
		 * two. S->C over and over
		 */

		// this section sends the data to the router.
		out.println("AddIn");
		out.println(thisIP);

		// read input from router
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			System.err.println("I/O Connetion failed for: " + routerIP);
			System.exit(1);
		}

		String clientIPFound = "25.3.126.86";
		try {
			socketToClient = new Socket(clientIPFound, 5555);
			System.out.println("FINALLY CONNECTED to Client!");
		} catch (IOException e) {
			System.err.println("Client not found.");
			System.exit(1);
		}
		// this receives the string and then shoots it back as an upper case version
		String str = "";
		try {
			in = new BufferedReader(new InputStreamReader(socketToClient.getInputStream()));
			str = in.readLine();
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + routerIP);
			System.exit(1);
		}

		System.out.println("Received: " + str);
		str = str.toUpperCase();

		try {
			out = new PrintWriter(socketToClient.getOutputStream(), true);
			out.println(str);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + routerIP);
			System.exit(1);
		}

				File f1 = new File(FILETOSEND);
				// createNewFile makes one if not present, otherwise nothing
				f1.createNewFile();

				// this setup is for reading the data bytes from file into an array
				InputStream inputStream = new FileInputStream(f1);

				byte[] fileContent = inputStream.readAllBytes();
				// System.out.println("File successfully read!");

				// closing reading in from file and opening the socket output to send it
				inputStream.close();
				OutputStream byteOutStreams = socketToClient.getOutputStream();

				// make sure to log times, first when sending file
				long time1 = System.currentTimeMillis();
				// System.out.println("Pushing the file through OutStream");
				System.out.println(byteOutStreams);
				byteOutStreams.write(fileContent); // bing bing the server is written to
				byteOutStreams.flush();

				System.out.println(FILETOSEND + " file successfully flushed from stream!");
				byteOutStreams.close();

				long time2 = System.currentTimeMillis();
				System.out.println(FILETOSEND + " file sent in: " + (time2 - time1) + " miliseconds");
		// Code from the original instructions

		// close socket connections
		// after verification the buffered readers are done
		// can't be closed because they close the socket, binding.
		socketToClient.close();
		in.close();
		out.close();
	}

}