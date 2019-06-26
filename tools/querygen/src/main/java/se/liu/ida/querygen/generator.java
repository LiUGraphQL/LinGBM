package se.liu.ida.querygen;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Random;

// This module works for input query templates and output query instances
// queryInstantiation is called from here
public class generator {

	// TODO Auto-generated constructor stub
	protected static int numQueriesPerTempate = generatorDefaultValues.numQueriesPerTempateDef;
	protected static String queryTemplateDir = generatorDefaultValues.queryTemplateDirDef;
	protected static String queryInstanceDir = generatorDefaultValues.queryInstanceDirDef;
	protected static String querywithVariDir = generatorDefaultValues.querywithVariDirDef;

	//Current work path
	public static String currentPath = System.getProperty("user.dir");
	//return back to the project path: ../LinGBM
	public static String path = currentPath.substring(0, currentPath.lastIndexOf("/querygen"));
	protected static String placeholderValDir= path+generatorDefaultValues.placeholderValDirDef;

	static ArrayList<String> templates = new ArrayList<String>();
	static final Random seedGenerator = new Random(53223436L);
	static ArrayList<String> placeholders = new ArrayList<String>();
	/*
	 * Parameters for steady state
	 */
	
	public generator(String[] args) {
		processProgramParameters(args);
	}
	
	 //Process the program parameters typed on the command line.
	 
	protected void processProgramParameters(String[] args) {
		int i = 0;
		while (i < args.length) {
			try {
				if (args[i].equals("-nm")) {
					numQueriesPerTempate = Integer.parseInt(args[i++ + 1]);
				} else if (args[i].equals("-values")) {
					placeholderValDir = args[i++ + 1];
				} else if (args[i].equals("-templates")) {
					queryTemplateDir = args[i++ + 1];
				} else if (args[i].equals("-outdir")) {
					queryInstanceDir = args[i++ + 1];
				} else {
					if (!args[i].equals("-help")) {
						System.err.println("Unknown parameter: " + args[i]);
					}
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
	

	public static void main(String[] args) throws IOException, ParseException {
		generator generator = new generator(args);

		//read query template, and store it as string
		File dir = new File(queryTemplateDir);
		int numberOfTemplates = 0;
		File listDir[] = dir.listFiles();
		for (int i = 0; i < listDir.length; i++) {
			if (listDir[i].isDirectory()) {
				numberOfTemplates++;
			}
		}
		System.out.println("number of query templates:"+numberOfTemplates);
		System.out.println("Read in query templates and placeholders for query templates...");
		for (int i = 1; i<= numberOfTemplates; i++){
			File queryTempfile = new File(dir, "template"+i+"/queryTemplate"+i+".txt");
			FileInputStream queryT = new FileInputStream(queryTempfile);
			BufferedReader txtQueryTem = new BufferedReader(new InputStreamReader(queryT));


			String queryLine = txtQueryTem.readLine();
			StringBuilder queryBuilder = new StringBuilder();
			while(queryLine != null){
				queryBuilder.append(queryLine).append("\n");
				queryLine = txtQueryTem.readLine();
			}
			String queryTemp = queryBuilder.toString();
			templates.add(queryTemp);

			File queryDesfile = new File(dir, "template"+i+"/queryTemplateDes"+i+".txt");
			BufferedReader txtQueryDes = new BufferedReader(new InputStreamReader(new FileInputStream(queryDesfile)));
			String placeholder = txtQueryDes.readLine();

			String queryDescription= "";
			while(placeholder != null){
				queryDescription = queryDescription+"-"+placeholder;
				placeholder = txtQueryDes.readLine();
			}
			String para = queryDescription.substring(1, queryDescription.length());
			placeholders.add(para);
		}


		System.out.println("Query templates and description are prepared.\n");

		System.out.println("Read in Values for placeholders...");
		//The path to possible values for the placeholders
		File resourceDir = new File(placeholderValDir);
		// read in files that used to generate values for the placeholders
		valueSelection valueSel = new valueSelection();
		Long seed = seedGenerator.nextLong();
		valueSel.init(resourceDir, seed);
		System.out.println("Values for placeholders are prepared.");

		System.out.println("\n Clear existing query instances...\n");
		File dirIns = new File(queryInstanceDir);
		deleteFolder(dirIns);
		System.out.println("\n Cleared\n");
		System.out.println("\n Clear existing queries with variables...\n");
		File dirQueryVari = new File(querywithVariDir);
		deleteFolder(dirQueryVari);
		System.out.println("\n Cleared\n");

		System.out.println("\nStart generating new query instances...\n");
		for(int i=0; i< placeholders.size(); i++){
			String queryTemp = templates.get(i);
			String placeholder = placeholders.get(i);
			queryInstantiation instances = new queryInstantiation(queryTemp, placeholder, valueSel, dirIns, dirQueryVari, numQueriesPerTempate, (i+1));
			System.out.println("queries for template "+(i+1)+" has been generated.");
		}
		System.out.println("All query instances has been generated.");
	}
}
