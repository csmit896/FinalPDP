package pdp;

import java.io.*;
import java.net.*;



public class TCPClient {
	private static final String input = "C:\\Users\\colem\\eclipse-workspace\\FinalPDP\\FinalParallelProj\\src\\pdp\\file.txt";// This file should be sent

	static String connectionIP = "25.70.115.179";
	static String thisIP;
	

	public static void main(String args[]) throws IOException {

		// important to have both for flavor, but the router and client IP's should be
		// the same
		// due to localized data
		thisIP = InetAddress.getLocalHost().getHostAddress();

		// building the Client->Router connection, don't need to send anything from
		// Router back.
		Socket clientSocket = new Socket("DESKTOP-CK5OHRN",5555);
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

		// this section sends the data to the router.
		out.println("AddIn");
		out.println(thisIP);

		// Request B connects to me, then wait for B to connect to me
		// once registered we can ask the router to push us over to the other client
		out.println("ClientToRouter1");
		out.println(connectionIP);

		// sample of sending a message, simulation of a echo server
		String clientMessage = "Howdy Howdy";

		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			out.println(clientMessage);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + connectionIP);
			System.exit(1);
		}

		// then receiving a message
		String temp = "";
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			temp = in.readLine();
		} catch (IOException e) {
			System.err.println("No I/O For: " + connectionIP);
			System.exit(1);
		}

		System.out.println("Received: " + temp);

		File f1 = new File(input);
		// createNewFile makes one if not present, otherwise nothing
		f1.createNewFile();

		// this setup is for reading the data bytes from file into an array
		InputStream inputStream = new FileInputStream(f1);

		byte[] fileContent = inputStream.readAllBytes();
				
		// closing reading in from file and opening the socket output to send it
		inputStream.close();
		OutputStream byteOutStreams = clientSocket.getOutputStream();

		// make sure to log times, first when sending file
		long time1 = System.currentTimeMillis();
		byteOutStreams.write(fileContent); // bing bing the server is written to
		byteOutStreams.flush();

		System.out.println(input + " file successfully flushed from stream!");
		byteOutStreams.close();

		long time2 = System.currentTimeMillis();
		System.out.println(input + " file sent in: " + (time2 - time1) + " miliseconds");
		in.close();
		out.close();
		clientSocket.close();
	}
}