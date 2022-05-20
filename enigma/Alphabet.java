package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Edan Bash
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _chars = chars;
        checkAlphabet();
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _chars.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return _chars.indexOf(ch) >= 0;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return _chars.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        return (contains(ch)) ? _chars.indexOf(ch) : -1;
    }

    /** Returns chars in this alphabet. */
    String getChars() {
        return _chars;
    }

    /** Makes sures no duplicates and bad characters not in alphabet. */
    private void checkAlphabet() {
        for (int i = 0; i < _chars.length(); i += 1) {
            char c = _chars.charAt(i);
            if (_chars.indexOf(c) != _chars.lastIndexOf(c)) {
                throw new enigma.EnigmaException("Non-unique Alphabet");
            }
            if (contains('(') || contains(')') || contains('*')) {
                throw new enigma.EnigmaException("Bad Alphabet: "
                        + c + "not allowed");
            }
        }
    }


    /** Characters in this alphabet. */
    private String _chars;

}
