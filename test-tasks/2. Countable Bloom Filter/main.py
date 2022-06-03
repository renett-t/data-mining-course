import math
import numpy as np


# k = m / n * ln(2)
def calculateNumberOfHashFunction(size, volume, probability):
    return int(math.ceil(size / volume * np.log(2)))


# from P(FP) = (1 - e^(-kn/m))^k and k = m / n * ln(2)
def calculateBloomFilterLength(n, probability):
    base = 1 - math.exp(- np.log(2))
    multiplier = n / np.log(2)
    return int(math.log(probability, base) * multiplier)


class BloomFiler(object):

    def __init__(self, volume, probability):
        self.m = calculateBloomFilterLength(volume, probability)
        self.p = probability
        self.n = volume
        self.bloom_filter = [0 for i in range(self.m)]

        self.k = calculateNumberOfHashFunction(self.m, self.n, self.p)

        print('m = %d' % self.m)
        print('k = %d' % self.k)

    def _hash_djb2(self, string, k):
        hashRes = 5381
        sim = 13
        for x in string:
            if k % 2 == 0:
                hashRes = ((hashRes << 5) + hashRes) + ord(x) + k*sim + sim*self.m
            else:
                hashRes = ((hashRes << 5) + hashRes) + ord(x) + k*sim

        return hashRes % self.m

    def calculate_hash(self, item, k):
        return self._hash_djb2(str(k) + item, k)

    def add_to_filter(self, item):
        for i in range(self.k):
            self.bloom_filter[self.calculate_hash(item, i)] += 1

    def check_is_not_in_filter(self, item):
        for i in range(self.k):
            if self.bloom_filter[self.calculate_hash(item, i)] == 0:
                return True
        return False


def read_file(file_name):
    file = open(file_name, "r")
    words = []
    lines = file.read().splitlines()

    for line in lines:
        line = line.lower().strip()

        drop_chars = '!@#$%ˆ&()_+—-={}[]|\\:;\"’<»«>,.?/1234567890…'
        for c in drop_chars:
            line = line.replace(c, "")

        inner = [word.strip() for word in line.split(' ')]
        for w in inner:
            if w != '':
                words.append(w)

    file.close()
    return set(words)


if __name__ == '__main__':
    words_from_file = read_file("grasshopper.txt")

    p = 0.10  # false positive probability
    n = len(words_from_file)
    print(n)
    print(words_from_file)

    bloom_filter = BloomFiler(n, p)

    for word in words_from_file:
        bloom_filter.add_to_filter(word)
    print(f'Countable Bloom filter = {bloom_filter.bloom_filter}')
