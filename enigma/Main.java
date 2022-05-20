package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Edan Bash
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine m = readConfig();
        _input.useDelimiter("\\n");

        if (!_input.hasNext("\\*.+")) {
            throw new enigma.EnigmaException("Bad config file");
        }

        while (_input.hasNext()) {
            if (_input.hasNext("\\*.+")) {
                setUp(m, _input.next());
            } else {
                printMessageLine(m.convert(_input.next()));
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = alphabet();
            int numRotors = numRotors();
            int pawls = pawls();
            ArrayList<Rotor> rotors = new ArrayList<Rotor>();
            while (_config.hasNext()) {
                rotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, pawls, rotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return alphabet of machine. */
    private Alphabet alphabet() {
        if (_config.hasNext(".*")) {
            return new Alphabet(_config.next());
        }
        return new Alphabet();
    }

    /** Return numRotors of machine. */
    private int numRotors() {
        if (_config.hasNextInt()) {
            return _config.nextInt();
        }
        throw new enigma.EnigmaException("Bad config file");
    }

    /** Return pawls of machine. */
    private int pawls() {
        if (_config.hasNextInt()) {
            return _config.nextInt();
        }
        throw new enigma.EnigmaException("Bad config file");
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String type = _config.next();

            String cycles = "";
            while (_config.hasNext("\\([^\\s]*\\)")) {
                cycles += _config.next();
            }
            Permutation perm = new Permutation(cycles, _alphabet);

            if (type.charAt(0) == 'N' && type.length() == 1) {
                return new FixedRotor(name, perm);
            } else if (type.charAt(0) == 'R' && type.length() == 1) {
                return new Reflector(name, perm);
            } else if (type.charAt(0) == 'M') {
                return new MovingRotor(name, perm, type.substring(1));
            } else {
                throw new enigma.EnigmaException("Rotor type not recognized");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] setting = settings.split(" ");
        if (!setting[0].equals("*")) {
            throw new enigma.EnigmaException("Incorrect setting "
                    + "format: * not at beginning");
        }
        int n = M.numRotors();
        M.insertRotors(Arrays.copyOfRange(setting, 1, n + 1));
        M.setRotors(setting[n + 1]);

        String plugCycles = "";
        if (setting.length > n + 2) {
            if (setting[n + 2].charAt(0) == '(') {
                for (int i = n + 2; i < setting.length; i += 1) {
                    plugCycles += setting[i];
                }
            } else {
                if (setting[n + 2].length() != n - 1) {
                    throw new enigma.EnigmaException("Bad config file: "
                            + "incorrect settings");
                }
                for (int i = 1, j = 0; i < n; i += 1, j += 1) {
                    int currSetting = _alphabet.toInt(setting[n + 1].charAt(j));
                    int ringSetting = _alphabet.toInt(setting[n + 2].charAt(j));
                    M.activeRotors()[i].set(currSetting - ringSetting);
                    M.activeRotors()[i].setRing(ringSetting);
                }
                if (setting.length > n + 3) {
                    for (int i = n + 3; i < setting.length; i += 1) {
                        plugCycles += setting[i];
                    }
                }
            }
        }
        M.setPlugboard(new Permutation(plugCycles, _alphabet));
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i += 1) {
            if ((i + 1) % 5 == 0) {
                _output.print(msg.charAt(i) + " ");
            } else {
                _output.print(msg.charAt(i));
            }
        }
        _output.println();
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
