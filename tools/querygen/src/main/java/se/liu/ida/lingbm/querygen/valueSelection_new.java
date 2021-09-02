package se.liu.ida.lingbm.querygen;

import java.io.*;
import java.text.ParseException;
import java.util.*;

// This module works for generating a group of values for one parameter or a combination of parameters.
// Given a placeholder, randomly select a group of values for it
public class valueSelection_new {
	protected ValueGenerator valueGen;

	private List<Integer> university = new ArrayList<Integer>();
	private List<Integer> department = new ArrayList<Integer>();
	private List<Integer> researchGroup = new ArrayList<Integer>();
	private List<Integer> faculty = new ArrayList<Integer>();
	private List<Integer> professor = new ArrayList<Integer>();
	private List<Integer> lecturer = new ArrayList<Integer>();
	private List<Integer> graduateStudent = new ArrayList<Integer>();
	private List<Integer> undergraduateStudent = new ArrayList<Integer>();
	private List<Integer> publication = new ArrayList<Integer>();
	private List<Integer> graduateCourse = new ArrayList<Integer>();
	private List<Integer> undergraduateCourse = new ArrayList<Integer>();
	private List<String> titleWord = new ArrayList<String>();
	private List<String> abstractWord = new ArrayList<String>();
	private List<String> interestWord = new ArrayList<String>();

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
		for(int i = 0; i<entityNames.length; i++){
			readDepartment(resourceDir, entityNames[i]);
		}
	}
	private void readDepartment(File resourceDir, String entity) {
		File dep = new File(resourceDir, entity+".txt");
		String line = null;

		BufferedReader departmentinputReader = null;
		try {
			departmentinputReader = new BufferedReader(new FileReader(dep));
			line = departmentinputReader.readLine();

			String[] values = line.split(", ");

			switch (entity) {
				case "universityID":
					universityCount = values.length;
					for(int i=0; i<values.length; i++){
						university.add(Integer.parseInt(values[i]));
					}
					break;
				case "departmentID":
					departmentCount = values.length;
					for(int i=0; i<values.length; i++){
						department.add(Integer.parseInt(values[i]));
					}
					break;
				case "researchGroupID":
					researchGroupCount = values.length;
					for(int i=0; i<values.length; i++){
						researchGroup.add(Integer.parseInt(values[i]));
					}
					break;
				case "facultyID":
					facultyCount = values.length;
					for(int i=0; i<values.length; i++){
						faculty.add(Integer.parseInt(values[i]));
					}
					break;
				case "professorID":
					professorCount = values.length;
					for(int i=0; i<values.length; i++){
						professor.add(Integer.parseInt(values[i]));
					}
					break;
				case "lecturerID":
					lecturerCount = values.length;
					for(int i=0; i<values.length; i++){
						lecturer.add(Integer.parseInt(values[i]));
					}
					break;
				case "graduateStudentID":
					graduateStudentCount = values.length;
					for(int i=0; i<values.length; i++){
						graduateStudent.add(Integer.parseInt(values[i]));
					}
					break;
				case "undergraduateStudentID":
					undergraduateStudentCount = values.length;
					for(int i=0; i<values.length; i++){
						undergraduateStudent.add(Integer.parseInt(values[i]));
					}
					break;
				case "publicationID":
					publicationCount = values.length;
					for(int i=0; i<values.length; i++){
						publication.add(Integer.parseInt(values[i]));
					}
					break;
				case "graduateCourseID":
					graduateCourseCount = values.length;
					for(int i=0; i<values.length; i++){
						graduateCourse.add(Integer.parseInt(values[i]));
					}
					break;
				case "undergraduateCourseID":
					undergraduateCourseCount = values.length;
					for(int i=0; i<values.length; i++){
						undergraduateCourse.add(Integer.parseInt(values[i]));
					}
					break;
				case "title":
					titleWordCount = values.length;
					for(int i=0; i<values.length; i++){
						titleWord.add(values[i]);
					}
					break;
				case "abstract":
					abstractWordCount = values.length;
					for(int i=0; i<values.length; i++){
						abstractWord.add(values[i]);
					}
					break;
				case "interest":
					interestWordCount = values.length;
					for(int i=0; i<values.length; i++){
						interestWord.add(values[i]);
					}
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

	protected String[][] SelectedValues(String field, Integer maxInstanceNm, ValueGenerator valueGen) throws ParseException {
		this.valueGen = valueGen;
		Integer instance = getInstanceNm(field, maxInstanceNm)[1];
		Set setCombination=getRandomSelectedValues(field, maxInstanceNm);
		String[] fields = field.split("-");
		int paraNum = fields.length;
		String[][] paras = new String[instance][paraNum];
		Iterator iterator = setCombination.iterator();
		int i=0;
		while(iterator.hasNext()){
			String element = (String) iterator.next();
			//String[] parts = element.split("_");
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

	protected Set getRandomSelectedValues(String field, Integer maxInstanceNm) throws ParseException {
		Integer instanceNm = getInstanceNm(field, maxInstanceNm)[1];
		Set setCombination = new HashSet();
		int size = 0;

		String[] fields = field.split("-");
		int paraNum = fields.length;

		boolean Empty = true;
		String component = null;
		//String connect = "_";
		String connect = "/";

		while(size < instanceNm){
			String oneCombination = getRandom(fields[0]);
			for(int i = 1; i< paraNum;i++){
				String para = fields[i];

				//if multiple variables in one template come from the same list, the values must be different.
				String compa1 = fields[i-1].replaceAll("[0-9]","");
				String compa2 = fields[i].replaceAll("[0-9]","");
				if(compa2.equals(compa1)){
					component = getRandom(para);
					while (oneCombination.contains(component)){
						component = getRandom(para);
					}
				}else {
					component = getRandom(para);
				}
				oneCombination = oneCombination+connect+component;
			}
			setCombination.add(oneCombination);
			size = setCombination.size();
		}
		Empty = setCombination.isEmpty();

		if(Empty)
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
		int combTotalCount =0;
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
				combTotalCount = 500 * 6 * 5;
				NumOfInstance[1] = Math.min(combTotalCount, maxInstanceNm);
				NumOfInstance[2] = combTotalCount;
				break;
			case "$cnt-$attrGStudent1Hasura-$attrGStudent2Hasura":
				combTotalCount = 500 * 6 * 5;
				NumOfInstance[1] = Math.min(combTotalCount, maxInstanceNm);
				NumOfInstance[2] = combTotalCount;
				break;
			case "$cnt-$attrGStudent1PostGraphile-$attrGStudent2PostGraphile":
				combTotalCount = 500 * 6 * 5;
				NumOfInstance[1] = Math.min(combTotalCount, maxInstanceNm);
				NumOfInstance[2] = combTotalCount;
				break;
			case "$universityID-$attrPublicationField":
				combTotalCount = universityCount * 2;
				NumOfInstance[1] = Math.min(combTotalCount, maxInstanceNm);
				NumOfInstance[2] = combTotalCount;
				break;
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

	protected String getRandom(String field) throws ParseException {
		int Nr=-1;
		String randomNr = null;
		if(field.equals("$departmentID")){
			Nr = valueGen.randomInt(0, departmentCount-1);
			randomNr = String.valueOf(department.get(Nr));
		}else if(field.equals("$facultyID")){
			Nr = valueGen.randomInt(0, facultyCount-1);
			randomNr = String.valueOf(faculty.get(Nr));
		}else if(field.equals("$universityID")){
			Nr = valueGen.randomInt(0, universityCount-1);
			randomNr = String.valueOf(university.get(Nr));
		}else if(field.equals("$researchGroupID")){
			Nr = valueGen.randomInt(0, researchGroupCount-1);
			randomNr = String.valueOf(researchGroup.get(Nr));
		}else if(field.equals("$lecturerID")){
			Nr = valueGen.randomInt(0, lecturerCount-1);
			randomNr = String.valueOf(lecturer.get(Nr));
		}else if(field.equals("$offset")){
			Nr = valueGen.randomInt(1, 50);
			randomNr = String.valueOf(Nr);
		}else if(field.equals("$cnt")){
			Nr = valueGen.randomInt(500, 1000);
			randomNr = String.valueOf(Nr);
		}else if(field.contains("$attrGStudent")){
			if (field.contains("PostGraphile")){
				Nr = valueGen.randomInt(0, graduateStudentField_PostGraphile.length-1);
				randomNr = String.valueOf(graduateStudentField_PostGraphile[Nr]);
			}else if(field.contains("Hasura")){
				Nr = valueGen.randomInt(0, graduateStudentField_Hasura.length-1);
				randomNr = String.valueOf(graduateStudentField_Hasura[Nr]);
			}else{
				Nr = valueGen.randomInt(0, graduateStudentField.length-1);
				randomNr = String.valueOf(graduateStudentField[Nr]);
			}
		}else if(field.contains("$attrPublicationField")){
			if(field.contains("PostGraphile")){
				Nr = valueGen.randomInt(0, publicationField_PostGraphile.length-1);
				randomNr = String.valueOf(publicationField_PostGraphile[Nr]);
			}else{
				Nr = valueGen.randomInt(0, publicationField.length-1);
				randomNr = String.valueOf(publicationField[Nr]);
			}
		}else if(field.equals("$age")){
			Nr = valueGen.randomInt(20, 27);
			randomNr = String.valueOf(Nr);
		}else if(field.equals("$keyword")){
			Nr = valueGen.randomInt(0, titleWordCount-1);
			randomNr = titleWord.get(Nr);
		}else if(field.equals("$interestWord")){
			Nr = valueGen.randomInt(0, interestWordCount-1);
			randomNr = interestWord.get(Nr);
		}else {
		}
		return randomNr;
	}
}
