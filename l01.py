from text import proc as pr


dict_size = 256  # Size of character set
offset = 0  # Position of first character in set


def main():
    global dict_size
    global offset
    # text = "napis do testowania"
    text = "###"
    # print(rot(text, 3))
    # print(rot256(rot256(text, 13), 13, True))
    print(vigenere(vigenere(text, "abcd"), "abcd", True))
    # print(vigenere(text, "###"))


# Encrypt/Decrypt given text by rotation
def rot(text: str, distance: int, reverse=False):
    distance *= -1 if reverse else 1
    result = ""
    for char in text:
        result += chr(((ord(char) + distance - offset) % dict_size) + offset)
    return result


# Encrypt/Decrypt given text by Vigenere cipher using keyword
def vigenere(text: str, keyword: str, decrypt=False):
    result = ""
    for index, char in enumerate(text):
        result += rot(char, ord(keyword[index % len(keyword)]) - offset, decrypt)
    return result


def decrypt_by_freq(ref_file, text):
    pass


''' TODO
- decrypt using char freq
- plot char freq for comparison
- RC4
- bruteforce with entropy
'''


if __name__ == "__main__":
    main()
