/**
 * This class represents the filter and terminal observation
 * specified by the user. It uses a builder pattern to create 
 * the filter. 
 * 
 * 
 * @author Marcus
 *
 */
public class TSVFilter {

	private TSVFilter (WhichFile inWhichFile) {
		theFileName = inWhichFile.getFileName();
		theFieldName = inWhichFile.getFieldName();
		theField = inWhichFile.getField();
		terminalFieldName = inWhichFile.getTerminalFieldName();
		theObservation = inWhichFile.getObservation();
	}
	
	/**
	 * 
	 * This is the builder class.
	 * 
	 * @author Marcus
	 *
	 */
	public static class WhichFile{
		
		public WhichFile(String inFileName) {
			theFileName = inFileName;
		}
		/**
		 * 
		 * This specifies the filter for the .tsv file.
		 * 
		 * @param inFieldName a String that represents the filter
		 * field name.
		 * @param inField a String that represents the actual value
		 * of the field.
		 *  
		 * @return a reference to this (to be built).
		 */
		public WhichFile select(String inFieldName, String inField) {
			theFieldName = inFieldName;
			theField = inField;
	
			return this;
		}
		
		/**
		 * 
		 * This specifies the terminal observation for the .tsv file.
		 * 
		 * @param inFieldName a String that represents the field name
		 * to be observed.
		 * @param inObservation is type TerminalObservation that helps
		 * the compiler distinguish between different observation patterns.
		 *  
		 * @return a reference to this (to be built).
		 */
		public WhichFile terminate(String inFieldName, TerminalObservation inObservation) {
			terminalFieldName = inFieldName;
			theObservation = inObservation;
			
			return this;
		}
		
		
		public String getFileName() {
			return theFileName;
		}
		
		public String getFieldName() {
			return theFieldName;
		}
		
		public String getField() {
			return theField;
		}
		
		public String getTerminalFieldName() {
			return terminalFieldName;
		}
		
		public TerminalObservation getObservation() {
			return theObservation;
		}
		

		public TSVFilter done() {
			return new TSVFilter(this);
		}
		
		
		private String theFileName;
		private String theFieldName;
		private String theField;
		
		private String terminalFieldName;
		private TerminalObservation theObservation;
		
		
	}
	
	public String getFileName() {
		return theFileName;
	}
	
	public String getFieldName() {
		return theFieldName;
	}
	
	public String getField() {
		return theField;
	}
	
	public String getTerminalFieldName() {
		return terminalFieldName;
	}
	
	public TerminalObservation getObservation() {
		return theObservation;
	}
	
	
	public String toString() {
		return "File name: " + theFileName
				+"\nThe Filtered Field Name: " + theFieldName
				+"\nThe Filtered Field Value: " + theField
				+"\n\nThe Terminal Field Name: " + terminalFieldName
				+"\nThe Terminal Observation: " + theObservation;
	}
	
	
	private String theFileName;
	private String theFieldName;
	private String theField;
	
	private String terminalFieldName;
	private TerminalObservation theObservation;

}
