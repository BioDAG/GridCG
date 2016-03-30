package submit;

import java.io.File;
import java.io.IOException;
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
	
	public boolean generateJDLs(String output,boolean isBBH) throws IOException
	{
		String phyloExtension = (isBBH) ? ".bh" : ".phylo";
		String scriptFolder = "../";
		String phyloExecutable = "blast2phylo.py";
		String gridExecutable = "gridExecutable.sh";

		output = Utils.appendSlash(output);
		System.out.println(output);
		File f = new File(output);
		if(!f.exists()) {
			f.mkdirs();
		}
		if(f.isDirectory() && (f.list().length != 0)) {
			System.out.println("jdl file output file " + output + " is not empty, exiting");
			System.exit(1);		
		}
		boolean status = true;
        
        for(String part : this.queryFasta) {
        	//This extraction is needed because File.listFiles() is not guaranteed to return ordered array
        	//String i = Utils.extractPart(part);
        	PrintWriter writer = null;
        	try {
    			writer = new PrintWriter(Utils.appendSlash(output) + part + ".jdl", "UTF-8");
    			writer.println("Type = \"Job\";");
    			writer.println("JobType = \"Normal\";");
    			writer.println("Executable = \"" + gridExecutable + "\";");
    			writer.println("Arguments = \"" + databaseFasta + " " + part + " " + organisms + " " + isBBH + "\";");
			writer.println("InputSandBox = {\"" + scriptFolder +  gridExecutable + "\",\""
					+this.queryDir+part +"\",\"" + scriptFolder + phyloExecutable + "\",\"" + organisms + "\"};");
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
	
	public boolean generateJDLs(boolean isBBH) throws IOException
	{
		// Default jdl directory if no argument is passed.
		String output = "/home/steremma/Thesis/jdl_collection";
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
		this.organisms = "../" + organisms;
		this.databaseFasta = databaseFasta;
		this.queryDir = "../" + Utils.appendSlash(queryDir);
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
