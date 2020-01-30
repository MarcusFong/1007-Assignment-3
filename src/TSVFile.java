import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
/**
 * 
 * This class represents the contents of the TSV file. Its 
 * main job is to save the headers of the file (and not the 
 * rest of the content as it would be too large to store) and 
 * to check if the contents of the file are valid and match
 * the formatting defined by the headers of the file.  
 * 
 * 
 * @author Marcus
 *
 */
public class TSVFile {

	/**
	 * Creates a TSVFile object that analyzes a .tsv file, checks its formatting and
	 * validity, and writes a new .tsv file that contains only its valid lines.
	 * 
	 * @param inNameOfFile the name of the .tsv file in the directory
	 */
	public TSVFile(String inNameOfFile) {
		nameOfFile = inNameOfFile;
		theFileChecker = new FileChecker(nameOfFile);
		theFirstTwoLines = FileChecker.getLines(nameOfFile, 2);

		if (theFirstTwoLines != null) {
			System.out.println("Successfully retrieved headers from .tsv file...");
			processFile();
		}

	}
	
	
	/**
	 * This method checks a file's validity and processes it depending on the
	 * given filters and terminal observation specified by the user. If it passes,
	 * a new .tsv file is written to the directory with the specific parameters 
	 * as specified by the user. 
	 */
	public void processFile() {

		int numValidLines = getValidLines();
		if (numValidLines >= 0) {

			String fieldName = Controller.promptFilter();
			String fieldValue = "";

			// if the user wants to specify a filter value
			if (!fieldName.equals("")) {
				fieldValue = Controller.promptFilterValue();
			}

			String termFieldName = Controller.promptTerminal();
			TerminalObservation termObservation = TerminalObservation.NOTHING;

			// if the user wants to specify a terminal observation
			if (!termFieldName.equals("")) {
				termObservation = Controller.promptObservation();
			}

			TSVFilter myTSVFilter = new TSVFilter
					.WhichFile(nameOfFile)
					.select(fieldName, fieldValue)
					.terminate(termFieldName, termObservation)
					.done();
			
			//printing out myTSVFilter's attributes
			System.out.println(myTSVFilter + "\n");

			new TSVPipeline(myTSVFilter, variables).doit(numValidLines);
			
			
		} else {
			System.out.println(ErrorMessages.noFileCreated);
		}
	}
	
	
	
	
	/**
	 * 
	 * This method returns only the valid lines from the 
	 * original .tsv file as a String and null if the header(s)
	 * that were defined in the file are invalid. 
	 * 
	 * @return a String consisting of only the valid lines
	 * from the original .tsv file or null if the either of
	 * the fails to match formatting.
	 */
	public int getValidLines() {
		printContents();
		
		int numOfVariables = theFileChecker.getNumOfVariables();
		
		//checks the number of field names matches the number of field types in 
		//the headers
		if (numOfVariables == -1) {
			System.out.println(ErrorMessages.tooManyFieldsInHeader);
			return -1;
		}
		
		variables = new Variable[numOfVariables];
		getVariableNames();
		
		//if the header has invalid variable types
		if (!getVariableTypes()) {
			System.out.println(ErrorMessages.wrongVariable);
			return -1; 
		}
		
		int validLines = theFileChecker.checkFormatting(variables);
		
		return validLines;
		
		
		
	}
	
	/**
	 * This method sets each element in the array variables to 
	 * its respective variable name according to the 
	 * first line of the .tsv file.  
	 */
	public void getVariableNames() {
		int endOfFirstLine = theFirstTwoLines.indexOf('@');
		String firstLine = theFirstTwoLines.substring(0, endOfFirstLine);

		
		int startOfWord = 0;
		int index = 0;
		//record the variable names
		for (int i = 0; i < endOfFirstLine; i++) {
			if (firstLine.charAt(i) == '*') {
				variables[index] = new Variable();
				variables[index].setVariableName(firstLine.substring(startOfWord, i));
				startOfWord = i + 1;
				index++;
			}
		}
		//includes the last variable in firstLine
		variables[index] = new Variable();
		variables[index].setVariableName(firstLine.substring(startOfWord)); 
		
	}
	
	/**
	 * This method sets each element in the array variables to 
	 * its each respective variable type according to the 
	 * second line of the .tsv file. This method assumes that
	 * the variable types are all either integers or Strings. 
	 */
	public boolean getVariableTypes() {
		int startOfSecondLine = theFirstTwoLines.indexOf('@') + 1;
		
		//cuts out the secondLine from the theFirstTwoLines to simplify code
		String secondLine = theFirstTwoLines.substring(startOfSecondLine, 
				theFirstTwoLines.length());
		
		secondLine += "*"; //we add the '*' to include the last variable type in the second line 
		
		int startOfWord = 0;
		int index = 0;
		String variableType = "";
		//record the variable types 
		for (int i = 0; i < secondLine.length(); i++) {
			
			if (secondLine.charAt(i) == '*' ) {
				variableType = secondLine.substring(startOfWord, i);

				if (variableType.equals("byte") || variableType.equals("short") || variableType.equals("int")
						|| variableType.equals("long")) {
					variables[index].setAsString(false);
				}
				else if (variableType.equals("String")) {
					variables[index].setAsString(true);
				}
				else {
					return false;
				}
				variables[index].setActualVariableType(variableType);
				startOfWord = i + 1;
				index++;
			}
		}
		
		return true;
	}
	
	/**
	 * Prints the contents of the .tsv file line by line. (Separated by @)
	 */
	public void printContents() {
		BufferedReader br = null;	
		try {

			br = new BufferedReader(new FileReader(nameOfFile));
			String linesOfText = br.readLine();
			
			int startOfLine = 0;
			System.out.println(); 
			for (int i = 0; i < linesOfText.length(); i++) {
				if (linesOfText.charAt(i) == '@') {
					System.out.println(linesOfText.substring(startOfLine, i));
					startOfLine = i + 1;
				}
			}
			System.out.println(linesOfText.substring(startOfLine));
		}

		catch (IOException e) {
			e.printStackTrace();
		}

		finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private String theFirstTwoLines;
	private String nameOfFile;
	private Variable[] variables;
	private FileChecker theFileChecker;
	
	
	
	
}
