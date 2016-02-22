package javaTools.submit;

public class Submit {

	public static void main(String[] args) {
		
		if(args.length != 4) {
			System.out.println(printUsage());
			System.exit(85);
		}
		
		String fastaDB = args[0];
		String fastaDir = args[1];
		String organisms = args[2];
		String VO = args[3];
		Submitter submitter = new Submitter(fastaDB, fastaDir, organisms,VO);
		
		// ATTENTION on this line (hardcoded important parameter)
		boolean isBBH = true;
		boolean status = submitter.generateJDLs(isBBH);
	

	}
    private static String printUsage()
    {
    	return "Wrong mumber of arguments, \n" +
    			"USAGE: java javaTools/submit/Submit <database.fasta> <query_directory> <organisms.txt> <VO>";
    }

}
