package model;

/**
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class ExtendedHeadingInformation extends MinimalHeadingInformation {
	
	/** */
	private long srcLn = Long.MIN_VALUE;
	/** */
	private String fileName = null;
	
	/**
	 * 
	 * @param tabs
	 * @param srcLn
	 * @param fileName
	 */
	public ExtendedHeadingInformation(int tabs, long srcLn, String fileName) {
		super(tabs);
		this.srcLn = srcLn;
		this.fileName = fileName;
	}

	/**
	 * 
	 * @return
	 */
	public long getSrcLn() {
		return srcLn;
	}

	/**
	 * 
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}
	
}
