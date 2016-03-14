package postProcessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CommonQueries {
	
	// Prevent instantiation
	private CommonQueries() {}
	public static boolean collapseDir(String directoryPath)
	{
		String defaultOutputPath = Utils.stripExtension(directoryPath) + "collapsed";
		return collapseDir(directoryPath,defaultOutputPath);

	}
	public static boolean collapseDir(String directoryPath,String outputPath)
	{
		boolean status = false;
		List<String> list = new ArrayList<String>();
		File dir = new File(directoryPath);
		try	{
			if(!dir.isDirectory()) {
				throw new Exception("The specified path: " + directoryPath + " is not a directory!", new Exception());
			}
		} catch(Exception ex) {
			System.err.println(ex.getMessage());
		}

		File[] directoryListing = dir.listFiles();
		for (File file : directoryListing) {
			if(Utils.getExtension(file.getName()).equals("phylo")) {
				PhyloCollapser collapser = new PhyloCollapser(file.getAbsolutePath());
				collapser.collapseOrgs();
				list.add(collapser.toString());
			}
		}
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(outputPath);
			String header = "# Summarizing profiles at organism level";
			pw.println(header);
			pw.println("");
			for(String str : list) {
				pw.println(str);
			}
			status = true;
		} catch (FileNotFoundException e) {
			System.err.println("Could not write to file " + outputPath + ", check its path and/or privileges");
			status = false;
		} finally {
			pw.close();
		}
		
		return status;
	}
	
}
