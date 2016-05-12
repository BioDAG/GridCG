package postProcessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CommonQueries {
	
	// Prevent instantiation
	private CommonQueries() {}
	public static boolean collapseDir(String directoryPath,String orgFile)
	{
		String defaultOutputPath = Utils.stripExtension(directoryPath) + "collapsed";
		try {
			return collapseDir(directoryPath,defaultOutputPath,orgFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
	
	private static String findHeader(Phylogenetic ph)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("# Summarizing profiles at organism level\n");
		sb.append("# This is the files format:\n protein_name: [ ");
		for(String str : ph.orgs) {
			sb.append(str + " ");
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static boolean collapseDir(String directoryPath,String outputPath,String orgFile) throws Exception
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
		String header = null;
		for (File file : directoryListing) {
			if(Utils.getExtension(file.getName()).equals("phylo")) {
				PhyloCollapser collapser = new PhyloCollapser(file.getAbsolutePath(),orgFile);
				if(header == null) {
					header = findHeader((Phylogenetic)collapser);
				}
				else if(!header.equals(findHeader((Phylogenetic)collapser))) {
					throw new Exception("it seems like the phylogenetic files correspond "
							+ "to different organism sets",new Exception());
				}
				collapser.collapseOrgs();
				list.add(collapser.toString());
			}
		}
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(outputPath);
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
