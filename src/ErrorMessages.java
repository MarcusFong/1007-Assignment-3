/**
 * 
 * There were a lot of repetitive error messages in this program
 * so I decided to put them all into one place to make editing them
 * easier. 
 * 
 * @author Marcus
 *
 */
public class ErrorMessages {

	
	public final static String wrongVariable = 
			"Please include the valid variable types in your header!";

	public final static String tooManyFields =
			"One of your records has too many fields then what was specified"
			+ "\nin the header!";
	
	public final static String badRecordFound = 
			"is an invalid field found in your .tsv file. The program will process only"
			+ "\nthe valid lines from up to that record...";
	
	public final static String tooManyFieldsInHeader =
			"Your variable type header has too many fields then what was specified"
			+ "\nin the previous header!";
	
	public final static String noHeaderInFile =
			"Sorry. No header with that name was found in your file."
			+ "\nEnding program...";
	
	public final static String noFileCreated =
			"Sorry. No file was created due to problems with your .tsv file.";
	
	public final static String noFileFound = 
			"Sorry. No file with that name was found. No file will be created.\nEnding program...";

	public final static String invalidFieldName =
			" is not an existing field name as defined in the header of the .tsv file.";

	
	public final static String endingProgram = 
			"No file will be created. Ending program...";
	
}
