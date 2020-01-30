/**
 * 
 * This class is the main "hub" for prompting the user for inputs.  
 * 
 * @author Marcus
 *
 */
public class Controller {

	public Controller() {
		userInput = new UserScannerInput();
		promptUser();
	}
	
	
	
	public void promptUser() {
		
		System.out.println("Hello! Welcome to my program. Please input the name of the\n"
						+ ".tsv file you like to be analyzed.");
		String input = userInput.getScannerInput(0);
		new TSVFile(input);	
		
	}
	
	
	public static String promptFilter() {
		
		System.out.println("Please enter the field name you would like to be filtered from your chosen file."
				+ "\nInput nothing if you do not wish to filter the file: ");
		String fieldName = userInput.getScannerInput(1);

		return fieldName;
		
	}
	
	public static String promptFilterValue() {
		System.out.println("Enter the field value that you wish to be filtered from your chosen file.");
		String fieldValue = userInput.getScannerInput(1);
		
		return fieldValue;
	}
	
	
	public static String promptTerminal() {
		System.out.println("Please input the name of the field you would like the terminal operation to act upon."
				+ "\nInput nothing if you wish to not use the terminal.");
		String fieldName = userInput.getScannerInput(1);
		
		if (fieldName.equals("")) {
			return "";
		}
		
		return fieldName;
	}
	
	public static TerminalObservation promptObservation() {
		System.out.println("Please input the number that matches the operation you would to use:"
				+ "\n1) Count"
				+ "\n2) Max"
				+ "\n3) Min"
				+ "\n4) Is the same"
				+ "\n5) Is sorted");
		
		String fieldValue = userInput.getScannerInput(2);
		
		if (fieldValue.equals("1")) {
			return TerminalObservation.COUNT;
		}
		else if (fieldValue.equals("2")) {
			return TerminalObservation.MAX;
		}
		else if (fieldValue.equals("3")) {
			return TerminalObservation.MIN;
		}
		else if (fieldValue.equals("4")) {
			return TerminalObservation.ISSAME;
		}
		else if (fieldValue.equals("5")) {
			return TerminalObservation.ISSORTED;
		}
		return null;
	}
	
	
	
		
	
	private static UserScannerInput userInput;
	
}
