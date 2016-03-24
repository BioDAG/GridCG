package submit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class OrganismIdAssigner {

    /**
     * @param args String originalfastaFolderPath: An absolute or relative path
     *             to a directory containing fasta files WITHOUT an ID (to be assigned)
     *             
     *             String organismsWithIDFolder:   A path to an existing directory where the output
     *             ( fasta with id assigned ) will be dumped. ATTENTION: Previously existing files in this folder 
     *             will be deleted!
     *             
     *             String listPath: The path to a CSV file listing every fasta file to be assigned. Accepted
     *             extensions are .tfa and .fasta only! I expect the 2nd column of each row to have the exact same 
     *             name with the targeted input file and the 5th column to be the organism ID to be assigned.
     *              
     */
	
    public static void organismsID(String originalfastaFolderPath, String organismsWithIDFolder, String listPath) throws IOException {
        File folder = new File(organismsWithIDFolder);
        boolean status = folder.mkdirs();
        // If output directory already exists, delete all its contents, else create it.
        if(!status) {
        	File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                listOfFiles[i].delete();
            }
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(listPath));
            String line;

            while ((line = br.readLine()) != null) {
                String[] list = line.split(",");
                BufferedReader br1 = null;
                String filepath = originalfastaFolderPath + list[1] + ".fasta";
                File r = new File(filepath);
                if(!r.exists()) {
                	filepath = filepath.replace(".fasta", ".tfa");
                	r = new File(filepath);
                	if(!r.exists()) {
                		System.out.println("Listed file was not found in the input directory");
                		System.out.println(filepath);
                		int errorCode = 80;
                		System.exit(errorCode);
                	}
                } 
                r = null;
                br1 = new BufferedReader(new FileReader(filepath));
                File file = new File(organismsWithIDFolder + list[4].substring(0, list[4].length() - 1) + ".fasta");
                FileWriter fw;
                fw = new FileWriter(file);
                BufferedWriter t1 = new BufferedWriter(fw);
                int count = 1;
                int countProteins = 0;
                while ((line = br1.readLine()) != null) {

                    if (line.contains(">")) {
                        String protId;
                        countProteins++;
                        protId = String.format("%06d", count);
                        line = ">" + list[4].substring(0,list[4].length()-1) +"$"+ protId + "$" + line.substring(1, line.length());
                        count++;
                        t1.write(line);
                        t1.newLine();
                    } else {
                        t1.write(line);
                        t1.newLine();
                    }

                }
                t1.close();
                br1.close();

                int old = Integer.valueOf(list[3]);
                if (old != countProteins) {
                    System.out.println("MISMATCH DETECTED");
                    System.out.println(list[1] + "  WAS   " + list[3] + "   COUNTED   " + countProteins);
                }

            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }

    }
    
    private static String printUsage()
    {
    	return "Wrong mumber of arguments, \n" +
    			"USAGE: OrganismIdAssigner <input_directory> <output_directory> <input list.csv>";
    }
    
    private static String appendSlash(String incompletePath)
    {
    	/*
    	 * This function makes sure a directory path is in the right format ( with trailing '/' character)
    	 * This way the code works with both "path/to/dir" and "path/to/dir/" arguments.
    	 */
    	
    	// File.separator instead of '/' will work on every OS.
    	if(!incompletePath.substring(incompletePath.length() - 1).equals(File.separator)) {
    		return incompletePath + File.separator;
    	}
    	return incompletePath;
    }
    public static void main(String[] args)  {
    	if(args.length != 3) {
    		int errorCode = 85;
    		System.out.println(printUsage());
    		System.exit(errorCode);
    	}
        String inputFolder = appendSlash(args[0]);
        String ouputFolder = appendSlash(args[1]);
        String list = args[2];
     
        try {
			organismsID(inputFolder, ouputFolder, list);
		} catch (IOException e) {
			System.out.println("At least one of the input folders could not be found, \n" +
					"please check the paths.");
			System.out.println("Stack Trace follows:");
			e.printStackTrace();
		}
    }

}
