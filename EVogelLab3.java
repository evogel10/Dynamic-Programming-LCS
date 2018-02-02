import java.io.*;
import java.util.*;

/**
* Lab 3 reads in an input file containing a list of sequences. The sequences are validated
* to make sure they are in the correct format and contain only A's, C's, G's, or T's. The 
* valid sequences are then paired up and compared to find the longest common subsequence,
* which is written to an output file along with associated staticstics and invalid inputs.
*
* @author Eric Vogel
* @version 12/1/16
*/
public class EVogelLab3 {

	/**
	* Main method takes in an input file and uses the method readInData to
	* manupulate the data
	*
	* @param Args - is the file name in the command line
	* @exception FileNotFoundException
	*/
	public static void main(String[] args) throws IOException {

		try {
			readInData(new Scanner(new FileReader(args[0])), new File(args[1]));
		} catch (FileNotFoundException ex) {
			System.out.print("Error: " + ex);
		}
	}

	/**
	* Takes input file from main method and reads in line by line to build 
	* an arraylist of valid sequences to compare.
	*
	* @param input - file containing string sequences.
	* @param output - file containing results.
	*/
	private static void readInData(Scanner input, File file) throws IOException {
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
		String line;
		ArrayList<String> sequences = new ArrayList<String>();
		Stopwatch watch = new Stopwatch();
		StringBuilder resultsTable = new StringBuilder();
		StringBuilder invalidInput = new StringBuilder();
		int pair = 1;

		// Sets header for results table.
		resultsTable.append("-------------------------Algorithm Running Time---------------------------\n");
		resultsTable.append("Pair Number     Pair Lengths         Algorithm          Time In Nanosecond\n");

		// Sets head of invalid inputs table.
		invalidInput.append("\nInvalid Input(s)\n");
		invalidInput.append("----------------\n");

		try {
			// Parses through the input file line by line making sure that the string is all upper case.
			while ((line = (input.nextLine().toUpperCase())) != null) {

				// Checks if the input is valid
				if(validInput(line.trim())) {

					// Splits the string into an array by spaces.
					String[] seq = line.split(" ");

					// Checks to see if the sequence contains only A's, C's, G's, or T's.
					if (!(seq[2].matches("[A|C|G|T]+"))) {
						invalidInput.append(line + "\n");
					} else {
						// If the string is valid then the sequences that will be used to compare will be
						// in the 3 position in the array, which is added to the arraylist.
						sequences.add(seq[2]);
					}
				} else {
					if (line.equals("") || line.equals("\n")) {
					} else {
						// Appends the invalid input to the invalid input table.
						invalidInput.append(line + "\n");
					}
				}
    		}

		} catch (NoSuchElementException ex) {
			// End of file
		} catch (NumberFormatException ex) {
			System.out.println("Invalid Input.\n");
		}

		// While there are still sequences in the arraylist, they will be taken out to compare.
		while (!(sequences.size() == 0)) {

			// There must be at least 2 sequences in order to find the LCS.
			if (sequences.size() >= 2){
				String seq1 = sequences.remove(0);
				String seq2 = sequences.remove(0);
				watch.start();
				String subseq = compare(seq1.toCharArray(), seq2.toCharArray());
				watch.stop();

				writer.write("Pair " + pair + "\n");
				writer.write("Sequence 1: " + seq1 + "\n");
				writer.write("Sequence 2: " + seq2 + "\n");

				writer.write("Longest Common Subsequence: " + subseq + "\n");
				writer.write("Sequence Length: " + subseq.length() + "\n\n");
				resultsTable.append("  Pair  " + pair + "          " + seq1.length() + ", " + seq2.length() + 
									"        Dynamic Programing           " +  watch.timeInNanoseconds() + "\n");

			} else if (sequences.size() == 1){
				// If there is only one valid sequence left inside the arraylist, the LCS cannot be found.
				writer.write("One sequence left. No additional sequences to compare to: " 
							+ sequences.remove(0) + "\n\n");
			}
			pair++;
		}

		// Writes results and invalid input tables to the file.
		String results = resultsTable.toString();
		String errors = invalidInput.toString();
		writer.write(results);
		writer.write(errors);
		writer.close();
	}

	/**
	* Takes in line from the input file to check if the sequence is a valid input.
	*
	* @param line - possible valid sequence from input file.
	* @return true if input is valid and false if it is not.
	*/
	public static boolean validInput(String line) {

		String[] temp = line.split(" ");

		// Checks if the input is a space or new line.
		if (line.equals("") || line.equals("\n")) {
			return false;
		// If the input creates an array of less than 3, then the input is not in 
		// correct format and is invalid.
		} else if (temp.length < 3) {
			return false;
		} else {
			return true;
		}
	}

	/**
	* Dyanamic programming algorithm takes in two valid sequences from the input file and compares 
	* them to find the longest common subsequence.
	*
	* @param seq1 - first valid input sequence.
	* @param seq2 - second valid input sequence.
	* @return lCS - longest common subsequence of sequence 1 and sequence 2.
	*/
	public static String compare(char[] seq1, char[] seq2) {
		// Adjacency matrix used to find common matches.
		int[][] lCSAdjMatrix = new int[seq1.length + 1][seq2.length + 1];
		String[][] resultsMatrix = new String[seq1.length + 1][seq2.length + 1];

		// Compares the two sequences.
		for (int i = 1; i <= seq1.length; i++) {
			for (int j = 1; j <= seq2.length; j++) {
				if (seq1[i - 1] == seq2[j - 1]) {
					lCSAdjMatrix[i][j] = lCSAdjMatrix[i - 1][j - 1] + 1;
					resultsMatrix[i][j] = "diagonal";
				} else {
					lCSAdjMatrix[i][j] = Math.max(lCSAdjMatrix[i - 1][j], lCSAdjMatrix[i][j - 1]);
					if (lCSAdjMatrix[i][j] == lCSAdjMatrix[i - 1][j]) {
						resultsMatrix[i][j] = "up";
					} else {
						resultsMatrix[i][j] = "left";
					}
				}
			}
		}
		String temp = resultsMatrix[seq1.length][seq2.length];
		String lCS = "";
		int lat = seq1.length;
		int lon = seq2.length;
		// Loops through the results matrix to find the LCS.
		while (temp != null) {
			if (resultsMatrix[lat][lon] == "diagonal") {
				lCS = seq1[lat - 1] + lCS;
				lat--;
				lon--;
			} else if (resultsMatrix[lat][lon] == "up") {
				lat--;
			} else if (resultsMatrix[lat][lon] == "left") {
				lon--;
			}
			temp = resultsMatrix[lat][lon];
		}
		return lCS;
	}
}