package stubs;

import security.Definition.FieldSecurity;

public class Fields {

	@FieldSecurity("low")
	public int lowIField;

	@FieldSecurity("low")
	public static int lowSField;

	@FieldSecurity("high")
	public int highIField;

	@FieldSecurity("high")
	public static int highSField;
	
	@FieldSecurity({"low", "low"})
	public int[] lowLowIField;
	
	@FieldSecurity({"low", "high"})
	public int[] lowHighIField; 
	
	@FieldSecurity({"high", "high"})
	public int[] highHighIField; 
	
	@FieldSecurity({"low", "low", "low"})
	public int[][] lowLowLowIField;
	
	@FieldSecurity({"low", "low", "high"})
	public int[][] lowLowHighIField;

	@FieldSecurity({"low", "high", "high"})
	public int[][] lowHighHighIField;

	@FieldSecurity({"high", "high", "high"})
	public int[][] highHighHighIField;
	
	@FieldSecurity({"low", "low"})
	public static int[] lowLowSField;
	
	@FieldSecurity({"low", "high"})
	public static int[] lowHighSField; 
	
	@FieldSecurity({"high", "high"})
	public static int[] highHighSField; 
	
	@FieldSecurity({"low", "low", "low"})
	public static int[][] lowLowLowSField;
	
	@FieldSecurity({"low", "low", "high"})
	public static int[][] lowLowHighSField;

	@FieldSecurity({"low", "high", "high"})
	public static int[][] lowHighHighSField;

	@FieldSecurity({"high", "high", "high"})
	public static int[][] highHighHighSField;

}
