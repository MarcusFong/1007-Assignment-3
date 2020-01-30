import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * 
 * It is important to point out that when I state something is an integer, I do
 * not mean the definition of an Integer by java standards (so not an "int")
 * but by mathematical standards, meaning any whole positive or negative number
 * is an Integer.
 * 
 * @author Marcus
 *
 */
public class FileChecker {
	
	
	public FileChecker (String inFileName) {
		theFileName = inFileName;
	}
	
	
	/**
	 * 
	 * This method takes a the name of a file (assuming it is a .tsv file) and
	 * returns the String of text that contains numOfLines amount of lines and 
	 * null if the .tsv is out of format or if the file doens't exist. Each
	 * line is defined by the @ character in the .tsv file.
	 * 
	 * @param fileName   is a String that holds the name of the .tsv that will be
	 *                   read.
	 * @param numOfLines is an int that represents the amount of lines (designated
	 *                   by '@') the user would like returned. If the user inputs a
	 *                   number less than 0, the compiler assumes that the user
	 *                   wants all the lines in the .tsv file.
	 * @return a String that contains the amount of lines the user asked to be
	 *         returned from the .tsv file.
	 */
	public static String getLines(String fileName, int numOfLines) {
		BufferedReader br = null;
		String newLine = "";
		
		try {

			br = new BufferedReader(new FileReader(fileName));
			String linesOfText = br.readLine();
			int previousLineIndex = 0;
			
			if (numOfLines > 0) {
				for (int i = 0; i < numOfLines-1; i++) {
					previousLineIndex = linesOfText.indexOf('@', previousLineIndex) + 1;
				}
	
				int endIndex = linesOfText.indexOf('@', previousLineIndex);
				
				newLine = linesOfText.substring(0, endIndex);
			}
			
			else {//return the entire .tsv file as a String
				newLine = linesOfText;
			}

		}
		
		//if there's no file found, end the program
		catch (IOException e) {
			System.out.println(ErrorMessages.noFileFound);
			newLine = null;
			e.printStackTrace();
		}
		//if there is no "@" in the header or if there are
		//less than 2 lines in the header
		catch (StringIndexOutOfBoundsException e) {
			System.out.println(ErrorMessages.noHeaderInFile);
			newLine = null;
		}

		finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return newLine;
	}
	
	/**
	 * 
	 * @return returns the number of variables to be accounted
	 * for in the header of the .tsv file. In addition, however,
	 * this method also can return -1 if the header is invalid. 
	 */
	public int getNumOfVariables() {
		String firstTwoLines = getLines(theFileName, 2);
		int endOfFirstLine = firstTwoLines.indexOf('@'); //helps separate the first line from the second
		int tabCount1 = 0;
		int tabCount2 = 0;
		
		String firstLine = firstTwoLines.substring(0, endOfFirstLine);
		
		
		for (int i = 0; i < endOfFirstLine; i++) {
			if (firstLine.charAt(i) == '*') {
				tabCount1++;
			}
		}
		
		for (int i = endOfFirstLine+1; i < firstTwoLines.length(); i++) {
			if (firstTwoLines.charAt(i) == '*') {
				tabCount2++;
			}
		}
		
		if (tabCount1 == tabCount2) {
			return tabCount1 + 1;
		}
		
		return -1;
	}
	
	/**
	 * This method reads through the entire .tsv file (not including the header) to
	 * check if the formatting of the file matches the formatting that was defined
	 * previously in the header of the file and returns only the lines that valid
	 * (it stops checking when it encounters a invalid line).
	 * 
	 * @param variables an array of Variable(s) that helps the compiler understand
	 *                  the correct format of the .tsv file (as defined by the
	 *                  header). This is basically an abstraction of the header.
	 * @return a String containing the only the valid lines int the .tsv file.
	 */
	public int checkFormatting(Variable[] variables) {
		// gets the index of the end of the second line in the .tsv file
		int startOfThirdLine = getLines(theFileName, -1).indexOf('@', getLines(theFileName, -1).indexOf('@') + 1) + 1;
		String linesOfText = getLines(theFileName, -1);
				
		
		// start iterating through the .tsv file starting after the end of the second
		// line
		int endOfRecord = 0;
		int recordLength = 0;
		int numValidLines = 0; 
		  
		String tempRecord = "";
		
		//This for loop iterates through the entire .tsv file record by record.
		for (int startOfRecord = startOfThirdLine; startOfRecord < linesOfText
				.length(); startOfRecord += recordLength + 1) {
			
			endOfRecord = linesOfText.indexOf('@', startOfRecord);

			// separates each record to be analyzed separately. I chose to break up the .tsv
			// file record by record to minimize calls to method getLines().
			tempRecord = linesOfText.substring(startOfRecord, endOfRecord);
			
			//if this record has bad formatting, return all valid lines so far.
			if (!checkRecordFormatting(tempRecord, variables)) {
				return numValidLines;
			}
			
			numValidLines++;
			recordLength = tempRecord.length();
		}
		return numValidLines;
	}
	
