package se.liu.ida.lingbm.querygen;

import java.io.*;
import java.text.ParseException;
import java.util.*;

// This module works for generating a group of values for one parameter or a combination of parameters.
// Given a placeholder, randomly select a group of values for it
public class valueSelection_new {
	protected ValueGenerator valueGen;

	private List<Integer> university = new ArrayList<>();
	private List<Integer> department = new ArrayList<>();
	private List<Integer> researchGroup = new ArrayList<>();
	private List<Integer> faculty = new ArrayList<>();
	private List<Integer> professor = new ArrayList<>();
	private List<Integer> lecturer = new ArrayList<>();
	private List<Integer> graduateStudent = new ArrayList<>();
	private List<Integer> undergraduateStudent = new ArrayList<>();
	private List<Integer> publication = new ArrayList<>();
	private List<Integer> graduateCourse = new ArrayList<>();
	private List<Integer> undergraduateCourse = new ArrayList<>();
	private List<String> titleWord = new ArrayList<>();
	private List<String> abstractWord = new ArrayList<>();
	private List<String> interestWord = new ArrayList<>();

	protected Integer scalefactor;
	protected Integer departmentCount;
	protected Integer universityCount;
	protected Integer researchGroupCount;
	protected Integer facultyCount;
	protected Integer professorCount;
	protected Integer lecturerCount;
	protected Integer graduateStudentCount;
	protected Integer undergraduateStudentCount;
	protected Integer publicationCount;
	protected Integer graduateCourseCount;
	protected Integer undergraduateCourseCount;
	protected Integer titleWordCount;
	protected Integer abstractWordCount;
	protected Integer interestWordCount;

	protected String[] entityNames = {"universityID", "departmentID", "researchGroupID", "facultyID",
			"professorID", "lecturerID", "graduateStudentID", "undergraduateStudentID",
			"publicationID", "graduateCourseID", "undergraduateCourseID", "title", "abstract", "interest"};

	protected void init( File resourceDir ) {
		for( String entity : entityNames ){
			readDepartment( resourceDir, entity );
		}
	}
	private void readDepartment(File resourceDir, String entity) {
		File dep = new File(resourceDir, entity+".txt");

		try {
			BufferedReader departmentInputReader = new BufferedReader(new FileReader(dep));
			String line = departmentInputReader.readLine();

			String[] values = line.split(", ");

			switch (entity) {
				case "universityID":
					universityCount = values.length;
					for ( String value : values){
						university.add( Integer.parseInt(value) );
					}
					break;
				case "departmentID":
					departmentCount = values.length;
					for ( String value : values){
						department.add( Integer.parseInt(value) );
					}
					break;
				case "researchGroupID":
					researchGroupCount = values.length;
					for ( String value : values){
						researchGroup.add( Integer.parseInt(value) );
					}
					break;
				case "facultyID":
					facultyCount = values.length;
					for ( String value : values){
						faculty.add( Integer.parseInt(value) );
					}
					break;
				case "professorID":
					professorCount = values.length;
					for ( String value : values){
						professor.add( Integer.parseInt(value) );
					}
					break;
				case "lecturerID":
					lecturerCount = values.length;
					for ( String value : values){
						lecturer.add( Integer.parseInt(value) );
					}
					break;
				case "graduateStudentID":
					graduateStudentCount = values.length;
					for ( String value : values){
						graduateStudent.add( Integer.parseInt(value) );
					}
					break;
				case "undergraduateStudentID":
					undergraduateStudentCount = values.length;
					for ( String value : values){
						undergraduateStudent.add( Integer.parseInt(value) );
					}
					break;
				case "publicationID":
					publicationCount = values.length;
					for ( String value : values){
						publication.add( Integer.parseInt(value) );
					}
					break;
				case "graduateCourseID":
					graduateCourseCount = values.length;
					for ( String value : values){
						graduateCourse.add( Integer.parseInt(value) );
					}
					break;
				case "undergraduateCourseID":
					undergraduateCourseCount = values.length;
					for ( String value : values){
						undergraduateCourse.add( Integer.parseInt(value) );
					}
					break;
				case "title":
					titleWordCount = values.length;
					titleWord.addAll(List.of(values));
					break;
				case "abstract":
					abstractWordCount = values.length;
					abstractWord.addAll(List.of(values));
					break;
				case "interest":
					interestWordCount = values.length;
					interestWord.addAll(List.of(values));
					break;
				default:
					break;
			}
		}
		catch(IOException e) {
			System.err.println("Could not open or process file " + resourceDir.getAbsolutePath());
			System.err.println(e.getMessage());
			System.exit(-1);
		}

	}

