package com.kema.p2p;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

import net.tomp2p.connection.DiscoverNetworks;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;

public class MasterPeer {

    int port = 4000;
    private Peer master;
    boolean stopped = false;

    public MasterPeer(int port) throws Exception {
        this.port = port;
        startServer();
    }

    public void stop(){
        stopped = true;
        master.shutdown();
    }

    public static final void main(String[] args) {
        try {
            new MasterPeer(4000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> listPeers() {
        return master.getPeerBean().getPeerMap().getAll().stream().map(peerAddress -> {
            return peerAddress.toString();
        }).collect(Collectors.toList());
    }

    private void startServer() throws Exception {
        Random rnd = new Random(43L);
        master = new PeerMaker(new Number160(rnd)).setPorts(4000).makeAndListen();
        Executors.newSingleThreadExecutor().submit(() -> {
            while (!stopped) {
                for (PeerAddress pa : master.getPeerBean().getPeerMap().getAll()) {
                    System.out.println("PeerAddress: " + pa);
                    FutureResponse fr1 = master.ping().ping(pa.createSocketTCP(), false);
                    fr1.awaitUninterruptibly();

                    if (fr1.isSuccess()) {
                        //System.out.println("peer online T:" + pa);
                    } else {
                        System.out.println("offline " + pa);
                    }
                }
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
