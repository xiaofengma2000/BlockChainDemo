import rsa
from rsa import VerificationError
from Block import CoinBlock


def __verify(self, signature, recipient, value):
    try:
        data = str(recipient) + str(value)
        rsa.verify(data.encode('utf8'), signature, self.pubickey)
        return 0
    except VerificationError:
        return 1
    else:
        return 2


class Miner:

    def __init__(self):
        pub, pri = rsa.newkeys(1024)
        self.privatekey = pri
        self.pubickey = pub
        self.pubickey.save_pkcs1()

    def __sign(self, recipient, value):
        data = str(recipient) + str(value)
        hash = rsa.compute_hash(data.encode('utf8'), 'SHA-1')
        signature = rsa.sign_hash(hash, self.privatekey, 'SHA-1')
        return signature

    def transfer(self, recipent, amount):
        signature = self.__sign(recipent, amount)
        return Transaction(self.pubickey, recipent, amount, signature)

    def mining(self, preblock):
        return CoinBlock(self.pubickey, preblock.hash)


class Transaction:

    def __init__(self, sender, recipient, amount, signature):
        self.sender = sender
        self.recipient = recipient
        self.amount = amount
        self.signature = signature

    def verify(self):
        try:
            data = str(self.recipient) + str(self.amount)
            rsa.verify(data.encode('utf8'), self.signature, self.sender)
            return 0
        except VerificationError:
            return 1
        else:
            return 2


if __name__ == "__main__":
    miner = Miner()
    trans = miner.transfer("person 1", 50)
    print(trans.verify())
    trans.amount = 51
    print(trans.verify())