	protected String[][] SelectedValues(String field, Integer maxInstanceNm) {
		final Random seedGenerator = new Random(53223436L);
		valueGen = new ValueGenerator(seedGenerator.nextLong());

		Integer instance = getInstanceNm(field, maxInstanceNm)[1];
		Set<String> setCombination=getRandomSelectedValues(field, maxInstanceNm);
		String[] fields = field.split("-");
		int paraNum = fields.length;
		String[][] paras = new String[instance][paraNum];
		int i=0;
		for( String element:setCombination ){
			String[] parts = element.split("/");
			int k=0;
			for(String value:parts){
				paras[i][k]= value;
				k++;
			}
			i++;
		}
		return paras;
	}

	protected Set<String> getRandomSelectedValues(String field, Integer maxInstanceNm) {
		Set<String> setCombination = new LinkedHashSet<>();
		int size = 0;

		String[] fields = field.split("-");
		int paraNum = fields.length;

		Integer instanceNm = getInstanceNm(field, maxInstanceNm)[1];
		while(size < instanceNm){
			String oneCombination = getRandom(fields[0]);
			for(int i = 1; i< paraNum; i++){
				String para = fields[i];
				String component = getRandom(para);

				//if multiple variables in one template come from the same list, the values must be different.
				String compa1 = fields[i-1].replaceAll("[0-9]","");
				String compa2 = fields[i].replaceAll("[0-9]","");
				if(compa2.equals(compa1)){
					while (oneCombination.contains(component)){
						component = getRandom(para);
					}
				}
				oneCombination = oneCombination+"/"+component;
			}
			setCombination.add(oneCombination);
			size = setCombination.size();
		}

		if(setCombination.isEmpty())
			System.out.println("The combination set is empty");
		return setCombination;
	}

	//For the customized schema: attributes of publication
	protected String[] publicationField = {"title", "abstract"};
    //For auto generated schema of PostGraphile: attributes of publication
	protected String[] publicationField_PostGraphile = {"TITLE_DESC", "ABSTRACT_DESC"};

	//For the customized schema 'main': attributes of GrauduateStudent
	protected String[] graduateStudentField = {"id", "telephone", "emailAddress", "memberOf", "undergraduateDegreeFrom", "advisor"};
    //For auto generated schema of Hasura: attributes of GrauduateStudent
    protected String[] graduateStudentField_Hasura = {"nr", "telephone", "emailaddress", "memberof", "undergraduatedegreefrom", "advisor"};
    //For auto generated schema of PostGraphile: attributes of GrauduateStudent
    protected String[] graduateStudentField_PostGraphile = {"NR_ASC", "TELEPHONE_ASC", "EMAILADDRESS_ASC", "MEMBEROF_ASC", "UNDERGRADUATEDEGREEFROM_ASC", "ADVISOR_ASC"};




