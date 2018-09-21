package com.kema.shell.master;

import com.kema.p2p.MasterPeer;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.stream.Collectors;

@ShellComponent
public class MasterCmd {

    private MasterPeer masterPeer;

    @ShellMethod(value = "list peers connected")
    public String list(){
        validateMasterCreated();
        return masterPeer.listPeers().stream().collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "start the master")
    public String start(int port){
        validateMasterNotCreated();
        try {
            masterPeer = new MasterPeer(port);
            return "Master started on port : " + port;
        } catch (Exception e) {
            e.printStackTrace();
            return String.valueOf(e);
        }
    }

    @ShellMethod(value = "stop the master")
    public String stop(){
        validateMasterCreated();
        try {
            masterPeer.stop();
            masterPeer = null;
            return "Master stopped.";
        } catch (Exception e) {
            e.printStackTrace();
            return String.valueOf(e);
        }
    }

    private void validateMasterCreated() {
        if(masterPeer == null){
            throw new RuntimeException("Master Peer not started yet.");
        }
    }

    private void validateMasterNotCreated() {
        if(masterPeer != null){
            throw new RuntimeException("Master Peer already started : " + masterPeer.toString());
        }
    }
}
