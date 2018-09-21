package com.kema.blockcoin;

import java.security.*;


public class Person {

    public PublicKey publicKey;

    private PrivateKey privateKey;

    public Person(){
        generateKeyPair();
    }

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(512);
            KeyPair keyPair = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] sign(String input) {
        Signature rsa;
        byte[] output = new byte[0];
        try {
            rsa = Signature.getInstance("SHA256withRSA");
            rsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            rsa.update(strByte);
            byte[] realSig = rsa.sign();
            output = realSig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    public static void main(String[] args){
        Person w1 = new Person();
        System.out.println("public key : " + CoinBlock.keyToString(w1.publicKey));
        System.out.println("private key : " + CoinBlock.keyToString(w1.privateKey));
    }
}
