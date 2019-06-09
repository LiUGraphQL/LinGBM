package se.liu.ida.querygen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

//This class works for generating query instances, which are the query templates with all placeholders filling.
//Given a query template, output a set of query instances.
public class queryInstantiation {

	static final Random seedGenerator = new Random(53223436L);
	public queryInstantiation(String oldQuery, String parameter, File resourceDir, File actualDir, int max, int No_temp) throws ParseException {

		// read in files that used to generate values for the placeholders
		valueSelection value = new valueSelection();
		Long seed = seedGenerator.nextLong();
		value.init(resourceDir, seed);

		// Parse the parameter
		String[] paraParts = parameter.split("-");

		//Process the query string: replace the placeholders with corresponding values
		String[][] valueCombine = null;
		valueCombine = value.SelectedValues(parameter, 20);
		if(valueCombine!= null){
			for(int i = 0; i<valueCombine.length; i++){
				String replaceString=oldQuery;
				File dir = new File("actualQueries/queryTemplate_"+No_temp+"/");
				dir.mkdir();
				for(int j = 0; j<valueCombine[0].length; j++){
					replaceString = replaceString.replace(paraParts[j],valueCombine[i][j]);
				}
				File queryinstance = new File(dir, "query"+(i+1)+".graphql");
				try(
						FileWriter rfw = new FileWriter(queryinstance, true);
						BufferedWriter rbw = new BufferedWriter(rfw);
						PrintWriter R_file = new PrintWriter(rbw))
				{
					R_file.println( replaceString + "\n ");
					}
				catch(Exception e) {
					System.out.println("This is the type of exception found for filling parameter of query: " + e);
					}

				}
			}
		}
}
