package com.kema.blockcoin;

import java.io.Serializable;
import java.security.PublicKey;
import java.security.Signature;

public class Transaction implements Serializable {

    public PublicKey from;

    public PublicKey to;

    public long amound;

    public byte[] signature;

    public boolean verify() {
        try {
            Signature ecdsaVerify = Signature.getInstance("SHA256withRSA");
            String data = CoinBlock.keyToString(to) + amound;
            ecdsaVerify.initVerify(from);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sign(Person person) {
        String data = CoinBlock.keyToString(to) + amound;
        this.signature = person.sign(data);
    }

}
