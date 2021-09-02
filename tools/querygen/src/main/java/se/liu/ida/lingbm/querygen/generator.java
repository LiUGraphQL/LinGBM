package se.liu.ida.lingbm.querygen;

import java.io.*;
import java.text.ParseException;
import java.util.*;

// This module works for input query templates and output query instances
// queryInstantiation is called from here
public class generator {

	// TODO Auto-generated constructor stub
	protected static int numQueriesPerTempate = generatorDefaultValues.numQueriesPerTempateDef;
	protected static String queryInstanceDir = generatorDefaultValues.queryInstanceDirDef;
	protected static String querywithVariDir = generatorDefaultValues.querywithVariDirDef;


	protected static String placeholderValDir= generatorDefaultValues.placeholderValDirDef;
	protected static String queryTemplateDir = generatorDefaultValues.queryTemplateDirDef;

	static ArrayList<String> statistic_data = new ArrayList<>();
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
				switch (args[i]){
					case "-nm":
						numQueriesPerTempate = Integer.parseInt(args[i++ + 1]);
						break;
					case "-values":
						placeholderValDir = args[i++ + 1];
						break;
					case "-templates":
						queryTemplateDir = args[i++ + 1];
						break;
					case "-outdirQ":
						queryInstanceDir = args[i++ + 1];
						break;
					case "-outdirV":
						querywithVariDir = args[i++ + 1];
						break;
					default:
						if (!args[i].equals("-help")) {
							System.err.println("Unknown parameter: " + args[i]);
						}
						printUsageInfos();
						System.exit(-1);
						break;
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
				+ "\t-nm <specify the number of query instances for each template>\n" + "\t\tdefault: "
				+ generatorDefaultValues.numQueriesPerTempateDef
				+ "\n"
				+ "\t-values <path to input values for placeholders>\n"
				+ "\t\tThe input directory for the possible values for placeholders of template\n"
				+ "\t\tdefault: "
				+ generatorDefaultValues.placeholderValDirDef
				+ "\n"
				+ "\t-templates <path to query template>\n"
				+ "\t\tdefault: "
				+ generatorDefaultValues.queryTemplateDirDef
				+ "\n"
				+ "\t-outdirQ <path to output directory:query instances>\n"
				+ "\t\tdefault: "
				+ generatorDefaultValues.queryInstanceDirDef
				+ "\n"
				+ "\t-outdirV <path to output directory: values for variables>\n"
				+ "\t\tdefault: "
				+ generatorDefaultValues.querywithVariDirDef
				+ "\n"
				;

		System.out.print(output);
	}
	
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            }
	            f.delete();
	        }
	    }
	}

	static class FileGen{
		String fileName;
		File txtFile;
		File varsFile;

		public FileGen(String fileName){
			this.fileName=fileName;
		}

		public boolean checkPaired(){
			return !(txtFile==null||varsFile==null);
		}
	}
	

	public static void main(String[] args) throws IOException {
		new generator(args);

		File dir = new File(queryTemplateDir);
		if (!dir.isDirectory()){
			System.out.println("ERROR: "+dir+" is not a valid directory");
			System.exit(1);
		}
		//System.out.println("Read in Values for placeholders...");
		//The path to possible values for the placeholders
		File resourceDir = new File(placeholderValDir);
		if(!resourceDir.isDirectory()){
			System.out.println("ERROR: "+resourceDir+" is not a valid directory.");
			System.exit(1);
		}

		// read in files that used to generate values for the placeholders
		valueSelection_new valueSel = new valueSelection_new();
		valueSel.init(resourceDir);
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
		File[] listDir = dir.listFiles();

		Map<String,FileGen> fileMap=new LinkedHashMap<>();
		for ( File file : listDir ) {
			String fileName=file.getName();
			if(!(fileName.endsWith(".txt")||fileName.endsWith(".vars"))){
				continue;
			}
			String simpleName=fileName.split("\\.")[0];
			FileGen fileGen;
			if(fileMap.containsKey(simpleName)){
				fileGen=fileMap.get(simpleName);
			}else{
				fileGen=new FileGen(simpleName);
				fileMap.put(simpleName,fileGen);
			}
			if(fileName.endsWith(".txt")){
				fileGen.txtFile=file;
			}else{
				fileGen.varsFile=file;
			}
		}

		System.out.println("Read in query templates and placeholders for query templates...");

		List<FileGen> collection=new ArrayList<>(fileMap.values());
		Collections.sort(collection, Comparator.comparing(o -> o.fileName));
		int countTemplates=0;

		for (FileGen fileGen:collection){
			if(!fileGen.checkPaired()){
				System.out.println("query template/placeholders for "+fileGen.fileName+" is missing. Skipped");
				continue;
			}
			countTemplates++;
			File queryTempfile = fileGen.txtFile;
			FileInputStream queryT = new FileInputStream(queryTempfile);
			BufferedReader txtQueryTem = new BufferedReader(new InputStreamReader(queryT));

			String queryLine = txtQueryTem.readLine();
			StringBuilder queryBuilder = new StringBuilder();
			while(queryLine != null){
				queryBuilder.append(queryLine).append("\n");
				queryLine = txtQueryTem.readLine();
			}
			String queryTemp = queryBuilder.toString();

			File queryDesfile = fileGen.varsFile;
			BufferedReader txtQueryDes = new BufferedReader(new InputStreamReader(new FileInputStream(queryDesfile)));
			String placeholderTemp = txtQueryDes.readLine();

			String queryDescription= "";
			while(placeholderTemp != null){
				queryDescription = queryDescription+"-"+placeholderTemp;
				placeholderTemp = txtQueryDes.readLine();
			}
			String placeholder = queryDescription.substring(1);

			int templateNr = Integer.parseInt(fileGen.fileName.replaceAll("[^0-9]", ""));
			new queryInstantiation(queryTemp, placeholder, valueSel, dirIns, dirQueryVari, numQueriesPerTempate, templateNr);

			Integer[] actualNumInstan = valueSel.getInstanceNm(placeholder, numQueriesPerTempate);
			statistic_data.add(actualNumInstan[1]+", QT"+templateNr+","+actualNumInstan[2]);
			System.out.println("queries for template QT"+templateNr+" has been generated.");
		}

		if(countTemplates==0){
			System.out.println("ERROR: no applicable template exists in the given directory "+dir.getName());
			System.exit(1);
		}

		System.out.println("All query instances has been generated.");

		File path = new File(dirIns.getPath().substring(0, dirIns.getPath().lastIndexOf("/actualQueries")));
		File oldNumInstance = new File(path, "/NumOfInstances.csv");
		oldNumInstance.delete();

		path.mkdir();

		File numInstance = new File(path, "/NumOfInstances.csv");
		try (
			FileWriter rfw = new FileWriter(numInstance, true);
			BufferedWriter rbw = new BufferedWriter(rfw);
			PrintWriter R_file = new PrintWriter(rbw)) {
				for ( String data : statistic_data) {
					R_file.println( data );
				}
		} catch (Exception e) {
			System.out.println("This is the type of exception found for filling parameter of query: " + e);
		}
		System.out.println("Statics has been recorded.");
	}
}
