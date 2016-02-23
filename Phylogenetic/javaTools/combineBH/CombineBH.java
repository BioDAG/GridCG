package javaTools.combineBH;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javaTools.submit.Utils;

public class CombineBH {

	public static void main(String[] args) {

		if(args.length != 2) {
			System.out.println(printUsage());
			System.exit(85);
		}
		
		String BH_directory = args[0];
		String orgFile = args[1];
		combineAll(BH_directory,orgFile);
	}
	
	private static String printUsage()
    {
    	return "Wrong mumber of arguments, \n" +
    			"USAGE: java javaTools/combineBH/CombineBH <BH directory> <organisms.txt>";
    }
    
	private static void test() 
	{
		String BH_directory = "/home/manos/BH_output/";
		String file2 = Utils.appendSlash(BH_directory) + "BAPH-SCH_MGEN-G37.bh";
		String file1 = Utils.appendSlash(BH_directory) + "MGEN-G37_BAPH-SCH.bh";
		BBH test = new BBH(file1,file2);

		test.writeBBH();
		

	}
	private static void combineAll(String BH_directory,String orgFile)
	{
		ArrayList<String> organisms = new ArrayList<>();
		try {
			organisms = Utils.readOrgs(orgFile);
		} catch (FileNotFoundException e) {
			System.out.println(orgFile + " file could not be found!");
			e.printStackTrace();
		}	
		String bh_1,bh_2;
		for(int i=0;i<organisms.size();i++) {
			for(int j=0;j<i;j++) {
				bh_1 = Utils.appendSlash(BH_directory) + organisms.get(i) + "_" + organisms.get(j) + ".bh";
				bh_2 = Utils.appendSlash(BH_directory) + organisms.get(j) + "_" + organisms.get(i) + ".bh";
				if(new File(bh_1).exists() && new File(bh_2).exists()) {
					System.out.println("combining files " + bh_1 + " and " + bh_2);
					BBH bbh = new BBH(bh_1,bh_2);
					bbh.writeBBH();
				}
				//System.out.println(organisms.get(i) + "_" + organisms.get(j)
				//		+ "    " + organisms.get(j) + "_" + organisms.get(i));
				
			}
			
		}
	}
	
}
