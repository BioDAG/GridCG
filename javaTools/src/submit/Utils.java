package submit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class Utils {
	// Should this become a common ancestor instead of Utility pattern? -> more OOP design...
	
	private Utils() {} // Make sure no client can instantiate this class.
	
	public static void delete(File f) throws IOException {
		  if(!f.exists()) 
			  return;
		  if (f.isDirectory()) {
		    for (File c : f.listFiles())
		      delete(c);
		  }
		  if (!f.delete())
		    throw new FileNotFoundException("Failed to delete file: " + f);
	}
	public static ArrayList<String> readOrgs(String orgFile) throws FileNotFoundException
	{
		ArrayList<String> organisms = new ArrayList<>();
		File f = new File(orgFile);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		String identifier;
		try {
			while ((line = br.readLine()) != null) {
				identifier = line.split("\\s+")[1];
				organisms.add(identifier);
			}
		} catch (IOException e) {
			System.out.println("File GenomeAll.txt was found but an IO error occured!");
			e.printStackTrace();
		}
		return organisms;
		
	}
	public static String extractPart(final String input)
	{
		// Attention, local variable filename has nothing to do with the respective field.
		String filename = input.substring(input.lastIndexOf(File.separator),input.length());
	    final StringBuilder sb = new StringBuilder(filename.length());
	    for(int i = 0; i < filename.length(); i++){
	        final char c = filename.charAt(i);
	        if(c > 47 && c < 58){
	            sb.append(c);
	        }
	    }
	    return sb.toString();
    
	}
	
	public static String stripExtension(String str) {
		/* 
		 * This function will return the basename of a file without
		 * its extension 
		*/
		if (str == null) {
			return null;
		}
        int pos = str.lastIndexOf(".");
        if (pos == -1) {
			return str;
		}
        return str.substring(0, pos);
    }
	
	public static String tail( File file ) {
	    RandomAccessFile fileHandler = null;
	    try {
	        fileHandler = new RandomAccessFile( file, "r" );
	        long fileLength = fileHandler.length() - 1;
	        StringBuilder sb = new StringBuilder();

	        for(long filePointer = fileLength; filePointer != -1; filePointer--){
	            fileHandler.seek( filePointer );
	            int readByte = fileHandler.readByte();

	            if( readByte == 0xA ) {
	                if( filePointer == fileLength ) {
	                    continue;
	                }
	                break;

	            } else if( readByte == 0xD ) {
	                if( filePointer == fileLength - 1 ) {
	                    continue;
	                }
	                break;
	            }

	            sb.append( ( char ) readByte );
	        }

	        String lastLine = sb.reverse().toString();
	        return lastLine;
	    } catch( java.io.FileNotFoundException e ) {
	        e.printStackTrace();
	        return null;
	    } catch( java.io.IOException e ) {
	        e.printStackTrace();
	        return null;
	    } finally {
	        if (fileHandler != null )
	            try {
	                fileHandler.close();
	            } catch (IOException e) {
	                /* ignore */
	            }
	    }
	}
	public static String appendSlash(String incompletePath)
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

}
