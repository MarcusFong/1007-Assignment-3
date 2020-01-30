import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * This class accomplishes the duties of mainly step two 
 * and three, meaning it helps with filtering and terminally
 * observing the data fed to it. 
 * 
 * @author Marcus
 *
 */
public class TSVPipeline {
	
	/**
	 * 
	 * Creates a TSVPipeline that will help, eventually, create a filtered
	 * and valid .tsv file file (with a terminal observation output to the
	 * console). 
	 * 
	 * @param inFilter a TSVFilter with attributes that specify the
	 * filter and observation type.
	 * @param inVariables an array of Variable that represent the header
	 * fields to help the program understand which records have the right 
	 * formatting. 
	 */
	public TSVPipeline (TSVFilter inFilter, Variable[] inVariables) {
		theFilter = inFilter;
		variables = inVariables;
		
		myTerminal = new TerminalHelper(theFilter, variables);
	}
	
	/**
	 * This method checks if the filter is valid and if it is, 
	 * it creates a filtered .tsv file with only valid and 
	 * filtered lines. If the filter is invalid, a .tsv file is
	 * not made.   
	 * 
	 * @param numValidLines the number of valid lines with correct
	 * formatting as defined in the headers.
	 */
	public void doit(int numValidLines) {
		
		if (isFilterValid() && myTerminal.hasValidFieldName()) {
			// if the TSV file was created successfully.
			if (writeTSVFile(numValidLines) 
					&& !theFilter.getTerminalFieldName().equals("")) {
				System.out.println("Terminal results for observation " 
					+ theFilter.getObservation() + ": " + myTerminal.getResult());
			}
		}
		else {
			System.out.println(ErrorMessages.endingProgram);
		}
	}
	
	
	/**
	 * This method checks if the filter is valid, meaning if its
	 * field name and value correspond to what was defined in the
	 * header.  
	 * 
	 * @return true if the field name and value match 
	 * the correct formatting. False if not.
	 */
	public boolean isFilterValid() {
		if (checkFieldName() && checkFieldValue()) {
			return true;
		}
		return false;	
		
	}
	
	
	/**
	 * 
	 * This method checks the .tsv file for the existence of the
	 * filter's field name. 
	 * 
	 * @return returns true if the the filter's field name exists
	 * in the header of the .tsv file.
	 */
	public boolean checkFieldName() {
		
		if (theFilter.getFieldName().equals("")) {
			return true;
		}
		
		//I temporarily add the '*' at the end to count the last field
		String firstLine = FileChecker.getLines(theFilter.getFileName(), 1)+"*";
		
		int startOfField = 0;
		int count = 0;
		String tempFieldName = "";
		boolean fieldExists = false;
		
		for (int i = 0; i < firstLine.length(); i++) {
			if (firstLine.charAt(i) == '*') {
				tempFieldName = firstLine.substring(startOfField, i);
												
				if (tempFieldName.equals(theFilter.getFieldName())) {
					fieldExists = true;
					variableIndex = count;
					break;
				}
				startOfField = i + 1;
				count++;
			}
		}
		if (!fieldExists) {
			System.out.println(theFilter.getFieldName() + ErrorMessages.invalidFieldName);
			return false;
		}
		return true;
	}

	
	
	/**
	 * 
	 * This method checks if the field value in the filter is of the right variables
	 * type as specified in the second header of the .tsv file.
	 * 
	 * For example, if the specified the field "Age" to be only of type byte and the
	 * user inputs selects an age older than Byte.MAX_VALUE, the program stops.
	 * 
	 * @return true is the field value matches up with what its type is specified as
	 *         in the second header of the .tsv file.
	 */
	public boolean checkFieldValue() {
		
		//I temporarily add the '*' at the end to count the last field
		String firstTwoLines = FileChecker.getLines(theFilter.getFileName(), 2)+"*";
		String theField = theFilter.getField();
		
		
		//if the user didn't specify a field value.
		if (theFilter.getField().equals("")) {
			return true;
		}
		
		// int count represents the index that corresponds to the field in
		// the variables array. The variables array is in the same order as that
		// of the .tsv file header.
		int count = 0;

		// this for loop starts at the second line (second header) of the file
		for (int i = firstTwoLines.indexOf('@') + 1; i < firstTwoLines.length(); i++) {
			if (firstTwoLines.charAt(i) == '*') {

				if (count == variableIndex && FileChecker.checkFieldFormatting(theField, variables[count])) {
					return true;
				}

				count++;
			}
		}

		return true;
	}
	
