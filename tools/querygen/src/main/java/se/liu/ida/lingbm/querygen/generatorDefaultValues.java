package se.liu.ida.lingbm.querygen;

// This module defines the default values for running the queryGenerator
// If a user doesn't specify any values when running, the default values will be set
public class generatorDefaultValues {

	//Current work path
	public static String currentPath = System.getProperty("user.dir");
	//return back to the project path: ../LinGBM
	public static String path = currentPath.substring(0, currentPath.lastIndexOf("LinGBM"));

	public static int numQueriesPerTempateDef = 20;//how many Query mixes are generated for one query template
	public static String queryTemplateDirDef = path+"LinGBM/artifacts/queryTemplates/main";
	//Absolute path for "td_data", need to find this directory in dataset generator
	public static String placeholderValDirDef = path+"LinGBM/tools/datasetgen/td_data";
	public static String queryInstanceDirDef = path+"LinGBM/tools/querygen/actualQueries";
	public static String querywithVariDirDef = path+"LinGBM/tools/querygen/queryVariables";
}
