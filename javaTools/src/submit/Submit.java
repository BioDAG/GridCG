package submit;

import java.io.IOException;

public class Submit {

	public static void main(String[] args) throws IOException {
		
		if(args.length != 6) {
			System.out.println(printUsage());
			System.exit(85);
		}
		
		String fastaDB = args[0];
		String fastaDir = args[1];
		String organisms = args[2];
		String VO = args[3];
        	Boolean isBBH = Boolean.valueOf(args[4]);
		String jdl_collection = args[5]+"/jdl_collection";

		File file=new File(args[5]);
		System.setProperty("user.dir", file.getAbsolutePath());

		Submitter submitter = new Submitter(fastaDB, fastaDir, organisms,VO);
		
		boolean status = submitter.generateJDLs(jdl_collection,isBBH);
	

	}
    private static String printUsage()
    {
    	return "Wrong mumber of arguments, \n" +
    			"USAGE: java javaTools/submit/Submit <database.fasta> <query_directory> <organisms.txt> <VO> <isBBH> <session_dir>";
    }

}
