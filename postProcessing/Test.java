package postProcessing;

public class Test {

	public static void main(String[] args)
	{
		/*
		if(args.length != 3) {
			System.out.println(usage());
			System.exit(85);
		}	
		PhyloFilter filter = new PhyloFilter(5,filename);
		boolean ignoreHitsWithMyself = true;
		filter.minimumHits(100,ignoreHitsWithMyself);
		*/
		String directoryPath = "/home/manos/PhyloOutput/";
		String outputPath = "/home/manos/PhyloOutput/collapsed";
		String orgPath = "/home/manos/PhyloOutput/GenomeAll";
		boolean status = CommonQueries.collapseDir(directoryPath,orgPath);
		String report = (status) ? "Operation was succesful, check the output" : "Operation failed";
		System.out.println(report);
	}
	private static String usage()
	{
		return "fix me!";
	}


}