	/**
	 * 
	 * This method loops through a given record and checks for the right formatting
	 * according the the formatting set by the header in the .tsv file via the array
	 * variables. It checks if tempRecord has: 1) the right number of fields 2) if
	 * each field matches the data type specified in the header 3) if each field is 
	 * in bounds of the data type specified in the header (only if the field is
	 * a number)
	 * 
	 * @param tempRecord a record in the .tsv file
	 * @param variables  the formatting for the variables in the .tsv file.
	 * @return true if the record has correct formatting and false if it does not.
	 */
	public boolean checkRecordFormatting(String tempRecord, Variable[] variables) {
		tempRecord += "*"; // so the last word is counted

		int startOfField = 0;
		String theField = "";
		int count = 0;

		for (int j = 0; j < tempRecord.length(); j++) {
			if (count == variables.length) {
				System.out.println(ErrorMessages.tooManyFields);
				return false;
			}

			// checks if the field is a valid field
			if (tempRecord.charAt(j) == '*') {
				theField = tempRecord.substring(startOfField, j);
				
				if (checkFieldFormatting(theField, variables[count])) {
					startOfField = j + 1;
					count++;
				}
				
				else {
					return false;
				}
			}

		}

		return true;
	}

	/**
	 * This method checks if the specified field has the correct formatting
	 * according to theHeaderVariable as defined in the header of the .tsv file.
	 * 
	 * E.G: If theField is equal to 300 but, as defined by the header,
	 * theHeaderVariable says that fields of this type should be type byte, this
	 * method returns false.
	 * 
	 * @param theField          a field (String) that is going to be checked for the
	 *                          correct formatting/type as defined in the header of
	 *                          the file.
	 * @param theHeaderVariable is type Variable and contains the correct formatting
	 *                          for this type of field.
	 * @return true if the field has correct formatting and false if it does not.
	 */
	public static boolean checkFieldFormatting(String theField, Variable theHeaderVariable) {
		long numberField = 0;

		try { //try catch block to test if the field is a String or a number
			
			/*
			By assuming theField is supposed to be a long, I can parse it 
			to check if it is a String. If a number format exception is 
			thrown, I will know that it is a String.
			 */
			
			numberField = Math.abs(Long.parseLong(theField));

			if (numberField > Byte.MAX_VALUE 
					&& theHeaderVariable.getActualVariableType().equals("byte")) {
				System.out.println(theField + " " + ErrorMessages.badRecordFound);
				return false;

			} else if (numberField > Short.MAX_VALUE
					&& theHeaderVariable.getActualVariableType().equals("short")) {
				System.out.println(theField + " " + ErrorMessages.badRecordFound);
				return false;

			} else if (numberField > Integer.MAX_VALUE
					&& theHeaderVariable.getActualVariableType().equals("int")) {
				System.out.println(theField + " " + ErrorMessages.badRecordFound);
				return false;

			} else if (numberField > Long.MAX_VALUE
					&& theHeaderVariable.getActualVariableType().equals("long")) {
				System.out.println(theField + " " + ErrorMessages.badRecordFound);
				return false;

				
			} else { // if the field is an acceptable byte/short/int/long
				return true;
			}
			
			

		} catch (NumberFormatException e) {// if the field is not parsable/is a String (when we assumed it was
											// an integer)
			if (theHeaderVariable.isString()) {// if the field is an acceptable String
				return true;

			} else {
				System.out.println(theField + " " + ErrorMessages.badRecordFound);
				return false;
			}

		}
		
		
		
	}

	/**
	 * This method takes the name of the file (with the extension)
	 * and checks for its existence in the directory.
	 * 
	 * @param fileName the name of the file in the directory.
	 * @return true if the file exists or false if it doesn't.
	 */
	public static boolean searchForFile(String fileName) {

		BufferedReader br = null;

		try {// check to see if a .tsv exists

			br = new BufferedReader(new FileReader(fileName));
			return true;
		}

		catch (IOException e) { // runs if a .tsv file has not been found
			return false;

		} finally {
			try {

				br.close();

			} catch (Exception e) {
				
			}
		}

	}

	String theFileName;
	
	
	
	
	
	
	
}
