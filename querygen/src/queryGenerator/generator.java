package queryGenerator;

import java.io.*;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import java.io.*;
import java.util.StringTokenizer;

// This module works for input query template and output query instances
// queryInstantiation will be called from here
public class generator {

	// TODO Auto-generated constructor stub
	protected static int numQueriesPerTempate = generatorDefaultValues.numQueriesPerTempateDef;
	//protected static File queryTemplate = generatorDefaultValues.queryTemplateFile;// where to take the queries from
	protected static String placeholderValDir= generatorDefaultValues.placeholderValDirDef;
	protected static String queryTemplateDir = generatorDefaultValues.queryTemplateDirDef;
	protected static String queryInstanceDir = generatorDefaultValues.queryInstanceDirDef;
	static ArrayList<String> templates = new ArrayList<String>();
	/*
	 * Parameters for steady state
	 */

	protected boolean rampup = false;
	
	public generator(String[] args) {
		processProgramParameters(args);
		System.out.print("Reading query template data...");
		System.out.println("done");
	}
	
	 //Process the program parameters typed on the command line.
	 
	protected void processProgramParameters(String[] args) {
		int i = 0;
		while (i < args.length) {
			try {
				if (args[i].equals("-nm")) {
					numQueriesPerTempate = Integer.parseInt(args[i++ + 1]);
				} else if (args[i].equals("-idir")) {
					placeholderValDir = args[i++ + 1];
				} else if (args[i].equals("-temp")) {
					queryTemplateDir = args[i++ + 1];
				} else if (args[i].equals("-oq")) {
					queryInstanceDir = args[i++ + 1];
				} else {
					if (!args[i].equals("-help"))
						System.err.println("Unknown parameter: " + args[i]);
					printUsageInfos();
					System.exit(-1);
				}

				i++;

			} catch (Exception e) {
				System.err.println("Invalid arguments:\n");
				e.printStackTrace();
				printUsageInfos();
				System.exit(-1);
			}
		}
	}
	
	//print command line options

	protected void printUsageInfos() {
		String output = "Usage: java benchmark.queryGenerator <options> GraphQL\n\n"
				+ "Possible options are:\n"
				+ "\t-nm <specify the number of query instances per template>\n" + "\t\tdefault: "
				+ generatorDefaultValues.numQueriesPerTempateDef
				+ "\n"
				+ "\t-idir <data input directory>\n"
				+ "\t\tThe input directory for the possible values for placeholders of template\n"
				+ "\t\tdefault: "
				+ generatorDefaultValues.placeholderValDirDef
				+ "\n"
				+ "\t-temp <query template files>\n"
				+ "\t\tdefault: "
				+ generatorDefaultValues.queryTemplateDirDef
				+ "\n"
				+ "\t-oq <output query instances>\n"
				+ "\t\tdefault: "
				+ generatorDefaultValues.queryInstanceDirDef
				+ "\n";

		System.out.print(output);
	}
	
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	        		f.delete();
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	}
	
	static String[] parameters={"$offerID", 
			"$producerID", "$reviewID", 
			"$offerID", "$productID", 
			"$vendorID", "$vendorID-$offset", 
			"$cnt-$attrOffer1-$attrOffer2", 
			"$vendorID-$attrReview", "$keyword", 
			"$vendorID", "$producerID-$vendorID", 
			"$producerID-$date", 
			"$producerID-$date-$keyword", 
			"$vendorID", "$vendorID"};
	

	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		generator generator = new generator(args);
		
		//generator.init();
		System.out.println("\nStarting generating queries...\n");
		
		//Find possible values for the placeholders
		File resourceDir = new File(placeholderValDir);
		
		//read query template, and store it as string
		File dir = new File(queryTemplateDir);
		File[] directoryListing = dir.listFiles();
		if(directoryListing != null){
			for (int i = 1; i<= 16; i++){
				File queryinstance = new File(dir, "template"+i+".txt");
				FileInputStream is = new FileInputStream(queryinstance);
				BufferedReader tsvFile = new BufferedReader(new InputStreamReader(is));
				String line = tsvFile.readLine(); 
				StringBuilder sb = new StringBuilder();
				while(line != null){ 
					sb.append(line).append("\n"); 
					line = tsvFile.readLine(); 
				} 
				String queryTemp = sb.toString();
				templates.add(queryTemp);
			}
		}
		
		File dirTem = new File(queryInstanceDir);
		deleteFolder(dirTem);
		
		for(int i=0; i< parameters.length; i++){
			String queryTemp = templates.get(i);
			String placeholder = parameters[i];
			System.out.println(i+"queryTemp:"+queryTemp);
			System.out.print(i+"placeholder:"+placeholder);
			queryInstantiation instances = new queryInstantiation(queryTemp, placeholder, resourceDir, dirTem, numQueriesPerTempate, (i+1));
		}
	}
}
