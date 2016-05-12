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
	protected List<String> orgs;
	protected int numProteins;
	protected String organism;
	protected String phyloFile;
	protected String orgFile;
	protected int myIndex;
	
	// I include much logic inside the constructor to make sure my object is fully functional.
	// No calculation is meaningful with a semi-constructed object of this type.
	public Phylogenetic(String phyloFile,String orgFile)
	{		
		this.phyloFile = phyloFile;
		this.orgFile = orgFile;
		this.numProteins = getNumProteins();
		this.myIndex = -1;
		this.organism = getOrgName(phyloFile);
		this.orgs = findOrgs(orgFile);
	}
	public Phylogenetic(String phyloFile)
	{
		this.phyloFile = phyloFile;
		this.numProteins = getNumProteins();
		this.myIndex = -1;
	}
	
	private List<String> findOrgs(String orgFile)
	{
		List<String> orgs = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(orgFile));
			String line; 
			while ((line = br.readLine()) != null) {
				if(line.startsWith("#") || line.isEmpty()) {
					continue;
				}

				orgs.add(line.split("\\s+")[1].trim());
			}
			br.close();
			return orgs;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	public int getNumProteins() {
		if(numProteins == 0) {
			try {
				return countLines(phyloFile);
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
			br = new BufferedReader(new FileReader(phyloFile));
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
	
	private int countLines(String filename) throws IOException 
	{
		BufferedReader br = new BufferedReader(new FileReader(phyloFile));
		int count = 0;
		String line;
		while ((line = br.readLine()) != null) {
			// If line is a comment or empty, ignore it.
			if(lineIsValid(line)) {
				count++;
			}
		}
		return count;
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
