package stubs;

import security.Definition.FieldSecurity;

public class ProtectedFields {
	
	@FieldSecurity("low")
	protected int lowIField;
	
	@FieldSecurity("high")
	protected int highIField;
	
	@FieldSecurity({"low", "low"})
	protected int[] lowLowIField;
	
	@FieldSecurity({"low", "high"})
	protected int[] lowHighIField; 
	
	@FieldSecurity({"high", "high"})
	protected int[] highHighIField; 
	
	@FieldSecurity({"low", "low", "low"})
	protected int[][] lowLowLowIField;
	
	@FieldSecurity({"low", "low", "high"})
	protected int[][] lowLowHighIField;

	@FieldSecurity({"low", "high", "high"})
	protected int[][] lowHighHighIField;

	@FieldSecurity({"high", "high", "high"})
	protected int[][] highHighHighIField;
	
	@FieldSecurity("low")
	protected static int lowSField;
	
	@FieldSecurity("high")
	protected static int highSField;
	
	@FieldSecurity({"low", "low"})
	protected static int[] lowLowSField;
	
	@FieldSecurity({"low", "high"})
	protected static int[] lowHighSField; 
	
	@FieldSecurity({"high", "high"})
	protected static int[] highHighSField; 
	
	@FieldSecurity({"low", "low", "low"})
	protected static int[][] lowLowLowSField;
	
	@FieldSecurity({"low", "low", "high"})
	protected static int[][] lowLowHighSField;

	@FieldSecurity({"low", "high", "high"})
	protected static int[][] lowHighHighSField;

	@FieldSecurity({"high", "high", "high"})
	protected static int[][] highHighHighSField;

}
