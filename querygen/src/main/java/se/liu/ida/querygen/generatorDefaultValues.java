package se.liu.ida.querygen;

// This module defines the default values for funning the queryGenerator
// If the user don's specify any values when running, the default values will be set
public class generatorDefaultValues {
	public static int numQueriesPerTempateDef = 20;//how many Query mixes are generated for one query template
	public static String queryTemplateDirDef = "queryTemplate/";
	public static String placeholderValDirDef = "td_data";
	public static String queryInstanceDirDef = "actualQueries/";
}
