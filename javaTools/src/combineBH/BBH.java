package combineBH;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class BBH {
	private String file1;
	private String file2;
	private String[] organisms;
	private String directory;
	private boolean done;
	ArrayList<Integer> referenceList;
	ArrayList<Integer> checkList;
	
	public String getFile1() {
		return file1;
	}

	public String getFile2() {
		return file2;
	}

	public boolean isDone() {
		return done;
	}

	public BBH(String file1, String file2) {
		super();
		this.file1 = file1;
		this.file2 = file2;
		this.done = false;
		try {
			this.referenceList = new ArrayList<Integer>(Collections.nCopies(sizeNeeded(file1), -1));
			this.checkList = new ArrayList<Integer>(Collections.nCopies(sizeNeeded(file2), -1));
		} catch (IOException e) {
			// fatal error if file isn't found.
			e.printStackTrace();
		}
		
		boolean status = findDir();
		if(!status) {
			System.out.println("input files reside in different directories...?!");
		}
		findOrgs();
	}
	
	private int sizeNeeded(String filename) throws IOException
	{
		String lastLine = Utils.tail(new File(filename));
		String leftColumn = lastLine.split("\\s+")[0];
		return getNumber(leftColumn) + 1;
	}
	
	private void loadToMemory()
	{
		loadFile(this.file1,this.referenceList);
		loadFile(this.file2,this.checkList);
	}
	
	private void loadFile(String filename,ArrayList<Integer> list)
	{
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			System.out.println("This exception has been handled by the constructor, "
					+ "so i should never see this message!");
		}
		String line;		
		String[] proteins = new String[2];
        try {
			while ((line = br.readLine()) != null) {
				proteins = line.split("\\s+");
				list.set(getNumber(proteins[0]),getNumber(proteins[1]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void checkBidirectional() 
	{
		loadToMemory();
		for(int i=0;i<this.referenceList.size();i++) {
			if(referenceList.get(i) < 0) {
				continue;
			}
			try {
				if(checkList.get(referenceList.get(i)) != i) {
					referenceList.set(i, -1);
				}
			} catch(IndexOutOfBoundsException e) {
				continue;
			}
		}
		done = true;
	}
	public boolean writeBBH() 
	// Overload this function for default output Directory!
	{
		checkBidirectional();
		if(!done) {
			System.out.println("checkBidirectional should run first to exclude one-side matches");
			return false;
		}
		File outputDirectory = new File(this.directory.substring(0,this.directory.lastIndexOf(File.separator)+1) + "BBH");
		if(outputDirectory.exists()) {
			try {
				return writeBBH(outputDirectory.getCanonicalPath());
			} catch (FileNotFoundException e) {
				System.out.println("i just checked that this exists, so i should never see this message");
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		outputDirectory.mkdir();
		try {
			return writeBBH(outputDirectory.getCanonicalPath());
		} catch (FileNotFoundException e) {
			System.out.println("i just created this dir, so i should never see this message");
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	public boolean writeBBH(String outputDirectory)
	{
		if(!done) {
			// Call everything myself if invoked directly.
			checkBidirectional();
		}
		
		if(!done) {
			return done;
		}
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(Utils.appendSlash(outputDirectory)
					+ organisms[0] + "_" + organisms[1] + ".bbh", "UTF-8");
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			//If UTF-8 is not supported use Windows default.
			try {
				writer = new PrintWriter(outputDirectory 
						+ organisms[0] + "_" + organisms[1] + ".bbh");
			} catch (FileNotFoundException e1) {
				// This is safe to ignore, im never getting here anyway.
			}
		}
		String line;
		for(int i=0;i<referenceList.size();i++) {
			int val = referenceList.get(i);
			if(val < 0) {
				continue;
			}
			line = organisms[0] + "-" + String.format("%05d", i) 
				 + "\t" + organisms[1] + "-" + String.format("%05d", val);
			writer.println(line);
		}
		writer.close();
		return done;
	}
	private Integer getNumber(String protein)
	{
		String lastPart = protein.substring(protein.lastIndexOf("-")+1);
		int number = Integer.parseInt(lastPart);
		return number;

	}
	private void findOrgs()
	{
		String temp = file1.substring(file1.lastIndexOf(File.separator)+1,file1.indexOf("."));
		this.organisms = temp.split("_");
	}
	private boolean findDir()
	{
		//Make sure i dont include the trailing slash ('/')
		this.directory = file1.substring(0, file1.lastIndexOf(File.separator));
		if(file2.substring(0, file1.lastIndexOf(File.separator)).equals(this.directory)) {
			return true;
		}
		return false;
	}

	

}
