package test;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;

public class ECKeyExchange {
    public static void main(String[] args) throws Exception {
        // Generate key pairs for Alice and Bob
        KeyPair aliceKeyPair = generateKeyPair();
        KeyPair bobKeyPair = generateKeyPair();

        // Get public keys for Alice and Bob
        PublicKey alicePublicKey = aliceKeyPair.getPublic();
        PublicKey bobPublicKey = bobKeyPair.getPublic();

        // Perform key agreement for Alice and Bob
        KeyAgreement aliceKeyAgreement = KeyAgreement.getInstance("ECDH");
        aliceKeyAgreement.init(aliceKeyPair.getPrivate());
        aliceKeyAgreement.doPhase(bobPublicKey, true);
        SecretKey aliceSharedSecret = aliceKeyAgreement.generateSecret("AES");

        KeyAgreement bobKeyAgreement = KeyAgreement.getInstance("ECDH");
        bobKeyAgreement.init(bobKeyPair.getPrivate());
        bobKeyAgreement.doPhase(alicePublicKey, true);
        SecretKey bobSharedSecret = bobKeyAgreement.generateSecret("AES");

        // Print out shared secrets to verify that they match
        System.out.println("Alice's shared secret: " + bytesToHex(aliceSharedSecret.getEncoded()));
        System.out.println("Bob's shared secret: " + bytesToHex(bobSharedSecret.getEncoded()));
    }

    // Helper method to generate a key pair using the secp256r1 curve
    public static KeyPair generateKeyPair() throws Exception {
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(ecSpec, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    // Helper method to convert byte arrays to hexadecimal strings
    public static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}