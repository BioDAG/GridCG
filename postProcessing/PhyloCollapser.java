package postProcessing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhyloCollapser extends Phylogenetic {

	private ArrayList<Integer> profile;
	private ArrayList<Float> scaledProfile;
	private boolean computed;
	
	public PhyloCollapser(String phyloFile,String orgFile) {
		super(phyloFile,orgFile);
		this.profile = new ArrayList<Integer>(orgs.size());
		this.computed = false;
		this.scaledProfile = new ArrayList<Float>(orgs.size());
		for(int i=0;i<orgs.size();i++) {
			profile.add(0);
			scaledProfile.add(0f);
		}
	}
	
	public ArrayList<Integer> getProfile() {
		try
		{
			if(!computed) {
				throw new Exception("the profile has not yet been computed", new Exception());
				//throw new NotComputedException("the profile has not yet been computed");
			}
		}
		catch(Exception ex)
		{
			System.err.println(ex.getMessage());
		}
		return profile;
	}
	public ArrayList<Float> getScaledProfile() {
		try {
			if(!computed) {
				throw new Exception("the profile has not yet been computed", new Exception());
				//throw new NotComputedException("the profile has not yet been computed");
			}
		} catch(Exception ex)	{
			System.err.println(ex.getMessage());
		}
		return scaledProfile;
	}

	public void collapseOrgs()
	{
		this.organism = getOrgName(phyloFile);
		if(computed) {
			System.out.println("It seems like this profile has already been collapsed, im repeating it anyway");
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(phyloFile));
			String line;
			List<Integer> vals = null;
			while ((line = br.readLine()) != null) {
				// If line is a comment or empty, ignore it.
				if(!lineIsValid(line)) {
					continue;
				}
				vals = parseVector(line);
				for(int i=0;i<orgs.size();i++) {
					profile.set(i, profile.get(i) + vals.get(i));
				}
			}
			// Scale output to the number of proteins.
			for(int i=0;i<orgs.size();i++) {
				scaledProfile.set(i, (float)profile.get(i) / numProteins);
			}
		} catch (FileNotFoundException e) {
			System.out.println("file " + phyloFile + " was not found");
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		computed = true;
	}
	
	@Override
	public String toString() {
		if(!computed) return "organism profile has not been computed yet";
		return organism + ": " + scaledProfile;
	}
}
