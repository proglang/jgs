package model;

public class ExtendedHeadingInformation extends MinimalHeadingInformation {
	
	
	private long sourceLine = Long.MIN_VALUE;
	private String fileName = null;
	
	public ExtendedHeadingInformation(int tabs, long sourceLine, String fileName) {
		super(tabs);
		this.sourceLine = sourceLine;
		this.fileName = fileName;
	}

	public long getSourceLine() {
		return sourceLine;
	}

	public String getFileName() {
		return fileName;
	}
	
}
