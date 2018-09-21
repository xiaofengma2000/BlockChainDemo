package com.kema.p2p;

import com.kema.blockcoin.CoinBlock;
import com.kema.blockcoin.CoinChain;
import com.kema.blockcoin.Person;
import net.tomp2p.peers.Number160;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

public class MinerApp {

    public static final Number160 CHAIN_KEY = Number160.createHash("ROOT_CHAIN");

    CoinChain chain;

    ClientPeer myPeer;

    Person person;

    boolean stopped = false;

    public MinerApp(ClientPeer myPeer) throws Exception {
        this.myPeer = myPeer;
        person = new Person();
        syncChain();

        Executors.newSingleThreadExecutor().submit(new Runnable() {
//        ForkJoinPool.commonPool().execute(new Runnable() {
            @Override
            public void run() {
                while(!stopped)
                {
                    try
                    {
                        Thread.sleep(20000);
                        //check if there is a newer chain
                        CoinChain cc = (CoinChain)get(CHAIN_KEY);
                        if(cc != null){
                            refreshChain(cc);
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private synchronized void refreshChain(CoinChain cc){
        if(this.chain == null){
            chain = cc;
        } else {
            if(cc.size() >= chain.size() && cc.isChainValid()){
                chain = cc;
                System.out.println("Block Chain refreshed : ");
                chain.printInfo();
            } else {
                try {
                    send(CHAIN_KEY, chain);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void send(Number160 key, Object value) throws IOException, InterruptedException {
        myPeer.send(key, value);
    }

    public Object get(Number160 key) throws IOException, ClassNotFoundException, InterruptedException {
        return myPeer.get(key);
    }

    public void stop() {
        stopped = true;
        myPeer.stop();
    }

    private void createChain() throws IOException, InterruptedException {
        CoinBlock b1 = new CoinBlock("root", person.publicKey);
        CoinChain cc = new CoinChain();
        cc.addCoin(b1);
        send(CHAIN_KEY, cc);
    }

    private void syncChain() throws InterruptedException, IOException, ClassNotFoundException {

        CoinChain cc = (CoinChain) get(CHAIN_KEY);
        if(cc == null){
            createChain();
        } else {
            refreshChain(cc);
        }
    }

    public void mining() throws IOException, InterruptedException, ClassNotFoundException {
        syncChain();
        CoinBlock b1 = new CoinBlock(chain.getChainList().get(chain.size()-1).getHash(), person.publicKey);
        chain.addCoin(b1);
        send(CHAIN_KEY, chain);
    }

    public static final void main(String[] args){

        try
        {
            //1 start miner 1
            MinerApp m1 = new MinerApp(new ClientPeer(4001));
            //2 create the chain
            CoinBlock b1 = new CoinBlock("root", m1.person.publicKey);
            CoinBlock b2 = new CoinBlock(b1.getHash(), m1.person.publicKey);
            CoinBlock b3 = new CoinBlock(b2.getHash(), m1.person.publicKey);
            CoinChain cc = new CoinChain();
            cc.addCoin(b1);
            cc.addCoin(b2);
            cc.addCoin(b3);
            m1.send(CHAIN_KEY, cc);

            //3. start miner 2
            MinerApp m2 = new MinerApp(new ClientPeer(4002));
            CoinChain cc2 = (CoinChain) m2.get(CHAIN_KEY);
            cc.printInfo();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "MinerApp{" +
                "chain=" + chain +
                ", Peer=" + myPeer +
                ", person=" + person +
                '}';
    }

    public List<String> listPeers() {
        return myPeer.listPeers();
    }

}
