package com.kema.blockcoin;

import com.kema.blockchain.Block;

import java.io.Serializable;
import java.security.Key;
import java.security.PublicKey;
import java.util.*;

public class CoinBlock extends Block<Long> implements Serializable {

    protected  PublicKey miner;

    protected Map<String, Long> balance = new HashMap<>();

    protected List<Transaction> transList = new ArrayList<>();

    public CoinBlock(String previousHash, PublicKey publicKey) {
        super(previousHash, 100l);
        mineBlock();
        this.miner = publicKey;
        balance.put(keyToString(publicKey), this.getData());
    }

    public static String keyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public void addTransaction(Transaction t){
        //1. validate the balance
        String keySrc = keyToString(t.from);
        Long bal = balance.get(keySrc);
        if(bal == null || bal.longValue() < t.amound)
        {
            throw new RuntimeException("no suffieient found");
        }

        if(!t.verify()){
            throw new RuntimeException("Invalid transaction");
        }

        //2. transfer the balance
        transList.add(t);

        //3. calculate the balance
        balance.put(keySrc, bal - t.amound);
        String keyTarget = keyToString(t.to);
        if(balance.containsKey(keyTarget))
        {
            balance.put(keyTarget, balance.get(keyTarget) + t.amound);
        }
        else
        {
            balance.put(keyTarget, t.amound);
        }
    }

    public void calbalance(){
        balance.clear();
        balance.put(keyToString(this.miner), this.getData());

        for(Transaction t : transList){
            String keySrc = keyToString(t.from);
            String keyTarget = keyToString(t.to);
            Long bal = balance.get(keySrc);
            if(bal == null || bal.longValue() < t.amound)
            {
                throw new RuntimeException("no suffieient found");
            }
            balance.put(keySrc, bal - t.amound);
            if(balance.containsKey(keyTarget))
            {
                balance.put(keyTarget, balance.get(keyTarget) + t.amound);
            }
            else
            {
                balance.put(keyTarget, t.amound);
            }
        }
    }

    public void printBalance(){
        balance.entrySet().stream().forEach(en -> {
            System.out.println(en.getKey() + " : " + en.getValue());
        });
    }

    public static void main(String[] args){

        Person kema = new Person();

        CoinBlock b1 = new CoinBlock("root", kema.publicKey);
        CoinBlock b2 = new CoinBlock(b1.hash, kema.publicKey);
        CoinBlock b3 = new CoinBlock(b2.hash, kema.publicKey);

        b1.mineBlock();
        b2.mineBlock();
        b3.mineBlock();

    }

}
