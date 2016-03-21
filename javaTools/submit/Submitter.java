package javaTools.submit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

public class Submitter {
	private String databaseFasta;
	private String queryDir;
	private ArrayList<String> queryFasta;
	private String organisms;
	private String VO;
	
	public boolean generateJDLs(String output,boolean isBBH)
	{
		String phyloExtension = (isBBH) ? ".bh" : ".phylo";
		String phyloExecutable = "..blast2phylo.py";
		String gridExecutable = "gridExecutable.sh";
		output = Utils.appendSlash(output);
		System.out.println("Generating jdl files for " + ((isBBH) ? "BBH PHYLO" : "REGULAR PHYLO") + " jobs");
		boolean status = Utils.emptyDirectory(output);
        
        for(String part : this.queryFasta) {
        	//This extraction is needed because File.listFiles() is not guaranteed to return ordered array
        	//String i = Utils.extractPart(part);
        	PrintWriter writer = null;
        	try {
    			writer = new PrintWriter(Utils.appendSlash(output) + part + ".jdl", "UTF-8");
    			writer.println("Type = \"Job\";");
    			writer.println("JobType = \"Normal\";");
    			writer.println("Executable = \"" + gridExecutable + "\";");
    			writer.println("Arguments = \"" + databaseFasta + " " + part + " " + organisms + "\";");
    			// Remove database since it will be downloaded from a SE.
    			//writer.println("InputSandBox = {\"" + gridExecutable + "\",\"../" +databaseFasta +"\",\"../"
				//				+this.queryDir+part +"\",\"" + phyloExecutable + "\",\"../" + organisms + "\"};");
				writer.println("InputSandBox = {\"" + gridExecutable + "\",\"../"
								+this.queryDir+part +"\",\"" + phyloExecutable + "\",\"../" + organisms + "\"};");
    			writer.println("stdOutput = \"std.out\";");
    			writer.println("stdError = \"std.err\";");
    			writer.println("OutputSandbox = {\"std.out\",\"std.err\",\""+Utils.stripExtension(part)+".blast"
								+"\",\""+Utils.stripExtension(part)+phyloExtension+"\"};");
    			writer.println("VirtualOrganisation = \""+VO+"\";");
    			writer.println("Requirements = (other.GlueCEInfoHostName == \"cream01.grid.auth.gr\") ||"
    					+ " (other.GlueCEInfoHostName == \"cream.afroditi.hellasgrid.gr\");");

    		} catch (FileNotFoundException e) {
    			System.out.println("generateJDL failed, output path could not be found");
    			e.printStackTrace();
    			status = false;
    		} catch (UnsupportedEncodingException e) {
    			status = false;
    			e.printStackTrace();
    		} finally {
    			writer.close();
    		}
        }
        
		return status;
	}
	
	public boolean generateJDLs(boolean isBBH)
	{
		// Default jdl directory if no argument is passed.
		String output = "jdl_collection";
		return generateJDLs(output,isBBH);
	}
	
	public String getDatabaseFasta() {
		return databaseFasta;
	}

	public String getQueryDir() {
		return queryDir;
	}	
	
	public String getOrganisms() {
		return organisms;
	}

	public ArrayList<String> getQueryFasta() {
		return queryFasta;
	}

	public Submitter(String databaseFasta, String queryDir, String organisms, String VO) {
		super();
		this.organisms = organisms;
		this.databaseFasta = databaseFasta;
		this.queryDir = Utils.appendSlash(queryDir);
		this.VO = VO;
		File input = new File(queryDir);
		File[] listOfFiles = input.listFiles(); // NOT IN ORDER!
		Arrays.sort(listOfFiles);
		this.queryFasta = new ArrayList<>(listOfFiles.length);
        for (File f : listOfFiles) {
            this.queryFasta.add(f.getName());
        }
	}
	
	
	
	

}
