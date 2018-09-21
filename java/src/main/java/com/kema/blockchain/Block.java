package com.kema.blockchain;

import java.io.Serializable;
import java.util.Date;

public class Block<T> implements Serializable {

    public String getPreviousHash() {
        return previousHash;
    }
    public String getHash() {
        return hash;
    }

    protected int difficulty = 4;

    protected String previousHash;

    public T getData() {
        return data;
    }

    protected T data;

    protected int index = 0;

    protected String hash;

    protected long timestamp;

    public Block(String previousHash, T data) {
        this.previousHash = previousHash;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public String calculateHash() {
        try{
            return Encoder.encode(String.valueOf(index) + previousHash + data + String.valueOf(timestamp));
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void mineBlock(){
        //below is the key of every coin
        hash = calculateHash();
        while(!isValid()){
            index ++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

    public boolean isValid() {
        String target = new String(new char[difficulty]).replace('\0', '0');
        return hash.substring(0, difficulty).equals(target);
    }

    public static void main(String[] args){

        Block<String> b1 = new Block("root", "no.1");
        Block<String> b2 = new Block(b1.hash, "No. 2");
        Block<String> b3 = new Block(b2.hash, "No. 3");

        b1.mineBlock();
        b2.mineBlock();
        b3.mineBlock();

    }

}
