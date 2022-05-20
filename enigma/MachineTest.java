package enigma;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import static org.junit.Assert.*;

public class MachineTest {

    private Alphabet alpha = new Alphabet();
    private Machine m;
    private ArrayList<Rotor> allRotors = new ArrayList<Rotor>();
    String[] testRotors = new String[]{"B", "Beta", "I"};

    Rotor _I = new MovingRotor("I", new Permutation("(AELTPHQXRU) (BKNW) "
            + "(CMOY) (DFG) (IV) (JZ) (S)", alpha), "Q");
    Rotor _II = new MovingRotor("II", new Permutation("((FIXVYOMW) (CDKLHUP)"
            + " (ESZ) (BJ) (GR) (NT) (A) (Q)", alpha), "E");
    Rotor _III = new MovingRotor("III", new Permutation("(ABDHPEJT) (CFLVMZO"
            + "YQIRWUKXSG) (N)", alpha), "V");
    Rotor _IV = new MovingRotor("IV", new Permutation("(AEPLIYWCOXMRFZBSTGJQ"
            + "NH) (DV) (KU)", alpha), "J");
    Rotor _V = new MovingRotor("V", new Permutation("(AVOLDRWFIUQ)(BZKSMNHYC)"
            + " (EGTJPX)", alpha), "Z");
    Rotor _VI = new MovingRotor("VI", new Permutation("(AJQDVLEOZWIYTS) (CGMN"
            + "HFUX) (BPRK)", alpha), "ZM");
    Rotor _VII = new MovingRotor("VII", new Permutation("(ANOUPFRIMBZTLWKSVEG"
            + "CJYDHXQ)", alpha), "ZM");
    Rotor _VIII = new MovingRotor("VIII", new Permutation("(AFLSETWUNDHOZVICQ)"
            + " (BKJ) (GXY) (MPR))", alpha), "ZM");
    Rotor _Beta = new FixedRotor("Beta", new Permutation("(ALBEVFCYODJWUGNMQTZ"
            + "SKPR) (HIX)", alpha));
    Rotor _Gamma = new FixedRotor("Gamma", new Permutation("(AFNIRLBSQWVXGUZDKM"
            + "TPCOYJHE)", alpha));
    Rotor _B = new Reflector("B", new Permutation("(AE) (BN) (CK) (DQ) (FU) "
            + "(GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", alpha));
    Rotor _C = new Reflector("C", new Permutation("(AR) (BD) (CO) (EJ) (FN) "
            + "(GT) (HK) (IV) (LM) (PW) (QZ) (SX) (UY))", alpha));

    public void intializeMachine(Alphabet alphabet, int numRotors, int pawls,
                                 String[] rotors) {
        Collections.addAll(allRotors,
                new Rotor[]{_I, _II, _III, _IV, _V, _VI, _VII, _VIII, _Beta,
                            _Gamma, _B, _C});
        m = new Machine(alphabet, numRotors, pawls, allRotors);
        m.insertRotors(rotors);
    }

    @Test
    public void testInsertRotors() {
        intializeMachine(alpha, 3, 1, testRotors);
        for (int i = 0; i < testRotors.length; i += 1) {
            assertEquals(m.activeRotors()[i].name(), testRotors[i]);
        }
    }

    @Test
    public void testSetRotors() {
        intializeMachine(alpha, 3, 1, testRotors);
        String setting = "AB";
        m.setRotors(setting);
        for (int i = 1, j = 0; j < setting.length(); i += 1, j += 1) {
            assertEquals(m.activeRotors()[i].setting(), alpha.toInt
                    (setting.charAt(j)));
        }
    }

    @Test (expected = enigma.EnigmaException.class)
    public void testSetFaultyPlugboard() {
        intializeMachine(alpha, 3, 1, testRotors);
        Permutation plugboard = new Permutation("(ABC)(DE)", alpha);
        m.setPlugboard(plugboard);
    }

    @Test
    public void testConvertInt() {
        testRotors = new String[]{"B", "Beta", "III", "IV", "I"};
        intializeMachine(alpha, 5, 3, testRotors);
        m.setRotors("AXLE");
        m.setPlugboard(new Permutation("(YF)(ZH)", alpha));
        assertEquals(25, m.convert(24));
    }

    @Test
    public void testConvertMsg() {
        testRotors = new String[]{"B", "Beta", "I", "II", "III"};
        intializeMachine(alpha, 5, 3, testRotors);
        m.setPlugboard(new Permutation("(TD)(KC)(JZ)", alpha));
        assertEquals("HGJNBOKDWALBFKUCMUTJZUIO", m.convert("I WAS SCARED OF "
                + "CODING IN JAVA"));
        assertEquals("XTYQFBDZRGBYFZCASYRU", m.convert("I WAS SCARED OF "
                + "USING GIT"));
        assertEquals("UAAFWOAGFKOCJGMUMOPCHTAVRSA", m.convert("AND STARTING "
                + "ALL THESE PROJECTS"));

    }
}