	/**
	 * 
	 * This method goes through the .tsv file and returns a String
	 * that contains only records that match what the filter specified. If the user
	 * never specified a filter in select(), this method returns just
	 * valid records that have correct formatting as defined by the header.
	 * 
	 * @param numValidLines the number of valid lines (records) in the .tsv file
	 * that have the correct formatting as defined by the header.
	 * @return A String that contains only valid records from the .tsv file.
	 */
	public String filterThroughTSV(int numValidLines) {
		
		
		// if the user doens't specify anything, in select(), return all valid .tsv
		// lines. Does what step one does. 
		if (theFilter.getFieldName().equals("")) {
			System.out.println("Creating " + theFilter.getFileName() + " without a filter...");
		}
				
		int startOfThirdLine = FileChecker.getLines(theFilter.getFileName(), -1).
				indexOf('@', FileChecker.getLines(theFilter.getFileName(), -1).indexOf('@') + 1) + 1;
		
		String linesOfText = FileChecker.getLines(theFilter.getFileName(), numValidLines+2) + "@";
		String filteredText = "";
		
		String tempRecord = "";
		int recordLength = 0;
		int endOfRecord = 0;
		
		//this for loop scans through the .tsv file record by record
		for (int startOfRecord = startOfThirdLine; startOfRecord < linesOfText.length(); 
				startOfRecord += recordLength+1) {
						
			endOfRecord = linesOfText.indexOf('@', startOfRecord);
			tempRecord = linesOfText.substring(startOfRecord, endOfRecord);
			
			if (theFilter.getFieldName().equals("") || isFieldInRecord(tempRecord)) {
				myTerminal.doObservation(tempRecord);
				filteredText += tempRecord+"@";
			}
			
			recordLength = tempRecord.length();
		}
		
		// the call to getLines() is to include the two headers of the file to the
		// filtered text.
		return FileChecker.getLines(theFilter.getFileName(), 2) + "@" + filteredText;
	}
	
	
	
	/**
	 * This method returns true is a specific record contains 
	 * the field that is to be filtered through.
	 * 
	 * @param theRecord a String that represents a record from the 
	 * .tv file.
	 * @return a boolean. True is the filtered field was found
	 * inside theRecord and false if not. 
	 */
	public boolean isFieldInRecord(String theRecord) {
		int startOfField = 0;
		int count = 0;
		
		//I temporarily add the '*' at the end to count the last field
		theRecord += "*";
			
		for (int i = 0; i < theRecord.length(); i++) {
			if (theRecord.charAt(i) == '*') {
				String tempField = theRecord.substring(startOfField, i);
				
				// if a matching field has been in a record 
				if (theFilter.getField().equals(tempField) && count == variableIndex) {
					return true;
				}
				startOfField = i+1;
				count++;
			}
		}
		return false;
	}
	
	
	
	/**
	 * This program writes a filtered .tsv file containing only valid lines (lines
	 * that match the formatting specified by the header) to the directory of this
	 * program.
	 * 
	 * 
	 * @param numValidLines the number of valid lines the filter should process to
	 *                      be written into the .tsv file.
	 * @return true if the file was successfully written or false if it wasn't.
	 */
	public boolean writeTSVFile(int numValidLines) {
		String nameOfFile = theFilter.getFileName();
		File file = null;

		// names the new .tsv file with v2 at the end of it
		String newFileName = nameOfFile.replace(".", "v2.");

		file = new File(newFileName);

		if (!file.exists()) {

			try {

				file.createNewFile();
				PrintWriter pw = new PrintWriter(file);

				pw.println(filterThroughTSV(numValidLines));

				pw.close();
				System.out.println("Created the file " + newFileName + "!");
				return true;
			}

			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("A file with that name already exists!" + "\nPlease delete it and restart the program!");
			return false;
		}
		return false;

	}

	
	private TerminalHelper myTerminal;
	private Variable[] variables;
	private int variableIndex;
	private TSVFilter theFilter;
	
}
