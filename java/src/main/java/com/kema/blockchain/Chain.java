package com.kema.blockchain;

import java.io.Serializable;
import java.util.ArrayList;

public class Chain implements Serializable {

    public static ArrayList<Block> blockchain = new ArrayList<>();

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;

        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.getHash().equals(currentBlock.calculateHash()) ){
                System.out.println("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.getHash().equals(currentBlock.getPreviousHash()) ) {
                System.out.println("Previous Hashes not equal");
                return false;
            }

            if(currentBlock.isValid()){
                System.out.println("Invalid hash");
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args){

        Block b1 = new Block("root", "no.1");
        Block b2 = new Block(b1.getHash(), "No. 2");
        Block b3 = new Block(b2.getHash(), "No. 3");

        b1.mineBlock();
        b2.mineBlock();
        b3.mineBlock();

        blockchain.add(b1);
        blockchain.add(b2);
        blockchain.add(b3);

        System.out.println("Validating Chain: " + isChainValid());

    }

}
