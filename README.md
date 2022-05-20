CONTENTS:
This directory contains a software implementation of the Enigma machine used during WW2.

"The Enigma machine is a cipher device developed and used in the early- to mid-20th century to protect commercial, diplomatic, and military communication. It as an electromechanical rotor mechanism that scrambles the 26 letters of the alphabet. In typical use, one person enters text on the Enigma's keyboard and another person writes down which of the 26 lights above the keyboard illuminated at each key press. If plain text is entered, the illuminated letters are the encoded ciphertext. Entering ciphertext transforms it back into readable plaintext. The rotor mechanism changes the electrical connections between the keys and the lights with each keypress."
	
	Makefile		A makefile that will compile your
				files and run tests.  You must turn in a Makefile,
				'make' must compile all your files, and 
				'make check' must perform all your tests.  
				Currently, this makefile is set up to do just 
				that with our skeleton files.  Be sure to keep 
				it up to date.

	engima/			Directory containing the Engima package.

	    Rotor.java	 	Superclass that represents a rotor in the enigma machine.

	    Reflector.java	Class that represents a reflector in the enigma.

	    Permutation.java    Represents a permutation of a range of integers starting at 0 corresponding
				to the characters of an alphabet.

	    MovingRotor.java	Class that represents a rotating rotor in the enigma machine.

	    Main.java           Simulates the Engima machine.

	    Machine.java        Class that represents a complete enigma machine.

	    FixedRotor.java	Class that represents a rotor that has no ratchet and does not advance.

	    Alphabet.java       An alphabet of encodable characters.  Provides a mapping from characters
				to and from indices into the alphabet.
