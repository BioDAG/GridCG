package blastUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class FastaSplitter extends FastaExaminator {
	
	public FastaSplitter(String filename)
	{
		super(filename);
	}
	
	public boolean split(int seqPerChunk)
	{
		if(seqPerChunk < 2) {
			System.out.println("Arg must be at least 2");
			return false;
		}
		String outputPath = this.filename.substring(0, this.filename.indexOf('.')); 
        boolean status = new File(outputPath).mkdirs();
        try {
	        BufferedReader br = new BufferedReader(new FileReader(this.filename));
	        String outputFile = outputPath + "/part1";
	        File partFile = new File(outputFile);
            BufferedWriter bw = new BufferedWriter(new FileWriter(partFile));
			String line;
			int sequenceCounter = -1;
	        while ((line = br.readLine()) != null) {  
	        	if (line.contains(">")) {
	        		sequenceCounter++;
	        		if(sequenceCounter == seqPerChunk) {
	        			// Time to create next part
	        			bw.close();
		        		String part = Utils.extractPart(outputFile);
		        		outputFile = outputFile.substring(0,outputFile.length() - part.length()) + (Integer.valueOf(part) + 1);        		
		        		bw = new BufferedWriter(new FileWriter(new File(outputFile)));
		        		sequenceCounter = 0;
	        		}	        		
	        	}
	        	bw.write(line);
	        	bw.newLine();
	        }
	        br.close();
	        bw.close();
        } catch(IOException e) {
        	System.out.println("IO exception in split()");
        	return false;
        }
		return status;
	}
	
}
