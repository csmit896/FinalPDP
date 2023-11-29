package cns;
//Node B class
import java.net.*;
import java.util.Random;
import java.io.*;

public class NodeB {
 public static void main(String[] args) throws Exception {
     int serverPort = 5555; // port number to listen on

     // listen for connections from Node A
     ServerSocket serverSocket = new ServerSocket(serverPort);
     System.out.println("Node B is listening on port " + serverPort);

     // accept incoming connections and handle authentication requests
     while (true) {
         Socket clientSocket = serverSocket.accept();
         handleAuthenticationRequest(clientSocket);
     }
 }

 private static void handleAuthenticationRequest(Socket clientSocket) throws Exception {
     // receive code from Node A
     InputStream in = clientSocket.getInputStream();
     byte[] codeBytes = new byte[50];
     in.read(codeBytes);
     String code = new String(codeBytes);

     // generate a random positive integer number and send it to Node A
     OutputStream out = clientSocket.getOutputStream();
     int challenge = generateChallenge();
     out.write(Integer.toString(challenge).getBytes());
     out.flush();

     // receive result from Node A and perform the challenge on the previous data
     byte[] resultBytes = new byte[50];
     in.read(resultBytes);
     String result = new String(resultBytes);
     String expected = performChallenge(challenge);
     if (result.equals(expected)) {
         System.out.println("Authentication successful");
     } else {
         System.out.println("Authentication failed");
     }

     // close the socket and streams
     out.close();
     in.close();
     clientSocket.close();
 }

 private static int generateChallenge() {
	 Random rand = new Random();
     int challenge = rand.nextInt(Integer.MAX_VALUE) + 1; // generate a random positive integer
     int binaryLength = Integer.toBinaryString(challenge).length(); // get the binary length of the integer
     int paddingLength = 32 - binaryLength; // calculate the amount of padding required to make the integer 32 bits long
     challenge <<= paddingLength; // shift the integer to the left by the padding length to make it 32 bits long
     challenge |= 1 << (paddingLength - 1); // set the most significant bit to 1 to ensure that the integer is positive
     String binaryChallenge = Integer.toBinaryString(challenge); // convert the challenge to its binary representation
     System.out.println("Challenge sent to NodeA: " + binaryChallenge);
     return challenge;
 }

 private static String performChallenge(int A) {
 	int result = (((int) Math.pow(A, 2)) % A) * 10; // perform the challenge using the A^2 mod A formula, and multiply the result by 10 to ensure that it is 10 bits long
    String binaryResult = Integer.toBinaryString(result); // convert the result to its binary representation
    return String.format("%10s", binaryResult).replace(' ', '0'); // pad the binary string with leading zeros and return it as a string
 }
}