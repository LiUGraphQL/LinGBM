package edu.lehigh.swat.bench.uba.writers;

import java.io.*;
import java.util.*;

import edu.lehigh.swat.bench.uba.GeneratorCallbackTarget;
import edu.lehigh.swat.bench.uba.GlobalState;
import edu.lehigh.swat.bench.uba.model.Ontology;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;


public abstract class SQLFlatWriter extends AbstractWriter implements Writer {

    protected static final String RDF_TYPE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    protected static final String OWL_ONTOLOGY = "http://www.w3.org/2002/07/owl#Ontology";
    protected static final String OWL_IMPORTS = "http://www.w3.org/2002/07/owl#imports";
    static Random rand= new Random(20);

    protected final String ontologyUrl;
    private final Stack<String> subjects = new Stack<String>();
    private final Stack<Integer> type = new Stack<Integer>();
    private List<String> wordlist = new ArrayList<String>();
    private List<Integer> universityID = new ArrayList<Integer>();
    private List<Integer> departmentID = new ArrayList<Integer>();
    private List<Integer> researchGroupID = new ArrayList<Integer>();
    private List<Integer> facultyID = new ArrayList<Integer>();
    private List<Integer> professorID = new ArrayList<Integer>();
    private List<Integer> lecturerID = new ArrayList<Integer>();
    private List<Integer> graduateStudentID = new ArrayList<Integer>();
    private List<Integer> undergraduateStudentID = new ArrayList<Integer>();
    private List<Integer> publicationID = new ArrayList<Integer>();
    private List<Integer> graduateCourseID = new ArrayList<Integer>();
    private List<Integer> undergraduateCourseID = new ArrayList<Integer>();
    private List<String> title_wordlist = new ArrayList<String>();
    private List<String> abstract_wordlist = new ArrayList<String>();
    private List<String> interest_wordlist = new ArrayList<String>();
    protected String[] entityNames = {"universityID", "departmentID", "researchGroupID", "facultyID",
            "professorID", "lecturerID", "graduateStudentID", "undergraduateStudentID",
            "publicationID", "graduateCourseID", "undergraduateCourseID", "title", "abstract", "interest"};

