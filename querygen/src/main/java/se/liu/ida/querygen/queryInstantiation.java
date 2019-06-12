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
				//String replaceString=oldQuery;
				StringBuilder builder = new StringBuilder();
				builder.append(oldQuery);
				File dirActualQueries = new File(dirIns+"/queryTemplate_"+No_temp+"/");
				dirActualQueries.mkdir();
				for(int j = 0; j<valueCombine[0].length; j++){
					int indexOfParainQuery = builder.lastIndexOf(paraParts[j]);
					builder = builder.replace(indexOfParainQuery, indexOfParainQuery + paraParts[j].length(), valueCombine[i][j]);
					//replaceString = replaceString.replace(paraParts[j],valueCombine[i][j]);
				}
				File queryinstance = new File(dirActualQueries, "query"+(i+1)+".graphql");
				try(
						FileWriter rfw = new FileWriter(queryinstance, true);
						BufferedWriter rbw = new BufferedWriter(rfw);
						PrintWriter R_file = new PrintWriter(rbw))
				{
					R_file.println( builder + "\n ");
					R_file.close();
					}
				catch(Exception e) {
					System.out.println("This is the type of exception found for filling parameter of query: " + e);
				}

				String newQuery=oldQuery.concat("{"+ "\n ");
				File dirQuerywithVari = new File(dirQueryVari+"/queryTemplate_"+No_temp+"/");
				dirQuerywithVari.mkdir();
				for(int k = 0; k<valueCombine[0].length; k++){
					newQuery = newQuery.concat("	\""+paraParts[k].substring(1, paraParts[k].length())+"\": "+valueCombine[i][k]+",\n");
				}
				newQuery = newQuery.substring(0, newQuery.length()-2);
				newQuery=newQuery.concat("\n}");

				File querywithVariable = new File(dirQuerywithVari, "query"+(i+1)+".graphql");
				try(
						FileWriter rfw = new FileWriter(querywithVariable, true);
						BufferedWriter rbw = new BufferedWriter(rfw);
						PrintWriter R_file = new PrintWriter(rbw))
				{
					R_file.println( newQuery + "\n ");
					R_file.close();
				}
				catch(Exception e) {
					System.out.println("This is the type of exception found for add values for variables of query: " + e);
				}


			}
		}
	}

}
