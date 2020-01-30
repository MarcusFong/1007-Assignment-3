import java.util.Scanner;
/**
 * 
 * This class helps with scanner input from the user. 
 * 
 * @author Marcus
 *
 */
public class UserScannerInput {
	
	/**
	 * 
	 * This method checks whether the input given by the user is a valid input
	 * or not. The method returns true if it is a valid one and vice versa.
	 * 
	 * @param input is a String that holds the user input
	 * @param whichInput this helps the compiler distinguish the type of input
	 * to be processed.
	 * @return this method returns true when the the input given by the user
	 * is a valid input. If it is not valid, it returns false.
	 */
	public boolean process(String input, int whichInput) {
		
		switch (whichInput) {
		
			case 0: //if the name of the inputed file is a .tsv file and exists
				if (input.indexOf(".tsv") != -1 
						&& FileChecker.searchForFile(input)){
					return true;
				}
				System.out.println("Please input a valid filename with the extension .tsv. Make sure the file"
								+ "\nexists!");
				return false;
				
			case 1:
				return true;
				
			case 2:
				try {
					int choice = Integer.parseInt(input);
					if (1 <= choice && choice <= 5) {
						return true;
					}
					System.out.println("Please input a number from 1 to 5!");
					return false;
				}
				catch (NumberFormatException e){
					System.out.println("Please input a number!");
					return false;
				}

				
				
		}
		return false;
	}
	
	
	
	/**
	 * 
	 * This method creates a new Scanner object for each time the user is prompted to input a 
	 * command into the console. It returns that inputed String only if what he or she inputed 
	 * is valid. However, if it is not valid, it will continue to prompt the user to input 
	 * a valid String until what they input is acceptable.  
	 * 
	 * @param whichInput helps denote what type of input is being processed. 
	 * @return returns the input if it is a valid input after processed by process()
	 */
	public String getScannerInput(int whichInput) {
		
		myScanner = new Scanner(System.in);
		
		boolean validInput = false;
		
		String input = "";
		while (!validInput) {
			try {
				input = myScanner.nextLine();
				validInput = process(input, whichInput);
			}
			catch (Exception e) {
				System.out.println(e);
			}
		}	
		return input;
		
	}
	
	Scanner myScanner;
	
	
}
