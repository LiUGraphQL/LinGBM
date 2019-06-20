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

	public queryInstantiation(String oldQuery, String parameter, valueSelection valueSel, File dirIns , File dirQueryVari, int max, int No_temp) throws ParseException {


		// Parse the parameter
		String[] paraParts = parameter.split("-");
		//Process the query string: replace the placeholders with corresponding values
		String[][] valueCombine = null;
		valueCombine = valueSel.SelectedValues(parameter, 20);

		if(valueCombine!= null){
			for(int i = 0; i<valueCombine.length; i++){
				String replaceString=oldQuery.substring(oldQuery.indexOf('\n')+1);
				File dirActualQueries = new File(dirIns+"/"+"queryTemplate_"+No_temp+"/");
				dirActualQueries.mkdirs();
				for(int j = 0; j<valueCombine[0].length; j++){
					replaceString = replaceString.replace(paraParts[j], valueCombine[i][j]);
				}

				File queryinstance = new File(dirActualQueries, "query"+(i+1)+".graphql");
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

				String variables="{"+ "\n ";
				File dirQuerywithVari = new File(dirQueryVari+"/"+"variableValues_"+No_temp+"/");
				dirQuerywithVari.mkdirs();
				for(int k = 0; k<valueCombine[0].length; k++){
					variables = variables.concat("	\""+paraParts[k].substring(1, paraParts[k].length())+"\": "+valueCombine[i][k]+",\n");
				}
				variables = variables.substring(0, variables.length()-2);
				variables=variables.concat("\n}");

				File querywithVariable = new File(dirQuerywithVari, "values"+(i+1)+".json");
				try(
						FileWriter rfw = new FileWriter(querywithVariable, true);
						BufferedWriter rbw = new BufferedWriter(rfw);
						PrintWriter R_file = new PrintWriter(rbw))
				{
					R_file.println( variables + "\n ");
				}
				catch(Exception e) {
					System.out.println("This is the type of exception found for add values for variables of query: " + e);
				}


			}
		}
	}

}
