package com.kema.p2p;

import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.p2p.builder.GetBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ClientPeer {

    String masterIpAddress;
    int masterPort = 4000;
    private int port = 4001;
    private Peer client;

    public ClientPeer(int port) throws Exception {
        this(port, "127.0.0.1", 4000);
    }

    public ClientPeer(int port, String masterIpAddress, int masterPort) throws Exception {
        this.port = port;
        this.masterPort = masterPort;
        this.masterIpAddress = masterIpAddress;
        startClient();
    }

    private  void startClient() throws Exception {
        Random rnd = new Random(42L);
        client = new PeerMaker(new Number160(rnd)).setPorts(port).makeAndListen();
        System.out.println("Client started and Listening to: " + client.getBindings());
        System.out.println("address visible to outside is " + client.getPeerAddress());

        InetAddress address = Inet4Address.getByName(masterIpAddress);

        PeerAddress pa = new PeerAddress(Number160.ZERO, address, masterPort, masterPort);
        System.out.println("PeerAddress: " + pa);

        // Future Discover
        FutureDiscover futureDiscover = client.discover().setInetAddress(address).setPorts(masterPort).start();
        futureDiscover.awaitUninterruptibly();

        // Future Bootstrap - slave
        FutureBootstrap futureBootstrap = client.bootstrap().setInetAddress(address).setPorts(masterPort).start();
        futureBootstrap.awaitUninterruptibly();

        Collection<PeerAddress> addressList = client.getPeerBean().getPeerMap().getAll();
        System.out.println(addressList.size());

        if (futureDiscover.isSuccess()) {
            System.out.println("found that my outside address is " + futureDiscover.getPeerAddress());
        } else {
            System.out.println("failed " + futureDiscover.getFailedReason());
        }

        //client.shutdown();
    }

    public List<String> listPeers() {
        return client.getPeerBean().getPeerMap().getAll().stream().map(peerAddress -> {
            return peerAddress.toString();
        }).collect(Collectors.toList());
    }

    public void send(Number160 key, Object value) throws IOException, InterruptedException {
        final FutureDHT dht = client.put(key).setData(new Data(value)).start();
        dht.await();
    }

    public Object get(Number160 key) throws IOException, ClassNotFoundException, InterruptedException {
        final FutureDHT dht = client.get(key).start();
        final Data data = dht.await().getData();
        if(data == null){
            return null;
        }
        return data.getObject();
    }

    public void stop(){
        client.shutdown();
    }

    public static final void main(String[] args){
        try {
            new ClientPeer(4001, "127.0.0.1", 4000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
