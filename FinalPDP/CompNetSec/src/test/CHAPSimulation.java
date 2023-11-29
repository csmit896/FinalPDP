package test;
import java.util.Random;

public class CHAPSimulation {
    
    public static void main(String[] args) {
        NodeA nodeA = new NodeA();
        NodeB nodeB = new NodeB();
        boolean isAuthenticated = nodeA.authenticate(nodeB);
        if (isAuthenticated) {
            System.out.println("Authentication successful.");
        } else {
            System.out.println("Authentication failed.");
        }
    }
}

class NodeA {
    
    private static final int CODE = 400;
    private static final int CHALLENGE = (int) (Math.pow(CODE, 2) % CODE) * 10;
    
    public boolean authenticate(NodeB nodeB) {
    	System.out.println("Transferred data is: "+nodeB.getRandomNum());
        int randomNum = Integer.parseInt(nodeB.getRandomNum(), 2);
        int challengeResponse = performChallenge(randomNum);
        return nodeB.verifyChallengeResponse(challengeResponse);
    }
    
    private int performChallenge(int randomNum) {
        int challengeResult = (int) (Math.pow(randomNum, 2) % randomNum) * 10;
        System.out.println("challenge result is: "+challengeResult);
        return challengeResult;
        
    }
}

class NodeB {
    
    private static final int NUM_BITS = 10;
    private int randomNum;
    private String bits;
    
    public NodeB() {
        Random rand = new Random();
        this.randomNum = rand.nextInt((int) Math.pow(2, NUM_BITS));
        System.out.println("Node B generated number is: "+this.randomNum);
        this.bits = Integer.toBinaryString(this.randomNum);
    }
    
    public String getRandomNum() {
        return this.bits;
    }
    
    public boolean verifyChallengeResponse(int challengeResponse) {
        int expectedResponse = (int) (Math.pow(randomNum, 2) % randomNum) * 10;
        System.out.println("expected result is: "+expectedResponse);
        return challengeResponse == expectedResponse;
    }
}