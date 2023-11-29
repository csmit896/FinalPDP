package cns;
import java.net.*;
import java.io.*;

public class NodeA {
    public static void main(String[] args) throws Exception {
        String serverAddress = "127.0.0.1"; // IP address of Node B
        int serverPort = 5555; // port number of Node B
        String code = generateCode(); // generate 400-bit code

        // connect to Node B
        Socket socket = new Socket(serverAddress, serverPort);

        // send code to Node B
        OutputStream out = socket.getOutputStream();
        out.write(code.getBytes());
        out.flush();

        // receive challenge from Node B
        InputStream in = socket.getInputStream();
        byte[] challengeBytes = new byte[50];
        in.read(challengeBytes);
        String challenge = new String(challengeBytes);

        // perform challenge and send result to Node B
        String result = performChallenge(Integer.parseInt(challenge));
        out.write(result.getBytes());
        out.flush();

        // close the socket and streams
        out.close();
        in.close();
        socket.close();
    }

    private static String generateCode() {
    	 int code = 400; // set the code to 400 as per the project requirements
    	 String binaryCode = Integer.toBinaryString(code); // convert the code to its binary representation
    	 return String.format("%8s", binaryCode).replace(' ', '0'); // pad the binary string with leading zeros and return it as a string
    }

    private static String performChallenge(int A) {
    	int result = (((int) Math.pow(A, 2)) % A) * 10; // perform the challenge using the A^2 mod A formula, and multiply the result by 10 to ensure that it is 10 bits long
        String binaryResult = Integer.toBinaryString(result); // convert the result to its binary representation
        return String.format("%10s", binaryResult).replace(' ', '0'); // pad the binary string with leading zeros and return it as a string
    }
}