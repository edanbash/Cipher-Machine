package enigma;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Edan Bash
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = new ArrayList<>();
        Pattern p = Pattern.compile("\\s*(\\([^\\s]+\\)\\s*)*\\s*");
        Matcher mat = p.matcher(cycles);
        if (!mat.matches()) {
            throw new enigma.EnigmaException("Bad cycle");
        }

        cycles = cycles.replaceAll("\\s+", "");
        cycles = cycles.replaceAll("\\(", "");
        checkCycles(cycles.replaceAll("\\)", ""));
        String[] cycleArray = cycles.split("\\)");
        for (int i = 0; i < cycleArray.length; i += 1) {
            if (!cycleArray[i].equals("")) {
                addCycle(cycleArray[i]);
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        cycle = cycle + cycle.charAt(0);
        _cycles.add(cycle);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        return _alphabet.toInt(permute(_alphabet.toChar(wrap(p))));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return  _alphabet.toInt(invert(_alphabet.toChar(wrap(c))));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (_alphabet.contains(p)) {
            for (String cycle: _cycles) {
                int index = cycle.indexOf(p);
                if (index >= 0) {
                    return cycle.charAt(index + 1);
                }
            }
            return p;
        } else {
            throw new enigma.EnigmaException(p + " not in alphabet");
        }
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (_alphabet.contains(c)) {
            for (String cycle: _cycles) {
                int index = cycle.lastIndexOf(c);
                if (index >= 0) {
                    return cycle.charAt(index - 1);
                }
            }
            return c;
        } else {
            throw new enigma.EnigmaException(c + " not in alphabet");
        }
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _alphabet.size(); i += 1) {
            if (i == permute(i)) {
                return false;
            }
        }
        return true;
    }

    /** Checks CYCLES for duplicates and non-alphabet characters.  */
    private void checkCycles(String cycles) {
        for (int i = 0; i < cycles.length(); i += 1) {
            char c = cycles.charAt(i);
            if (!_alphabet.contains(c)) {
                throw new enigma.EnigmaException(
                        "Bad Cycle: " + c + " not in alphabet");
            }
            if (cycles.indexOf(c)
                    != cycles.lastIndexOf(c)) {
                throw new enigma.EnigmaException(
                        "Bad Cycle: " + c + " is a duplicate");
            }
        }
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** List of cycles of this permutation. */
    private ArrayList<String> _cycles;

}
