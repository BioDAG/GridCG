package postProcessing;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Phylogenetic {

	// I dont really care about security since im the only developer, make common variables protected.
	protected int numOrgs;
	protected int numProteins;
	protected String organism;
	protected String filePath;
	protected int myIndex;
	public Phylogenetic(int numOrgs,String filePath)
	{	
		this.numOrgs = numOrgs;	
		init(filePath);			
	}
	public Phylogenetic(String filePath)
	{
		this.numOrgs = findNumOrgs(filePath);
		init(filePath);
	}
	private void init(String filePath)
	{
		if(numOrgs < 1) { 
			System.err.println("Must have at least 1 organism, exiting program");
			System.exit(1);
		}
		this.filePath = filePath;
		this.numProteins = 0;
		this.myIndex = -1;
		this.organism = getOrgName(filePath);
	}
	private int findNumOrgs(String filePath)
	{
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			String line; 
			while ((line = br.readLine()) != null) {
				if(line.startsWith("#") && line.contains("[")) {
					break;
				}
			}
			br.close();
			return strVector(line).size();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public int getNumOrgs() {
		return numOrgs;
	}

	public int getNumProteins() {
		if(numProteins == 0) {
			try {
				return countLines(filePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return numProteins;
	}
	
	protected int myIndex()
	{
		// Find the comment line reporting the format.
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			String line;
			while ((line = br.readLine()) != null) {
				// If line is a comment or empty, ignore it.
				if(line.startsWith("#") && line.contains("[")) {
					break;
				}
			}
			List<String> vec = strVector(line);
			for(String organism : vec) {
				//System.out.println("i am " + this.organism + " and i see " + organism);
				if(this.organism.equals(organism)) return vec.indexOf(organism);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public int countLines(String filename) throws IOException 
	{
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}

	// Parse my custom line to extract the organisms identifier.
	protected String getOrgName(String filePath) 
	{
		BufferedReader br = null;
		String organism = null;
			try {
				br = new BufferedReader(new FileReader(filePath));
				String line;
				while ((line = br.readLine()) != null) {
					// If line is a comment or empty, ignore it.
					if(!lineIsValid(line)) {
						continue;
					}
					String[] parts = line.split("-");
					organism = parts[0] + "-" + parts[1];
					br.close();
					return organism;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		
	}
	
	// Return true if this is an actual information line, i.e not a commend or blank.
	protected boolean lineIsValid(String line)
	{
		return !(line.startsWith("#") || line.trim().isEmpty());
	}
	// A parser for my custom output format.
	protected List<Integer> parseVector(String line) 
	{
		List<String> stringParts = strVector(line);
		List<Integer> intParts = new ArrayList<Integer>(stringParts.size());
		for(String str : stringParts) {
			intParts.add(Integer.parseInt(str));
		}
		return intParts;
	}
	private List<String> strVector(String line)
	{
		String[] stringParts = line.split("\\[")[1].split("\\]")[0].trim().split("\\s+");
		return Arrays.asList(stringParts);
	}

}
