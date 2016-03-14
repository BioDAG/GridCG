package postProcessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import postProcessing.Utils;

public class PhyloFilter extends Phylogenetic{
	private String outputPath;

	public PhyloFilter(int numOrgs,String filePath,String outputPath) {
		// TODO Auto-generated constructor stub
		super(numOrgs,filePath);
		this.outputPath = outputPath;
	}

	public PhyloFilter(int numOrgs,String filePath) {
		// Default value for outputPath used (append _filtered to input filename).
		super(numOrgs,filePath);
		this.outputPath = Utils.stripExtension(filePath) + "_filtered.phylo";
	}

	// Only write lines with more than <min> hits.
	public boolean minimumHits(int min, boolean ignoreSelf)
	{
		boolean status = true;
		PrintWriter pw = null;
		BufferedReader br = null;
		if(ignoreSelf) myIndex = myIndex();
		try {
			br = new BufferedReader(new FileReader(filePath));
			pw = new PrintWriter(outputPath);
			String line;
			List<Integer> vals = null;
			String header = "# Filtering lines with less than " + min + " hits";
			if(ignoreSelf) {
				header = header + " (ignoring hits with myself)";
			}
			else {
				header = header + " including hits with myself";
			}
			pw.println(header);
			while ((line = br.readLine()) != null) {
				// If line is a comment or empty, ignore it.
				if(lineIsValid(line)) {
					vals = parseVector(line);
					if(sum(vals,ignoreSelf) > min) {
						pw.println(line);
					}
				}
				else {
					pw.println(line);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			status = false;
		} finally {
			pw.close();
		}


		return status;
	}

	private int sum(List<Integer> list, boolean ignoreSelf){
		if(list == null || list.size()<1) return 0;
		int sum = 0;
		if(ignoreSelf) {
			for(int i=0;i<list.size();i++) {
				if(ignoreSelf && i == myIndex)
					continue;
				sum += list.get(i);
			}
		}
		else {
			for(int i : list) sum+=i;
		}
		return sum;
	}
}
