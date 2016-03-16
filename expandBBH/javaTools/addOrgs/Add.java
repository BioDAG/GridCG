package addOrgs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Add {

	public static void main(String[] args) {
		
		String properties = null;
		if(args.length == 0) {
			String defaultProperties = "/home/manos/workspace/expandBBH/input.properties";
			if(!fileExists(defaultProperties)) {
				System.out.println("No argument was passed and default properties file does not exist!");
				System.exit(5);
			}
			properties = defaultProperties;
		}
		else if(args.length == 1) {
			if(!fileExists(args[0])) {
				System.out.println("the argument supplied is not an existing properties file");
				System.exit(5);
			}
			properties = args[0];
		}
		
		UserInput input = new UserInput();
		readInput(input,properties);
		System.out.println(input);

	}
	private static boolean fileExists(String path)
	{
		File f = new File(path);
		return (f.exists() && !f.isDirectory()); 
		
	}
	private static String usage()
	{
		return "USAGE: addOrgs/Add input.properties";
	}
	
	private static void readInput(UserInput input,String properties)
	{
		Properties prop = new Properties();
		InputStream in = null;
		UserInput userInput = null;

		try {
			in = new FileInputStream(properties);

			// load a properties file
			prop.load(in);
			input.setProperties(prop);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}


}