	protected Integer[] getInstanceNm(String field, Integer maxInstanceNm) {
		int combTotalCount;
		//NumOfInstance[1]: number of query instances (should be generated)
		//NumOfInstance[2]: actual max number of query instances in the dataset
		Integer[] NumOfInstance = new Integer[3];
		switch (field){
			case "$facultyID":
				NumOfInstance[1] = Math.min(facultyCount, maxInstanceNm);
				NumOfInstance[2] = facultyCount;
				break;
			case "$universityID":
				NumOfInstance[1] = Math.min(universityCount, maxInstanceNm);
				NumOfInstance[2] = universityCount;
				break;
			case "$researchGroupID":
				NumOfInstance[1] = Math.min(researchGroupCount, maxInstanceNm);
				NumOfInstance[2] = researchGroupCount;
				break;
			case "$lecturerID":
				NumOfInstance[1] = Math.min(lecturerCount, maxInstanceNm);
				NumOfInstance[2] = lecturerCount;
				break;
			case "$universityID-$offset":
				combTotalCount = universityCount * 50;
				NumOfInstance[1] = Math.min(combTotalCount, maxInstanceNm);
				NumOfInstance[2] = combTotalCount;
				break;
			case "$cnt-$attrGStudent1-$attrGStudent2":
			case "$cnt-$attrGStudent1Hasura-$attrGStudent2Hasura":
			case "$cnt-$attrGStudent1PostGraphile-$attrGStudent2PostGraphile":
				combTotalCount = 500 * 6 * 5;
				NumOfInstance[1] = Math.min(combTotalCount, maxInstanceNm);
				NumOfInstance[2] = combTotalCount;
				break;
			case "$universityID-$attrPublicationField":
			case "$universityID-$attrPublicationFieldPostGraphile":
				combTotalCount = universityCount * 2;
				NumOfInstance[1] = Math.min(combTotalCount, maxInstanceNm);
				NumOfInstance[2] = combTotalCount;
				break;
			case "$keyword":
				NumOfInstance[1] = Math.min(titleWordCount, maxInstanceNm);
				NumOfInstance[2] = titleWordCount;
				break;
			case "$universityID-$departmentID":
				combTotalCount = universityCount * departmentCount;
				NumOfInstance[1] = Math.min(combTotalCount, maxInstanceNm);
				NumOfInstance[2] = combTotalCount;
				break;
			case "$universityID-$interestWord":
				combTotalCount = universityCount * interestWordCount;

				if(combTotalCount<0){
					NumOfInstance[1] = maxInstanceNm;
					NumOfInstance[2] = (int)Math.pow(2, 30)-1;
				}else {
					NumOfInstance[1] = Math.min(combTotalCount, maxInstanceNm);
					NumOfInstance[2] = combTotalCount;
				}
				break;
			case "$universityID-$age-$interestWord":
				combTotalCount = (universityCount * interestWordCount) * 7;
				if(combTotalCount<0){
					NumOfInstance[1] = maxInstanceNm;
					NumOfInstance[2] = (int)Math.pow(2, 30)-1;
				}else {
					NumOfInstance[1] = Math.min(combTotalCount, maxInstanceNm);
					NumOfInstance[2] = combTotalCount;
				}
				break;
			case "$departmentID":
				NumOfInstance[1] = Math.min(departmentCount, maxInstanceNm);
				NumOfInstance[2] = departmentCount;
				break;
			default:
				break;
		}
		NumOfInstance[0] = scalefactor;
		return NumOfInstance;
	}

	protected String getRandom(String field) {
		if(field.equals("$departmentID")){
			int Nr = valueGen.randomInt(0, departmentCount-1);
			return String.valueOf(department.get(Nr));
		}else if(field.equals("$facultyID")){
			int Nr = valueGen.randomInt(0, facultyCount-1);
			return String.valueOf(faculty.get(Nr));
		}else if(field.equals("$universityID")){
			int Nr = valueGen.randomInt(0, universityCount-1);
			return String.valueOf(university.get(Nr));
		}else if(field.equals("$researchGroupID")){
			int Nr = valueGen.randomInt(0, researchGroupCount-1);
			return String.valueOf(researchGroup.get(Nr));
		}else if(field.equals("$lecturerID")){
			int Nr = valueGen.randomInt(0, lecturerCount-1);
			return String.valueOf(lecturer.get(Nr));
		}else if(field.equals("$offset")){
			int Nr = valueGen.randomInt(1, 50);
			return String.valueOf(Nr);
		}else if(field.equals("$cnt")){
			int Nr = valueGen.randomInt(500, 1000);
			return String.valueOf(Nr);
		}else if(field.contains("$attrGStudent")){
			if (field.contains("PostGraphile")){
				int Nr = valueGen.randomInt(0, graduateStudentField_PostGraphile.length-1);
				return String.valueOf(graduateStudentField_PostGraphile[Nr]);
			}else if(field.contains("Hasura")){
				int Nr = valueGen.randomInt(0, graduateStudentField_Hasura.length-1);
				return String.valueOf(graduateStudentField_Hasura[Nr]);
			}else{
				int Nr = valueGen.randomInt(0, graduateStudentField.length-1);
				return String.valueOf(graduateStudentField[Nr]);
			}
		}else if(field.contains("$attrPublicationField")){
			if(field.contains("PostGraphile")){
				int Nr = valueGen.randomInt(0, publicationField_PostGraphile.length-1);
				return String.valueOf(publicationField_PostGraphile[Nr]);
			}else{
				int Nr = valueGen.randomInt(0, publicationField.length-1);
				return String.valueOf(publicationField[Nr]);
			}
		}else if(field.equals("$age")){
			int Nr = valueGen.randomInt(20, 27);
			return String.valueOf(Nr);
		}else if(field.equals("$keyword")){
			int Nr = valueGen.randomInt(0, titleWordCount-1);
			return titleWord.get(Nr);
		}else if(field.equals("$interestWord")){
			int Nr = valueGen.randomInt(0, interestWordCount-1);
			return interestWord.get(Nr);
		}else {
			return null;
		}
	}
}
