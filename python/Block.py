import hashlib


def toHashString(publickey):
    return str(hashlib.sha256(str(publickey).encode()).hexdigest())


class CoinBlock:

    difficult = 3

    def __init__(self, owner, previousHash):
        self.balance = 50
        self.previousHash = previousHash
        self.index = 1
        self.owner = owner
        self.__mining__()
        self.transactions=[]
        self.balancelist = dict()
        self.balancelist[toHashString(owner)] = self.balance

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
        if(transaction.verify() == 0):
            #validate transaction amout
            key = toHashString(transaction.sender)
            reckey = toHashString(transaction.recipient)
            # print(self.balancelist)
            # print(key)
            # print(self.balancelist.get(key, 0))
            if(transaction.amount <= self.balancelist.get(key, 0)):
                # transfer
                self.transactions.append(transaction)
                self.balancelist[key] = self.balancelist.get(key, 0) - transaction.amount
                self.balancelist[reckey] = self.balancelist.get(reckey, 0) + transaction.amount
                print("Transaction approved, current blance : ", self.balancelist)
            else:
                print("No sufficient found!")
        else:
            print("Invalid transaction!")


if __name__ == "__main__":
    print(hashlib.sha256(b"Hello").hexdigest())
    cb = CoinBlock("test", "fff")