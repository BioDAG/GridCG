package blastUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FastaExaminator {
	// Immutable class.
	protected String filename; 
	protected String organism;
	protected int numSequences;
	protected double avgCharsPerSeq;
	protected boolean examined;
	
	@Override public String toString() {
		if(examined) {
			String result = "There are " + numSequences + " sequences in organism: " + organism + ".\n" +
					"Each of these sequences contains " + (int)avgCharsPerSeq + " characters on average.";
			
			return result;
		}
		else {
			return "The file " + filename + " has not been examined yet (call examine on this object)";
		}

	}
	public void examine()
	{
		// This and only this method can set all class fields.
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.filename));
			String line;
			int sequenceCounter = 0;
			int charCounter = 0;
			ArrayList<Integer> numCharsPerSeq = new ArrayList<Integer>();
            while ((line = br.readLine()) != null) {
            	if (line.contains(">")) {
            		sequenceCounter++;
            		numCharsPerSeq.add(charCounter);
            		charCounter = 0;
            	}
            	else {
            		charCounter += line.length();
            	}
            }
            br.close();
            this.avgCharsPerSeq = calculateAverage(numCharsPerSeq);
            this.numSequences = sequenceCounter;
            this.examined = true;   
		} catch (IOException e) {
			System.out.println("File " + filename + "does not exist");
			int errorCode = 85;
			System.exit(errorCode);
		} 
		
	}
	
	private double calculateAverage(List <Integer> list) 
	{
		  Integer sum = 0;
		  if(!list.isEmpty()) {
		    for (Integer number : list) {
		        sum += number;
		    }
		    return sum.doubleValue() / list.size();
		  }
		  return sum;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getOrganism() {
		return organism;
	}
	
	public int getNumSequences() {
		return numSequences;
	}

	public double getAvgCharsPerSeq() {
		return avgCharsPerSeq;
	}
	
	public boolean isExamined() {
		return examined;
	}

	public FastaExaminator(String filename, int numSequences,
			double avgCharsPerSeq, double time) {
		super();
		
		this.filename = filename;
		this.numSequences = numSequences;
		this.avgCharsPerSeq = avgCharsPerSeq;
		this.examined = false;
		extractOrganism(filename);
	}

	public FastaExaminator(String filename) {
		super();
		this.filename = filename;
		extractOrganism(filename);
		this.examined = false;	
	}
	
	private void extractOrganism(String filename) 
	{
		String organism = filename.substring(filename.lastIndexOf(File.separator) + 1,filename.lastIndexOf('.'));
		this.organism = organism;
	}
}
