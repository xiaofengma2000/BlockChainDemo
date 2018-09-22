import rsa
from Block import CoinBlock
from Miner import Miner

class BlockChain:

    def __init__(self):
        rootBlock = CoinBlock("root", "root")
        self.chain = [rootBlock]

    def getTail(self):
        return self.chain[-1]

    def append(self, block):
        self.chain.append(block)

if __name__ == "__main__":
    chain = BlockChain()
    m1 = Miner()
    m1.mining(chain)

    m2 = Miner()
    chain.getTail().addTransaction(m1.transfer(m2.pubickey, 50))
