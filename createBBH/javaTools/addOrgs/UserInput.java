package addOrgs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class UserInput {
	private String oldGenome;
	private String newGenome;
	private String newFastaDir;
	private String oldFastaDir;
	private String oldBlast;
	private String oldBH;
	
	
	public UserInput() {	
	}
	public void setProperties(Properties prop)
	{
		if(prop.isEmpty()) {
			System.out.println("properties file was either empty or not correctly read");
		}
		else {
			this.oldGenome = prop.getProperty("oldGenome");
			this.newGenome = prop.getProperty("newGenome");
			this.newFastaDir = prop.getProperty("newFastaDir");
			this.oldFastaDir = prop.getProperty("oldFastaDir");
			this.oldBlast = prop.getProperty("oldBlast");
			this.oldBH = prop.getProperty("oldBH");
		}
	}
	
	@Override
	public String toString() {
		return "UserInput [\noldGenome=" + oldGenome + ", newGenome=" + newGenome + "\nnewFastaDir=" + newFastaDir
				+ ", oldFastaDir=" + oldFastaDir + "\noldBlast=" + oldBlast + ", oldBH=" + oldBH + "\n]";
	}
	
	public String getOldGenome() {
		return oldGenome;
	}
	
	public String getNewGenome() {
		return newGenome;
	}
	
	public String getNewFastaDir() {
		return newFastaDir;
	}

	public String getOldFastaDir() {
		return oldFastaDir;
	}

	public String getOldBlast() {
		return oldBlast;
	}

	public String getOldBH() {
		return oldBH;
	}
	

}
