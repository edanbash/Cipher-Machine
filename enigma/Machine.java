package enigma;

import java.util.ArrayList;
import java.util.Collection;

/** Class that represents a complete enigma machine.
 *  @author Edan Bash
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = new ArrayList<Rotor>();
        _allRotors.addAll(allRotors);
        _activeRotors = new Rotor[_numRotors];
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Return the active rotors in machine. */
    Rotor[] activeRotors() {
        return _activeRotors;
    }

    /** Return the plugboard in machine. */
    Rotor plugboard() {
        return _plugboard;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (rotors.length != _numRotors) {
            throw new enigma.EnigmaException("Invalid number of rotors");
        }

        int activeIndex = 0;
        for (String name: rotors) {
            for (int i = 0; i < _allRotors.size(); i += 1) {
                if (_allRotors.get(i).name().equals(name)) {
                    _activeRotors[activeIndex] = _allRotors.get(i);
                    activeIndex += 1;
                }
            }
        }

        for (int i = 0; i < _numRotors; i += 1) {
            Rotor r = _activeRotors[i];
            if (i == 0  && !r.reflecting()) {
                throw new enigma.EnigmaException("First rotor not a reflector");
            } else if (i > 0 && i < _numRotors - _pawls && r.rotates()) {
                throw new enigma.EnigmaException("Supposed to be fixed rotor");
            } else if (i >= _numRotors - _pawls && !r.rotates()) {
                throw new enigma.EnigmaException("Supposed to be moving rotor");
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != _numRotors - 1) {
            throw new enigma.EnigmaException("Incorrect number of settings");
        }

        for (int i = 1, j = 0; i < _numRotors; i += 1, j += 1) {
            char c = setting.charAt(j);
            if (!_alphabet.contains(c)) {
                throw new enigma.EnigmaException("Char: "
                        + c + "not in alphabet");
            }
            _activeRotors[i].setRing(0);
            _activeRotors[i].set(c);
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        for (int i = 0; i < plugboard.size(); i += 1) {
            char c = plugboard.alphabet().toChar(i);
            if (plugboard.permute(c) != plugboard.invert(c)) {
                throw new enigma.EnigmaException("Invalid plugboard");
            }
        }
        _plugboard = new Rotor("Plugboard", plugboard);
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean[] canRotate = new boolean[_numRotors];
        for (int i = _numRotors - 1; i >= 0; i -= 1) {
            if (_activeRotors[i].atNotch() && _activeRotors[i - 1].rotates()) {
                canRotate[i] = true;
                canRotate[i - 1] = true;
            }
        }
        canRotate[_numRotors - 1] = true;
        for (int i = 0; i < _numRotors; i += 1) {
            if (canRotate[i]) {
                _activeRotors[i].advance();
            }
        }

        int result = _plugboard.convertForward(c);
        for (int i = _numRotors - 1; i >= 0; i -= 1) {
            result = _activeRotors[i].convertForward(result);
        }
        for (int i = 1; i < _numRotors; i += 1) {
            result = _activeRotors[i].convertBackward(result);
        }
        result = _plugboard.convertBackward(result);
        return result;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        msg = msg.replaceAll(" ", "");
        String result = new String();
        for (int i = 0; i < msg.length(); i += 1) {
            result += _alphabet.toChar(convert(_alphabet.toInt(msg.charAt(i))));
        }
        return result;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors in machine. */
    private final int _numRotors;

    /** Number of pawls in machine. */
    private final int _pawls;

    /** List of all active rotors in machine. */
    private Rotor[] _activeRotors;

    /** List of all available rotors. */
    private ArrayList<Rotor> _allRotors;

    /** Stores plugboard setting of the machine. */
    private Rotor _plugboard;

}
