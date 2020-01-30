/**
 * 
 * This is a helper class to TSVPipeline that helps
 * with terminal operations on the .tsv file. Because only
 * one observation can be done at a time, many of the fields
 * in this method will be UNUSED because they belong to different
 * observation methods. For example, if the user chooses 
 * MAX, fields like count, isFirstObservedField, and previousField
 * will not be used/defined because they are irrelevant.
 * 
 * @author Marcus
 *
 */
public class TerminalHelper {
	
	public TerminalHelper(TSVFilter theFilter, Variable[] inVariables) {
		theFieldName = theFilter.getTerminalFieldName();
		if (!theFieldName.equals("")) {
			theObservation = theFilter.getObservation();
			variables = inVariables;
			fieldExistence = checkFieldExistence(); 
		}
		else {
			System.out.println("No terminal observation will be made...");
		}

	}
	
	/**
	 * This method helps the program understand which observation 
	 * is to be performed for each record that is passed through 
	 * this method.
	 * 
	 * @param theRecord to be observed.
	 */
	public void doObservation(String theRecord) {
		
		if (fieldExistence) {
			String theField = isolateField(theRecord);
					
			switch (theObservation) {
			case COUNT:
				increment();
				break;
				
			case MAX:
				checkMax(theField);
				break;
				
			case MIN:
				checkMin(theField);
				break;
				
			case ISSAME:
				isSame(theField);
				break;
				
			case ISSORTED:
				isSorted(theField);
				break;
			case NOTHING:
				break;

			}
		}
	}
	
	/**
	 * This method checks if the field name the user inputed to be
	 * terminally observed exists as defined in the header of the .tsv file.
	 * If it doesn't exist, this method makes the program stop. 
	 * 
	 * @return true if the field exists in the header and false if it doesn't.
	 */
	public boolean checkFieldExistence() {
		for (int i = 0; i < variables.length; i++) {
			if (variables[i].getName().equals(theFieldName)) {
				variableIndex = i;
				break;
			}
		}
		// if the variable to be observed does not exist in the header,
		// return false
		if (variableIndex == -1) {
			System.out.println(theFieldName + ErrorMessages.invalidFieldName);
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * This method isolates the field in the given record to 
	 * be terminally observed by this class.  
	 * 
	 * @param theRecord String that holds the record.
	 * @return
	 */
	public String isolateField(String theRecord) {
		int count = 0;
		int startOfField = 0;
		String tempField = "";
		
		// I temporarily add the star to count the last field in the record.
		theRecord += "*";
		for (int i = 0; i < theRecord.length(); i++) {
			if (theRecord.charAt(i) == '*') {
				tempField = theRecord.substring(startOfField, i);
				if (count == variableIndex) {
					return tempField;
				}
				count++;
				startOfField = i + 1;
			}
		}
		return tempField;
		
		
		
	}
	
	/**
	 * Increments the amount of times theFieldName is found
	 * in a given record.
	 */
	public void increment() {
		count++;
	}
	
	/**
	 * This method compares theField with the maxStringSoFar to see if it comes
	 * after it alphabetically. For a number, it compares max with maxNumSofar. The
	 * larger value replaces maxStringSoFar or minNumSoFar (depending on whether
	 * theField is an actual String or a number)
	 * 
	 * @param theField a String that represents the field to be compared.
	 */
	public void checkMax(String theField) {
		if (variables[variableIndex].isString()) {
						
			if (theField.compareTo(maxStringSoFar) > 0) {
				maxStringSoFar = theField;
			}
		}
		else {
			long max = Long.parseLong(theField);
			if (max > maxNumSoFar) {
				maxNumSoFar = max;
			}
		}
		
	}
	
	/**
	 * This method compares theField with the minStringSoFar to see if it comes
	 * before it alphabetically. For a number, it compares min with minNumSofar. The
	 * smaller value replaces minString or minNumSoFar (depending on whether
	 * theField is an actual String or a number)
	 * 
	 * @param theField a String that represents the field to be compared.
	 */
	public void checkMin(String theField) {
		if (variables[variableIndex].isString()) {
			if (theField.compareTo(minStringSoFar) < 0) {
				minStringSoFar = theField;
			}
		}
		else {
			long min = Long.parseLong(theField);
			if (min < minNumSoFar) {
				minNumSoFar = min;
			}
		}
	}
	
	/**
	 * This compares if the previous field was the same. If it is, 
	 * continue as nothing but if it is not, set isSane to false 
	 * as we thus know that records contain different values from 
	 * the specified observation field name.
	 * 
	 * @param theField a String that represents the field to be compared 
	 * to the previous field.
	 */
	public void isSame(String theField) {
		if (isFirstObservedField) {
			isFirstObservedField = false;
		}
		
		// if the current field is not equal to the previous field,
		// set isSame to false.
		else if (!theField.equals(previousField) && isSame) {
			isSame = false; // is set to false for the rest of the program
		}
		
		previousField = theField;
	}
	/**
	 * 
	 * This method compares the current field to the previous record's 
	 * field to see if it is sorted. If it is, continue as nothing but if 
	 * it is not, we set isSorted to false for the rest of the program.
	 * 
	 * @param theField a String that represents the field to be compared
	 * to the previous field.
	 */
	public void isSorted(String theField) {
		if (isFirstObservedField) {
			isFirstObservedField = false;
			previousField = theField;
		}
		
		if (isSorted && variables[variableIndex].isString()) {
			// if theField comes before previousField alphabetically
			// set isSorted to false.
			if (theField.compareTo(previousField) < 0) {
				isSorted = false;
			}
		}
		else if (isSorted && !variables[variableIndex].isString()){
			long theFieldNum = Long.parseLong(theField);
			if (maxNumSoFar > theFieldNum) {
				isSorted = false;
			}
		}
		previousField = theField;
		
		
	}
	
	/**
	 * This method is called when the program finishes looping through
	 * all of the valid lines and it returns the result of the specified
	 * observation pattern.
	 * 
	 * @return a String that represents the result of the observation.
	 */
	public String getResult() {
		
		if (!theFieldName.equals("")) {
			switch (theObservation) {
			case COUNT:
				return ""+count;
			case MAX:
				if (variables[variableIndex].isString()) {
					return maxStringSoFar;
				}
				else {
					return ""+maxNumSoFar;
				}
			case MIN:
				if (variables[variableIndex].isString()) {
					return minStringSoFar;
				}
				else {
					return ""+minNumSoFar;
				}
			case ISSAME:
				return ""+isSame;
			case ISSORTED:
				return ""+isSorted;
			case NOTHING:
				break;

			}
		}

		return null;
	}
	
	public boolean hasValidFieldName() {
		return fieldExistence;
	}
	
	
	
	private String theFieldName;
	private TerminalObservation theObservation;
	private Variable[] variables;
	private boolean fieldExistence;
	private int variableIndex = -1;
	
	private int count;
	
	private String maxStringSoFar = "0"; // 0 has the smallest ASCII value
	private String minStringSoFar = "~"; // ~ has the largest ASCII value
	private long maxNumSoFar = Long.MIN_VALUE;
	private long minNumSoFar = Long.MAX_VALUE;
	
	private boolean isFirstObservedField = true;  
	String previousField;
	private boolean isSame = true;
	private boolean isSorted = true;;
	
}
