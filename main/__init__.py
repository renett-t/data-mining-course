import math
import numpy as np


# k = m / n * ln(2)
def calculateNumberOfHashFunction(size, volume, probability):
    return int(math.ceil(size / volume * np.log(2)))


#  m = - (n * ln(p)) / ln(2)^2 OR simply m = n * 2       :D
def calculateBloomFilterLength(volume, probability):
    # return volume * 2
    return int(math.ceil(- volume * np.log(probability) / np.log(2) / np.log(2)))


class BloomFiler(object):

    def __init__(self, volume, probability):
        self.m = calculateBloomFilterLength(volume, probability)
        self.p = probability
        self.n = volume
        self.bloom_filter = [0 for i in range(self.m)]

        self.k = calculateNumberOfHashFunction(self.m, self.n, self.p)

        print('m = %d' % self.m)
        print('k = %d' % self.k)

    def _hash_djb2(self, s):
        hashRes = 5381
        for x in s:
            hashRes = ((hashRes << 5) + hashRes) + ord(x)
        return hashRes % self.m

    def _hash(self, item, k):
        return self._hash_djb2(str(k) + item)

    def add_to_filter(self, item):
        for i in range(self.k):
            self.bloom_filter[self._hash(item, i)] += 1

    def check_is_not_in_filter(self, item):
        for i in range(self.k):
            if self.bloom_filter[self._hash(item, i)] == 0:
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
    return words


if __name__ == '__main__':
    words_from_file = read_file("1984.txt")

    p = 0.10
    n = len(words_from_file)
    print(n)

    bloom_filter = BloomFiler(n, p)

    for word in words_from_file:
        bloom_filter.add_to_filter(word)
    print(bloom_filter.bloom_filter)

    words_to_check = ["ковид", "брат", "мыслепреступник", "пандемия", "картхолдер", "товарищ", "наградить", "немилость"]

    print("Проверим, НЕ содержатся ли слова в исходном тексте: ")
    for word in words_to_check:
        print(word, "--", bloom_filter.check_is_not_in_filter(word))

    # false positive probability = 0.9, n = 74074
    # m = 16244, k = 1
    # ковид -- False
    # брат -- False
    # мыслепреступник -- False
    # пандемия -- False
    # картхолдер -- True
    # товарищ -- False
    # наградить -- False
    # немилость -- False

    # false positive probability = 0.1, n = 74074
    # m = 355002, k = 4
    # ковид -- True
    # брат -- False
    # мыслепреступник -- False
    # пандемия -- True
    # картхолдер -- True
    # товарищ -- False
    # наградить -- False
    # немилость -- False




