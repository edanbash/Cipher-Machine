package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Edan Bash
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testPermuteAndInvert() {
        perm = new Permutation("(BACD)", new enigma.Alphabet("ABCD"));
        alpha = perm.alphabet().getChars();
        checkPerm("Test with one regular cycle", "ABCD", "CADB");

        perm = new Permutation("(HIG)(NF)(L)", new enigma.Alphabet("HILFNGR"));
        alpha = perm.alphabet().getChars();
        checkPerm("Test with multiple cycles", "HILFNGR", "IGLNFHR");
        assertEquals(2, perm.invert(9));
        assertEquals(3, perm.invert(18));
        assertEquals(6, perm.invert(-1));

        perm = new Permutation("(B)", new enigma.Alphabet("AB"));
        alpha = perm.alphabet().getChars();
        checkPerm("Test with cycle of length 1", "AB", "AB");
        assertEquals(0, perm.invert(2));
        assertEquals(1, perm.invert(3));
    }


    @Test
    public void testDerangement() {
        perm =  new Permutation("(BACD)", new enigma.Alphabet("ABCD"));
        assertTrue(perm.derangement());

        perm = new Permutation("(HIG)(NF)(L)", new enigma.Alphabet("HILFNGR"));
        assertFalse(perm.derangement());

        perm = new Permutation("(B)", new enigma.Alphabet("AB"));
        assertFalse(perm.derangement());
    }

    @Test(expected = enigma.EnigmaException.class)
    public void testNotInAlphabet() {
        perm = new Permutation("(BACD)", new enigma.Alphabet("ABCD"));
        perm.invert('F');
    }

    @Test
    public void testEmptyCycle() {
        perm = new Permutation("", new enigma.Alphabet("ABCD"));
        assertEquals('A', perm.permute('A'));
    }

    @Test(expected = enigma.EnigmaException.class)
    public void testWeirdCycle() {
        new Permutation("(awjefk.-dasjN", new enigma.Alphabet("ABCD"));
    }

    @Test(expected = enigma.EnigmaException.class)
    public void testCycleDuplicates() {
        new Permutation("(AA)", new enigma.Alphabet("ABCD"));
    }

    @Test(expected = enigma.EnigmaException.class)
    public void testNonLetterCycle() {
        new Permutation("(??*&&^)", new enigma.Alphabet("?*&^"));
    }

    @Test(expected = enigma.EnigmaException.class)
    public void testWhiteSpaceInCycle() {
        new Permutation("(A  B)", new enigma.Alphabet("ABC")); }

    @Test
    public void testWhiteSpaceBtwnCycle() {
        new Permutation(" (AC)  (B)", new enigma.Alphabet("ABCD")); }

    @Test (expected = enigma.EnigmaException.class)
    public void testNoContentCycle() {
        new Permutation("()", new enigma.Alphabet("ABCD")); }

    @Test
    public void testOneChar() {
        new Permutation("(?)", new enigma.Alphabet("^?")); }



}
