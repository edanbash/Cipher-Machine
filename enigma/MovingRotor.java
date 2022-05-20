package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Edan Bash
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);

        for (int i = 0; i < notches.length(); i += 1) {
            if (!alphabet().contains(notches.charAt(i))) {
                throw new enigma.EnigmaException("Bad Notch: "
                        + notches.charAt(i));
            }
        }
        _notches = notches;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        char notch = alphabet().toChar(permutation().wrap(setting()
                + ringSetting()));
        return _notches.contains(Character.toString(notch));
    }

    @Override
    void advance() {
        set((setting() + 1) % size());
    }

    /** Contains the notches of the rotor. */
    private String _notches;

}
