package se.liu.ida.querygen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

//This class works for generating query instances, that is the query templates with all placeholders filling.
//Given a query template, return a set of query instances.
public class queryInstantiation {
	//public abstract Object[] getParametersForQuery(queryPattern query);
	static final Random seedGenerator = new Random(53223436L);
	public queryInstantiation(String oldQuery, String parameter, 	File resourceDir, File actualDir, int max, int No_temp) throws ParseException {	
		valueSelection value = new valueSelection();
		Long seed = seedGenerator.nextLong();
		value.init(resourceDir, seed);
		String[] paraParts = parameter.split("-");
		if (paraParts.length==1){
			Set valueSet = null;
			//
			valueSet = value.getValues(parameter, max);
			if(valueSet!= null){
				Iterator iterator = valueSet.iterator();
				
				File dir = new File(actualDir, "queryTemplate_"+No_temp+"/");
				dir.mkdir();
				int i = 0;
				while(iterator.hasNext()){
				  String element = iterator.next().toString();
				  
				  
				  String replaceString=oldQuery.replace(parameter,element);
				  
				  
				  i++;
				  File queryinstance = new File(dir, "query"+i+".txt");
				  try(
						  FileWriter rfw = new FileWriter(queryinstance, true);
						  BufferedWriter rbw = new BufferedWriter(rfw);
						  PrintWriter R_file = new PrintWriter(rbw))
						{
							R_file.println( replaceString + "\n ");  
							R_file.close();
						} 
				  catch(Exception e) {
						System.out.println("This is the type of exception found for filling parameter of query: " + e);
				  }
				}
			}
			else {
				System.out.println("There is no possible value for this placeholder, please try again");
			}
			}
		else{
			String[][] valueCombine = null;
			//TODO numeber of instances		
			valueCombine = value.getCombines(parameter, 20);
			if(valueCombine!= null){
				for(int i = 0; i<valueCombine.length; i++){
					System.out.println("valueCombine.length:"+valueCombine.length);
					String replaceString=oldQuery;
					File dir = new File("actualQueries/queryTemplate_"+No_temp+"/");
					dir.mkdir();
					for(int j = 0; j<valueCombine[0].length; j++){
						System.out.println("valueCombine[0].length:"+valueCombine[0].length);
						System.out.println("paraParts[j]"+paraParts[j]);
						System.out.println("valueCombine[i][j]"+valueCombine[i][j]);
						replaceString = replaceString.replace(paraParts[j],valueCombine[i][j]);
					}
					System.out.println("replaceString:"+replaceString);
					File queryinstance = new File(dir, "query"+(i+1)+".txt");
					try(
							FileWriter rfw = new FileWriter(queryinstance, true);
							BufferedWriter rbw = new BufferedWriter(rfw);
							PrintWriter R_file = new PrintWriter(rbw))
					{
						R_file.println( replaceString + "\n ");  
						R_file.close();
						} 
					catch(Exception e) {
						System.out.println("This is the type of exception found for filling parameter of query: " + e);
						}
					  
					}
				}
			}			
		}
}
