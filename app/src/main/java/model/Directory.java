package model;

public class Directory {

	private String fileName;


	public String getFilePath() {
		return fileName;
	}

	public void setFilePath(String fileName) {
		this.fileName = fileName;
	}

	public String getDirectoryName() {
		return directoryName;
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}

	
	

	public long getPos() {
		return pos;
	}

	public void setPos(long pos) {
		this.pos = pos;
	}




	private String directoryName;
	
	private long  pos;
	
	
}
