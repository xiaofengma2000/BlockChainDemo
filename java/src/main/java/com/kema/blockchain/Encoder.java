package com.kema.blockchain;

import java.security.MessageDigest;

public class Encoder {

    public static String encode(String str) throws Exception{
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] bs = digest.digest(str.getBytes());
        StringBuilder buff = new StringBuilder();
        for(byte b : bs)
        {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) buff.append('0');
            buff.append(hex);
        }
        //return new String(bs);
        return buff.toString();
    }
}
