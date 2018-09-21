package com.kema.shell.miner;

import com.kema.p2p.ClientPeer;
import com.kema.p2p.MinerApp;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.stream.Collectors;

@ShellComponent
public class MinerCmd {

    MinerApp miner;

    @ShellMethod(value = "start a peer")
    public String start(int port, String masterIp, int masterPort) throws Exception {
        validatePeerNotCreated();
        ClientPeer clientPeer = new ClientPeer(port, masterIp, masterPort);
        miner = new MinerApp(clientPeer);
        return "Peer registerred.";
    }

    @ShellMethod(value = "stop a peer")
    public String stop() throws Exception {
        validatePeerCreated();
        miner.stop();
        miner = null;
        return "Peer un-registerred.";
    }

    @ShellMethod(value = "list peers connected")
    public String list(){
        validatePeerCreated();
        return miner.listPeers().stream().collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "minning a new block")
    public String mining() throws Exception {
        validatePeerCreated();
        miner.mining();
        return "A new block mined.";
    }

    private void validatePeerCreated() {
        if(miner == null){
            throw new RuntimeException("Peer not started yet.");
        }
    }

    private void validatePeerNotCreated() {
        if(miner != null){
            throw new RuntimeException("Peer already started : " + miner.toString());
        }
    }
}
