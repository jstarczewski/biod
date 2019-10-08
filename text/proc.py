import math
import re


# Calculates entropy of text using frequencies of chars
def calc_text_entropy(text: str):
    prob = {}
    add_char_prob(text, prob)
    result = 0
    for char in text:
        result += prob[char] * math.log(prob[char], 2)
    return -result


# Creates char probability dict
def add_char_prob(text: str, prob: dict):
    text_len = len(text)
    for char in text:
        prob[char] += 1 / text_len
    # for k in char_prob.keys():
    #   char_prob[k] /= text_len


# Creates char frequency dict
def add_char_freq(text: str, freq: dict):
    text_len = len(text)
    for char in text:
        freq[char] += 1
    # for k in char_prob.keys():
    #   char_prob[k] /= text_len


# Removes whitespaces from text
def remove_whitespaces(text: str):
    pattern = re.compile(r'\s+')
    return re.sub(pattern, '', text)


# Calculates char frequency for file
def create_char_freq_from_file(filepath: str):
    freq = {}
    with open(filepath, 'r') as f:
        for line in f:
            add_char_freq(line, freq)
    return freq


''' TODO
- select best way to calculate frequency/probability'''