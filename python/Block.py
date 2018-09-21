import hashlib


class CoinBlock:

    difficult = 3

    def __init__(self, owner, previousHash):
        self.balance = 50
        self.previousHash = previousHash
        self.index = 1
        self.owner = owner
        self.__mining__()
        self.transactions=[]

    def __mining__(self):
        data = str(self.index) + str(self.owner) + self.previousHash + str(self.balance)
        self.hash = hashlib.sha256(data.encode("ascii")).hexdigest()
        while(not self.validHash()):
            data = str(self.index) + str(self.owner) + self.previousHash + str(self.balance)
            self.hash = hashlib.sha256(data.encode("ascii")).hexdigest()
            # print(self.hash)
            self.index = self.index + 1
        print("Block mined : ", self.hash)

    def validHash(self):
        header = str(self.hash)[:CoinBlock.difficult]
        return (header.count('0') == CoinBlock.difficult)

    def addTransaction(self, transaction):
        if(self.owner==transaction.sender and transaction.verify() == 0):
            # todo validate transaction amout
            self.transactions.append(transaction)


if __name__ == "__main__":
    print(hashlib.sha256(b"Hello").hexdigest())
    cb = CoinBlock("test", "fff")