from flask import Flask
from Chain import BlockChain
from Miner import Miner

app = Flask("BlockChainDemo")
chain = BlockChain()


@app.route("/")
def root():
    return "My Blocking Chain DEMO"


@app.route("/blocks")
def listBlocks():
    listStr = '<br>'.join(str(block.hash) for block in chain.chain)
    return "List of blocks : <br>" + listStr


@app.route("/blocks/<index>")
def viewBlock(index):
    if (len(chain.chain) <= int(index)):
        return "No such block"
    else:
        block = chain.chain[int(index)]
        return "Hash : " + str(block.hash) + " <br> blance : " + str(block.balancelist)


if __name__ == "__main__":
    m1 = Miner()
    m1.mining(chain)
    m2 = Miner()
    chain.getTail().addTransaction(m1.transfer(m2.pubickey, 20))
    m2.mining(chain)

    app.run()
