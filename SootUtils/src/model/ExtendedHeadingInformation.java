package model;

/**
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class ExtendedHeadingInformation extends MinimalHeadingInformation {
	
	/** */
	private long sourceLine = Long.MIN_VALUE;
	/** */
	private String fileName = null;
	
	/**
	 * 
	 * @param tabs
	 * @param sourceLine
	 * @param fileName
	 */
	public ExtendedHeadingInformation(int tabs, long sourceLine, String fileName) {
		super(tabs);
		this.sourceLine = sourceLine;
		this.fileName = fileName;
	}

	/**
	 * 
	 * @return
	 */
	public long getSourceLine() {
		return sourceLine;
	}

	/**
	 * 
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}
	
}
