package enigma;

import org.junit.Test;

/** The suite of all JUnit tests for the Alphabet class.
 *  @author Edan Bash
 */
public class AlphabetTest {
    @Test(expected = enigma.EnigmaException.class)
    public void testUniqueAlphabet() {
        new enigma.Alphabet("AAA");
    }

    @Test(expected = enigma.EnigmaException.class)
    public void testSpecialCharacterAlphabet() {
        new enigma.Alphabet(")**");
    }

    @Test (expected = enigma.EnigmaException.class)
    public void testSpaceInAlphabet() {
        new enigma.Alphabet("3 4");
    }

    @Test
    public void testNormalAlphabet() {
        new enigma.Alphabet("20394vnw^");
    }


}
