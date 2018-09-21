package com.kema.blockcoin;

import java.io.Serializable;
import java.util.ArrayList;

public class CoinChain implements Serializable {

    public int size() {
        return myChain.size();
    }

    public ArrayList<CoinBlock> getChainList() {
        return myChain;
    }

    private ArrayList<CoinBlock> myChain = new ArrayList<>();

    public boolean isChainValid() {
        CoinBlock currentBlock;
        CoinBlock previousBlock;

        //loop through blockchain to check hashes:
        for(int i=1; i < myChain.size(); i++) {
            currentBlock = myChain.get(i);
            previousBlock = myChain.get(i-1);
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

            if(!currentBlock.isValid()){
                System.out.println("Invalid hash");
                return false;
            }
        }
        return true;
    }

    public void addCoin(CoinBlock cb){
        //1. check if the cb is valid or not
        if(!cb.isValid()){
            System.out.println("Current Hashes not valid");
            return;
        }

        if(!cb.getHash().equals(cb.calculateHash()) ){
            System.out.println("Current Hashes not equal");
            return;
        }

        //2. check against previous block
        if(myChain.size() > 0){
            CoinBlock preCb = myChain.get(myChain.size()-1);
            if(!preCb.getHash().equals(cb.getPreviousHash()) ) {
                System.out.println("Previous Hashes not equal");
                return;
            }
        }
        myChain.add(cb);
    }

    public void addTransaction(CoinBlock cb, Transaction trans){
        for(CoinBlock cbInChain : myChain){
            if(cbInChain.getHash().equals(cb.getHash())){
                //found the block
                System.out.println("Found block, adding transaction....");
                cbInChain.addTransaction(trans);
                return;
            }
        }
    }

    public void printInfo(){
        myChain.stream().forEach(block -> {block.printBalance();});
    }

    public static void main(String[] arge){
        Person p1 = new Person();
        Person p2 = new Person();

        Person kema = new Person();
        CoinBlock b1 = new CoinBlock("root", kema.publicKey);
        CoinBlock b2 = new CoinBlock(b1.getHash(), kema.publicKey);
        CoinBlock b3 = new CoinBlock(b2.getHash(), kema.publicKey);

        CoinChain cc = new CoinChain();

        cc.addCoin(b1);
        cc.addCoin(b2);
        cc.addCoin(b3);

        cc.printInfo();
        System.out.println("Is chain valid : " + cc.isChainValid());

        System.out.println("Before tranfer #1 : ");
        b1.printBalance();
        //transfer 50 to p1
        Transaction tran = new Transaction();
        tran.from = kema.publicKey;
        tran.to = p1.publicKey;
        tran.amound = 50;
        tran.sign(kema);

        //tran.amound = 80;
        cc.addTransaction(b1, tran);
        System.out.println("After tranfer #1 : ");
        b1.printBalance();

        tran = new Transaction();
        tran.from = kema.publicKey;
        tran.to = p2.publicKey;
        tran.amound = 50;
        tran.sign(kema);
        System.out.println("Before tranfer #2 : ");
        b3.printBalance();
        cc.addTransaction(b3, tran);
        System.out.println("After tranfer #2 : ");
        b3.printBalance();

//        tran = new Transaction();
//        tran.from = p1.publicKey;
//        tran.to = p2.publicKey;
//        tran.amound = 25;
//        tran.sign(kema);
//        System.out.println("before tranfer #3 : ");
//        addTransaction(b1, tran);
//        System.out.println("After tranfer #3 : ");
//        b1.printBalance();
    }

}
