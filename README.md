# BlockChainDemo
learn block chain by doing a demo

# Java Version
## High Level Overview:
1. Spring shell is used to provide a ineractive interface
2. TomP2P is used for P2P communication
3. No Persistent implemented, everything is in memory(Public Key, Block Chain etc.)

## How to start:
* Lanch BlockChainDemoMasterShell as Master
* Avalible Shell Commands:
  * help
  * start(port): start the master
  * list: list peers connected
  * stop: stop the master

* Launch as many BlockChainDemoMinerShell as needed, each instance represents one miner
* Avalible Shell Commands:
  * help
  * start(port, masterIp, masterPort): start a peer
  * list: list peers connected
  * mining: minning a new block
  * stop: stop a peer

# Python Version
to be done
