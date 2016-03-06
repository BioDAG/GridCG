package blastUtils;

import java.io.File;
import java.util.ArrayList;

public class FastaDirExaminator {
	private ArrayList<FastaExaminator> fastaList;
	private String directory;
	private int numFiles;
	
	public ArrayList<FastaExaminator> getFastaList() {
		return fastaList;
	}

	public String getDirectory() {
		return directory;
	}

	public int getNumFiles() {
		return numFiles;
	}
	public void examineAll()
	{
		for(FastaExaminator fe : fastaList) {
			fe.examine();
			System.out.println(fe);
		}
	}
	public FastaDirExaminator(String directory) {
		super();
		this.directory = directory;
		this.fastaList = new ArrayList<>();
		File tempFile = new File(directory);
		if(!tempFile.exists()) {
			System.out.println("Directory " + directory + " was not found");
			int errorCode = 80;
			System.exit(errorCode);
		}
		File[] files = new File(directory).listFiles();
		for(File file : files){
			  if(file.isFile()){
				  this.fastaList.add( new FastaExaminator(file.getAbsolutePath()) );		    
			  }
			}
		this.numFiles = fastaList.size();
		

	}

	@Override
	public String toString() {
		return "FastaDirExaminator [directory=" + directory + ", numFiles="
				+ numFiles + "]";
	}
	

	
}