    public SQLFlatWriter(GeneratorCallbackTarget target, String ontologyUrl) {
        super(target);
        this.ontologyUrl = ontologyUrl;
        try {
            this.wordlist = fileReader("titlewords.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected void recordValues(String entity){

        //new File("values").mkdir();
        File pth = new File("values", entity+".txt");
        BufferedWriter outputWriter = null;
        try {
            pth.createNewFile();
            outputWriter = new BufferedWriter(new FileWriter(pth, true));
            switch (entity) {
                case "universityID":
                    outputWriter.write(universityID.toString().replace("[", "").replace("]", "")+", ");
                    break;
                case "departmentID":
                    outputWriter.write(departmentID.toString().replace("[", "").replace("]", "")+", ");
                    break;
                case "researchGroupID":
                    outputWriter.write(researchGroupID.toString().replace("[", "").replace("]", "")+", ");
                    break;
                case "facultyID":
                    outputWriter.write(facultyID.toString().replace("[", "").replace("]", "")+", ");
                    break;
                case "professorID":
                    outputWriter.write(professorID.toString().replace("[", "").replace("]", "")+", ");
                    break;
                case "lecturerID":
                    outputWriter.write(lecturerID.toString().replace("[", "").replace("]", "")+", ");
                    break;
                case "graduateStudentID":
                    outputWriter.write(graduateStudentID.toString().replace("[", "").replace("]", "")+", ");
                    break;
                case "undergraduateStudentID":
                    outputWriter.write(undergraduateStudentID.toString().replace("[", "").replace("]", "")+", ");
                    break;
                case "publicationID":
                    outputWriter.write(publicationID.toString().replace("[", "").replace("]", "")+", ");
                    break;
                case "graduateCourseID":
                    outputWriter.write(graduateCourseID.toString().replace("[", "").replace("]", "")+", ");
                    break;
                case "undergraduateCourseID":
                    outputWriter.write(undergraduateCourseID.toString().replace("[", "").replace("]", "")+", ");
                    break;
                case "title":
                    outputWriter.write(title_wordlist.toString().replace("[", "").replace("]", "")+", ");
                    break;
                case "abstract":
                    outputWriter.write(abstract_wordlist.toString().replace("[", "").replace("]", "")+", ");
                    break;
                case "interest":
                    outputWriter.write(interest_wordlist.toString().replace("[", "").replace("]", "")+", ");
                    break;

                default:
                    break;
            }
            outputWriter.close();

        } catch(IOException e) {
            System.err.println("Could not open or create file " + pth.getAbsolutePath());
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    protected void recordStatistics(String entity){
        File statis = new File("values", "statistics.csv");
        BufferedWriter outputWriter = null;
        try {
            outputWriter = new BufferedWriter(new FileWriter(statis, true));
            switch (entity) {
                case "universityID":
                    outputWriter.write("\nthe number of "+entity+","+universityID.size()+"\n");
                    break;
                case "departmentID":
                    outputWriter.write("the number of "+entity+","+departmentID.size()+"\n");
                    break;
                case "researchGroupID":
                    outputWriter.write("the number of "+entity+","+researchGroupID.size()+"\n");
                    break;
                case "facultyID":
                    outputWriter.write("the number of "+entity+","+facultyID.size()+"\n");
                    break;
                case "professorID":
                    outputWriter.write("the number of "+entity+","+professorID.size()+"\n");
                    break;
                case "lecturerID":
                    outputWriter.write("the number of "+entity+","+lecturerID.size()+"\n");
                    break;
                case "graduateStudentID":
                    outputWriter.write("the number of "+entity+","+graduateStudentID.size()+"\n");
                    break;
                case "undergraduateStudentID":
                    outputWriter.write("the number of "+entity+","+undergraduateStudentID.size()+"\n");
                    break;
                case "publicationID":
                    outputWriter.write("the number of "+entity+","+publicationID.size()+"\n");
                    break;
                case "graduateCourseID":
                    outputWriter.write("the number of "+entity+","+graduateCourseID.size()+"\n");
                    break;
                case "undergraduateCourseID":
                    outputWriter.write("the number of "+entity+","+undergraduateCourseID.size()+"\n");
                    break;
                case "title":
                    outputWriter.write("the number of "+entity+","+title_wordlist.size()+"\n");
                    break;
                case "abstract":
                    outputWriter.write("the number of "+entity+","+abstract_wordlist.size()+"\n");
                    break;
                case "interest":
                    outputWriter.write("the number of "+entity+","+interest_wordlist.size()+"\n");
                    break;

                default:
                    break;
            }
            outputWriter.close();

        } catch(IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }


    @Override
    public void startFile(String fileName, GlobalState state) {

        this.out = prepareOutputStream(fileName, state);
        writeHeader(this.out);
    }


    @Override
    public void startFile(GlobalState state, OutputStream output) {
    }

    //disable keys before inserting data and enable keys after it
    protected abstract void writeHeader(PrintStream output);
    protected abstract void writeEnableKeys(PrintStream output);

    @Override
    public void flushFile(GlobalState state) {
        if (this.out != null)
            this.out.flush();
    }


    @Override
    public void endFile(GlobalState state) {
        writeEnableKeys(this.out);
        if (!subjects.isEmpty())
            throw new RuntimeException("Mismatched calls to writer in endFile()");
        try {
            cleanupOutputStream(this.out);
            this.submitWrites();
        } finally {
            this.out = null;
        }
        for(int i = 0; i<entityNames.length; i++){
            recordValues(entityNames[i]);
            recordStatistics(entityNames[i]);
        }

    }


    @Override
    public void endFile(GlobalState state, OutputStream output) {
        // No-op
    }


    protected List<Integer> extractIntfromString(String value) {
        Matcher matcher = Pattern.compile("\\d+").matcher(value);
        List<Integer> list = new ArrayList<Integer>();
        while(matcher.find()) {
            list.add(Integer.parseInt(matcher.group()));
        }
        return list;
    }

    protected List fileReader(String fileName) throws IOException{
        List<String> result = new ArrayList<>();
        BufferedReader br = null;

        try {

            br = new BufferedReader(new FileReader(fileName));

            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return result;
    }

    /*

    protected String generateRamString(List wordlist, int numberOfWords) throws IOException{
        StringBuilder text = new StringBuilder();
        rand = new Random();
        int numberOfwordlist = wordlist.size();
        for(int i = 0; i<numberOfWords;i++){
            int rand_g = rand.nextInt(numberOfwordlist);
            text.append(wordlist.get(rand_g)+" ");
        }
        return text.toString();
    }

     */

    protected String generateRamString(List wordlist) throws IOException{
        int numberOfwordlist = wordlist.size();
        int rand_g = rand.nextInt(numberOfwordlist);
        String word = wordlist.get(rand_g).toString();

        return word;
    }


    protected int getIdOfCurrentSubject() {
        int objectID = 0;
        int department_id = -1;
        String current_url = this.subjects.peek();
        List<Integer> list1 = extractIntfromString(current_url);
        if(list1.size()>1){
            department_id = list1.get(1)*100+ Integer.parseInt(String.format("%02d", list1.get(0)));
        }
        else{
        }
        if (this.subjects.isEmpty())
            throw new RuntimeException("Mismatched calls to writer in getCurrentSubject()");

        switch (this.type.peek()) {
            case Ontology.CS_C_UNIV:
                objectID=list1.get(0);
                break;
            case Ontology.CS_C_DEPT:
                objectID= department_id;
                break;
            case Ontology.CS_C_FULLPROF:
                objectID= department_id*1000+Integer.parseInt(String.format("%02d", list1.get(2)))*10+1;
                break;
            case Ontology.CS_C_ASSOPROF:
                objectID= department_id*1000+Integer.parseInt(String.format("%02d", list1.get(2)))*10+2;
                break;
            case Ontology.CS_C_ASSTPROF:
                objectID= department_id*1000+Integer.parseInt(String.format("%02d", list1.get(2)))*10+3;
                break;
            case Ontology.CS_C_LECTURER:
                objectID= department_id*1000+Integer.parseInt(String.format("%02d", list1.get(2)))*10+4;
                break;
            case Ontology.CS_C_UNDERSTUD:
                objectID= department_id*10000+Integer.parseInt(String.format("%03d", list1.get(2)))*10+1;
                break;
            case Ontology.CS_C_GRADSTUD:
                objectID= department_id*10000+Integer.parseInt(String.format("%03d", list1.get(2)))*10+2;
                break;
            case Ontology.CS_C_TA:
                objectID= department_id*10000+Integer.parseInt(String.format("%03d", list1.get(2)))*10+2;
                break;
            case Ontology.CS_C_COURSE:
                objectID= department_id*1000+Integer.parseInt(String.format("%02d", list1.get(2)))*10+1;
                break;
            case Ontology.CS_C_GRADCOURSE:
                objectID= department_id*1000+Integer.parseInt(String.format("%02d", list1.get(2)))*10+2;
                break;
            case Ontology.CS_C_RESEARCHGROUP:
                objectID= department_id*100+Integer.parseInt(String.format("%02d", list1.get(2)));
                break;
            case Ontology.CS_C_PUBLICATION:
                int main_author_id;
                if (current_url.contains("fullProfessor")) {
                    main_author_id = department_id * 1000 + Integer.parseInt(String.format("%02d", list1.get(2)))*10+1;
                }
                else if (current_url.contains("associateProfessor")){
                    main_author_id = department_id * 1000 + Integer.parseInt(String.format("%02d", list1.get(2)))*10+2;
                }
                else if (current_url.contains("assistantProfessor")){
                    main_author_id = department_id * 1000 + Integer.parseInt(String.format("%02d", list1.get(2)))*10+3;
                }
                else{
                    main_author_id = department_id * 1000 + Integer.parseInt(String.format("%02d", list1.get(2)))*10+4;
                }
                objectID = main_author_id * 100+Integer.parseInt(String.format("%02d", list1.get(3)));
                break;
            default:
                break;
        }
        return objectID;
    }
    protected String getCurrentSubject() {
        if (this.subjects.isEmpty())
            throw new RuntimeException("Mismatched calls to writer in getCurrentSubject()");
        return this.subjects.peek();
    }
    protected String getCurrentType() {
        if (this.type.isEmpty())
            throw new RuntimeException("Mismatched calls to writer in getCurrentSubject()");
        return Ontology.CLASS_TOKEN[this.type.peek()];
    }


    protected abstract void insertPriValue(String className, int valueID);
    protected abstract void insertAttrValue(String propertyType, String valueID, String objectType, boolean isResource);

    @Override
    public final void startSection(int classType, String id) {
        callbackTarget.startSectionCB(classType);
        newSection(classType, id);
    }

    protected void newSection(int classType, String id) {

        this.subjects.push(id);
        this.type.push(classType);
        int department_id = -1;

        String o_type = null;


        List<Integer> list = extractIntfromString(id);
        if(list.size()>1){
            department_id = list.get(1)*100+ Integer.parseInt(String.format("%02d", list.get(0)));
        }

        switch (classType) {
            case Ontology.CS_C_UNIV:
                int university_id = list.get(0);
                insertPriValue(Ontology.CLASS_TOKEN[classType], university_id);
                if(!universityID.contains(university_id))
                    universityID.add(university_id);
                break;
            case Ontology.CS_C_DEPT:
                insertPriValue(Ontology.CLASS_TOKEN[classType], department_id);
                if(!departmentID.contains(department_id))
                    departmentID.add(department_id);
                break;
            case Ontology.CS_C_FULLPROF:
                int fullProfessor_id = department_id*1000+Integer.parseInt(String.format("%02d", list.get(2)))*10+1;
                insertPriValue("faculty", fullProfessor_id);
                if(!facultyID.contains(fullProfessor_id))
                    facultyID.add(fullProfessor_id);
                insertPriValue("professor", fullProfessor_id);
                if(!professorID.contains(fullProfessor_id))
                    professorID.add(fullProfessor_id);
                insertAttrValue("professorType", Integer.toString(fullProfessor_id), "fullProfessor",false);
                break;
            case Ontology.CS_C_ASSOPROF:
                int associateProfessor_id = department_id*1000+Integer.parseInt(String.format("%02d", list.get(2)))*10+2;
                insertPriValue("faculty", associateProfessor_id);
                if(!facultyID.contains(associateProfessor_id))
                    facultyID.add(associateProfessor_id);
                insertPriValue("professor", associateProfessor_id);
                if(!professorID.contains(associateProfessor_id))
                    professorID.add(associateProfessor_id);
                insertAttrValue("professorType", Integer.toString(associateProfessor_id), "associateProfessor",false);
                break;
            case Ontology.CS_C_ASSTPROF:
                int assistantProfessor_id = department_id*1000+Integer.parseInt(String.format("%02d", list.get(2)))*10+3;
                insertPriValue("faculty", assistantProfessor_id);
                if(!facultyID.contains(assistantProfessor_id))
                    facultyID.add(assistantProfessor_id);
                insertPriValue("professor", assistantProfessor_id);
                if(!professorID.contains(assistantProfessor_id))
                    professorID.add(assistantProfessor_id);
                insertAttrValue("professorType", Integer.toString(assistantProfessor_id), "assistantProfessor",false);
                break;
            case Ontology.CS_C_LECTURER:
                int lecturer_id = department_id*1000+Integer.parseInt(String.format("%02d", list.get(2)))*10+4;
                insertPriValue(Ontology.CLASS_TOKEN[classType], lecturer_id);
                if(!lecturerID.contains(lecturer_id))
                    lecturerID.add(lecturer_id);
                insertPriValue("faculty", lecturer_id);
                if(!facultyID.contains(lecturer_id))
                    facultyID.add(lecturer_id);
                break;
            case Ontology.CS_C_UNDERSTUD:
                int underStudent_id = department_id*10000+Integer.parseInt(String.format("%03d", list.get(2)))*10+1;
                insertPriValue(Ontology.CLASS_TOKEN[classType], underStudent_id);
                if(!undergraduateStudentID.contains(underStudent_id))
                    undergraduateStudentID.add(underStudent_id);
                int rand_ug = rand.nextInt((24 - 16) + 1) + 16;
                insertAttrValue("age", Integer.toString(rand_ug), null,false);
                break;
            case Ontology.CS_C_GRADSTUD:
                int graduateStudent_id = department_id*10000+Integer.parseInt(String.format("%03d", list.get(2)))*10+2;
                insertPriValue(Ontology.CLASS_TOKEN[classType], graduateStudent_id);
                if(!graduateStudentID.contains(graduateStudent_id))
                    graduateStudentID.add(graduateStudent_id);
                int rand_g = rand.nextInt((27 - 20) + 1) + 20;
                insertAttrValue("age", Integer.toString(rand_g), null,false);
                break;
            case Ontology.CS_C_COURSE:
                int underCourse_id = department_id*1000+Integer.parseInt(String.format("%02d", list.get(2)))*10+1;
                insertPriValue(Ontology.CLASS_TOKEN[classType], underCourse_id);
                if(!undergraduateCourseID.contains(underCourse_id))
                    undergraduateCourseID.add(underCourse_id);
                break;
            case Ontology.CS_C_GRADCOURSE:
                int graduateCourse_id = department_id*1000+Integer.parseInt(String.format("%02d", list.get(2)))*10+2;
                insertPriValue(Ontology.CLASS_TOKEN[classType], graduateCourse_id);
                if(!graduateCourseID.contains(graduateCourse_id))
                    graduateCourseID.add(graduateCourse_id);
                break;
            case Ontology.CS_C_RESEARCHGROUP:
                int researchGroup_id = department_id*100+Integer.parseInt(String.format("%02d", list.get(2)));
                insertPriValue(Ontology.CLASS_TOKEN[classType], researchGroup_id);
                if(!researchGroupID.contains(researchGroup_id))
                    researchGroupID.add(researchGroup_id);
                break;
            case Ontology.CS_C_PUBLICATION:
                int main_author_id;
                if (id.contains("fullProfessor")) {
                    main_author_id = department_id * 1000 + Integer.parseInt(String.format("%02d", list.get(2)))*10+1;
                }
                else if (id.contains("associateProfessor")){
                    main_author_id = department_id * 1000 + Integer.parseInt(String.format("%02d", list.get(2)))*10+2;
                }
                else if (id.contains("assistantProfessor")){
                    main_author_id = department_id * 1000 + Integer.parseInt(String.format("%02d", list.get(2)))*10+3;
                }
                else{
                    main_author_id = department_id * 1000 + Integer.parseInt(String.format("%02d", list.get(2)))*10+4;
                }
                int publication_id = main_author_id * 100+Integer.parseInt(String.format("%02d", list.get(3)));
                insertPriValue(Ontology.CLASS_TOKEN[classType], publication_id);
                if(!publicationID.contains(publication_id))
                    publicationID.add(publication_id);
                int numberOfWords_t = rand.nextInt((6 - 3) + 1) + 3;
                int numberOfWords_a = rand.nextInt((20 - 10) + 1) + 10;
                String word = null;
                StringBuilder title_p = new StringBuilder();
                StringBuilder abstract_p = new StringBuilder();

                try {

                    for(int i = 0; i<numberOfWords_t;i++){
                        word = generateRamString(this.wordlist);
                        title_wordlist.add(word);
                        title_p.append(word+" ");
                    }

                    for(int i = 0; i<numberOfWords_a;i++){
                        word = generateRamString(this.wordlist);
                        abstract_wordlist.add(word);
                        abstract_p.append(word+" ");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                insertAttrValue("title", title_p.toString(), null,false);
                insertAttrValue("abstract", abstract_p.toString(), null,false);
                break;
            default:
                break;
        }
    }

    @Override
    public final void startAboutSection(int classType, String id) {
        callbackTarget.startAboutSectionCB(classType);
        newSection(classType, id);
    }

    @Override
    public final void endSection(int classType) {
        if (this.subjects.isEmpty())
            throw new RuntimeException("Mismatched calls to writer in endSection()");
        this.subjects.pop();
    }

    @Override
    public final void addProperty(int property, String value, boolean isResource) {
        callbackTarget.addPropertyCB(property);
        int department_id = -1;

        if (this.subjects.isEmpty())
            throw new RuntimeException("Mismatched calls to writer in addProperty()");
        String value_new = value;
        String o_type = null;
        List<Integer> list2 = extractIntfromString(value);
        if(list2.size()>1){
            department_id = list2.get(1)*100+ Integer.parseInt(String.format("%02d", list2.get(0)));
        }

        if(value.contains("@")){
            value_new = value;
        }
        else if ((value.endsWith(".edu"))&(!(value.contains("department")))){
            int university_id = list2.get(0);
            value_new = Integer.toString(university_id);
            if(!universityID.contains(university_id))
                universityID.add(university_id);
            o_type = "university";
        }
        else if(value.endsWith(".edu")&(value.contains("department"))){
            value_new = Integer.toString(department_id);
            o_type = "department";
        }
        else if(value.contains("/")){
            String objectType = value.substring(value.lastIndexOf("/")+1);
            if (objectType.contains("fullProfessor")){
                int fullProfessor_id = department_id*1000+Integer.parseInt(String.format("%02d", list2.get(2)))*10+1;
                value_new = Integer.toString(fullProfessor_id);
                o_type = "professor";

            }
            else if(objectType.contains("associateProfessor")){
                int associateProfessor_id = department_id*1000+Integer.parseInt(String.format("%02d", list2.get(2)))*10+2;
                value_new = Integer.toString(associateProfessor_id);
                o_type = "professor";
            }
            else if(objectType.contains("assistantProfessor")){
                int assistantProfessor_id = department_id*1000+Integer.parseInt(String.format("%02d", list2.get(2)))*10+3;
                value_new = Integer.toString(assistantProfessor_id);
                o_type = "professor";
            }
            else if(objectType.contains("lecturer")){
                int lectureProfessor_id = department_id*1000+Integer.parseInt(String.format("%02d", list2.get(2)))*10+4;
                value_new = Integer.toString(lectureProfessor_id);
                o_type = "lecturer";
            }
            else if(objectType.contains("graduateStudent")){
                int gradStudent_id = department_id*10000+Integer.parseInt(String.format("%03d", list2.get(2)))*10+2;
                value_new = Integer.toString(gradStudent_id);
                o_type = "graduateStudent";
            }
            else if(objectType.contains("undergraduateCourse")){
                int underCourse_id = department_id*1000+Integer.parseInt(String.format("%02d", list2.get(2)))*10+1;
                value_new = Integer.toString(underCourse_id);
                o_type = "undergraduateCourse";
            }
            else if(objectType.contains("graduateCourse")){
                int gradCourse_id = department_id*1000+Integer.parseInt(String.format("%02d", list2.get(2)))*10+2;
                value_new = Integer.toString(gradCourse_id);
                o_type = "graduateCourse";
            }
        }
        else if(value.contains("research")){
            int numberOfWords_r = rand.nextInt((3 - 1) + 1) + 1;

            String word = null;
            StringBuilder interest = new StringBuilder();

            try {
                for(int i = 0; i<numberOfWords_r;i++){
                    word = generateRamString(this.wordlist);
                    interest_wordlist.add(word);
                    interest.append(word+" ");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            value_new = interest.toString();
        }

        String propertyName = String.format("%s", Ontology.PROP_TOKEN[property]);
        insertAttrValue(propertyName, value_new, o_type, isResource);

    }

    @Override
    public final void addProperty(int property, int valueClass, String valueId) {
        callbackTarget.addPropertyCB(property);
        callbackTarget.addValueClassCB(valueClass);

        List<Integer> list3 = extractIntfromString(valueId);
        if(valueClass==Ontology.CS_C_UNIV) {
            int university_id = list3.get(0);
            if(!universityID.contains(university_id))
                universityID.add(university_id);
            insertPriValue(Ontology.CLASS_TOKEN[valueClass], university_id);
            addProperty(property, Integer.toString(university_id), true);
        }

    }


}
