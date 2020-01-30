/**
 * This class represents a variable in the header. It is often 
 * used to check if individual fields in the record of a .tsv file
 * are of the right format.
 * 
 * This class represents each field and its variable by a String, but 
 * it has the necessary methods to distinguish between Strings and
 * varying number types (byte, int, long, etc.) 
 * 
 * @author Marcus
 *
 */

public class Variable {

	
	public void setVariableName(String inName) {
		variableName = inName;
	}
	
	/**
	 * This method sets defines this object as either a
	 * String or long. 
	 * 
	 * @param isString true if the variable type is a String
	 * and false if it is a long.
	 */
	public void setAsString(boolean inIsString) {
		isString = inIsString;
	}
	
	public void setActualVariableType(String inType) {
		actualVariableType = inType;
	}
	
	/**
	 * Returns the name of the field/variable in the header
	 * 
	 * @return the name of the field/variable in the header.
	 */
	public String getName() {
		return variableName;
	}
	
	/**
	 * Returns the type of the field/variable in the header.
	 * 
	 * @return the type of field/variable in the header (as a String.)
	 */
	public String getActualVariableType() {
		return actualVariableType;
	}
	
	
	/**
	 * 
	 * @return true if the variable type is actually a String 
	 * or false if it is not. 
	 */
	public boolean isString() {
		return isString;
	}
	
	
	
	private String actualVariableType;
	private String variableName;
	private boolean isString;
}
