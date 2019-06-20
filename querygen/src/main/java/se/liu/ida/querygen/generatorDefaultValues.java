package se.liu.ida.querygen;

// This module defines the default values for running the queryGenerator
// If a user doesn't specify any values when running, the default values will be set
public class generatorDefaultValues {

	public static int numQueriesPerTempateDef = 20;//how many Query mixes are generated for one query template
	public static String queryTemplateDirDef = "queryTemplate";
	//Absolute path for "td_data", need to find this directory in dataset generator
	public static String placeholderValDirDef = "/datasetgen/td_data";
	public static String queryInstanceDirDef = "actualQueries";
	public static String querywithVariDirDef = "queryVariables";
}
