package edu.lehigh.swat.bench.uba.writers;

import java.io.OutputStream;
import java.util.Stack;
import java.util.ArrayList;

import edu.lehigh.swat.bench.uba.GeneratorCallbackTarget;
import edu.lehigh.swat.bench.uba.GlobalState;
import edu.lehigh.swat.bench.uba.model.Ontology;

public abstract class SQLFlatWriter extends AbstractWriter implements Writer {

    protected static final String RDF_TYPE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    protected static final String OWL_ONTOLOGY = "http://www.w3.org/2002/07/owl#Ontology";
    protected static final String OWL_IMPORTS = "http://www.w3.org/2002/07/owl#imports";

    protected final String ontologyUrl;
    private final Stack<String> subjects = new Stack<String>();
    private final Stack<Integer> type = new Stack<Integer>();
    ArrayList<String> globalURL = new ArrayList<String>();
    ArrayList<String> UniversityNr = new ArrayList<String>();
    ArrayList<String> DepartmentNr = new ArrayList<String>();
    ArrayList<String> FacultyNr = new ArrayList<String>();
    ArrayList<String> FullProfessorNr = new ArrayList<String>();
    ArrayList<String> AssociateProfessorNr = new ArrayList<String>();
    ArrayList<String> AssistantProfessorNr = new ArrayList<String>();
    ArrayList<String> LecturerNr = new ArrayList<String>();
    ArrayList<String> UnderStudentNr = new ArrayList<String>();
    ArrayList<String> GraduateStudentNr = new ArrayList<String>();
    ArrayList<String> UnderCourseNr = new ArrayList<String>();
    ArrayList<String> GraduateCourseNr = new ArrayList<String>();
    ArrayList<String> ResearchGroupNr = new ArrayList<String>();


    public SQLFlatWriter(GeneratorCallbackTarget target, String ontologyUrl) {
        super(target);
        this.ontologyUrl = ontologyUrl;
    }

    @Override
    public void startFile(String fileName, GlobalState state) {
        this.out = prepareOutputStream(fileName, state);
        //addOntologyDeclaration();
    }

    @Override
    public void startFile(GlobalState state, OutputStream output) {
        // No-op
        // For flat file formats which are natively concatenatable calling
        // startFile() multiple times won't matter
    }

    @Override
    public void flushFile(GlobalState state) {
        if (this.out != null)
            this.out.flush();
    }


    @Override
    public void endFile(GlobalState state) {
        if (!subjects.isEmpty())
            throw new RuntimeException("Mismatched calls to writer in endFile()");
        try {
            cleanupOutputStream(this.out);
            this.submitWrites();
        } finally {
            this.out = null;
        }
    }

    @Override
    public void endFile(GlobalState state, OutputStream output) {
        // No-op
    }

    protected int getIdOfCurrentSubject() {
        int objectID = -1;
        if (this.subjects.isEmpty())
            throw new RuntimeException("Mismatched calls to writer in getCurrentSubject()");

        switch (this.type.peek()) {
            case Ontology.CS_C_UNIV:
                objectID=UniversityNr.indexOf(this.subjects.peek());
                break;
            case Ontology.CS_C_DEPT:
                objectID= DepartmentNr.indexOf(this.subjects.peek());
                break;
            case Ontology.CS_C_FACULTY:
                objectID= FacultyNr.indexOf(this.subjects.peek());
                break;
            case Ontology.CS_C_FULLPROF:
                objectID= FullProfessorNr.indexOf(this.subjects.peek());
                break;
            case Ontology.CS_C_ASSOPROF:
                objectID= AssociateProfessorNr.indexOf(this.subjects.peek());
                break;
            case Ontology.CS_C_ASSTPROF:
                objectID= AssistantProfessorNr.indexOf(this.subjects.peek());
                break;
            case Ontology.CS_C_LECTURER:
                objectID= LecturerNr.indexOf(this.subjects.peek());
                break;
            case Ontology.CS_C_UNDERSTUD:
                objectID= UnderStudentNr.indexOf(this.subjects.peek());
                break;
            case Ontology.CS_C_GRADSTUD:
                objectID= GraduateStudentNr.indexOf(this.subjects.peek());
                break;
            case Ontology.CS_C_COURSE:
                objectID= UnderCourseNr.indexOf(this.subjects.peek());
                break;
            case Ontology.CS_C_GRADCOURSE:
                objectID= GraduateCourseNr.indexOf(this.subjects.peek());
                break;
            case Ontology.CS_C_RESEARCHGROUP:
                objectID= ResearchGroupNr.indexOf(this.subjects.peek());
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

    //protected abstract void addTriple(String property, String object, boolean isResource);

    //protected abstract void addTypeTriple(String subject, int classType);

    protected abstract void insertPriValue(String className, int valueID, boolean isResource);
    protected abstract void insertAttrValue(String propertyType, int valueID);

    @Override
    public final void startSection(int classType, String id) {
        callbackTarget.startSectionCB(classType);
        newSection(classType, id);
    }

    protected void newSection(int classType, String id) {

        // Nested section which we don't support directly
        // Link the existing subject to the new subject
        //addTriple(this.getCurrentSubject(), id, true);
        this.subjects.push(id);
        this.type.push(classType);
        //this.type.push(Ontology.CLASS_TOKEN[classType]);
        if(!globalURL.isEmpty() && globalURL.contains(id)){
            switch (classType) {
                case Ontology.CS_C_UNIV:
                    insertPriValue(Ontology.CLASS_TOKEN[classType], UniversityNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_DEPT:
                    insertPriValue(Ontology.CLASS_TOKEN[classType], DepartmentNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_FACULTY:
                    insertPriValue(Ontology.CLASS_TOKEN[classType], FacultyNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_FULLPROF:
                    insertPriValue(Ontology.CLASS_TOKEN[classType], FullProfessorNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_ASSOPROF:
                    insertPriValue(Ontology.CLASS_TOKEN[classType], AssociateProfessorNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_ASSTPROF:
                    insertPriValue(Ontology.CLASS_TOKEN[classType], AssistantProfessorNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_LECTURER:
                    insertPriValue(Ontology.CLASS_TOKEN[classType], LecturerNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_UNDERSTUD:
                    insertPriValue(Ontology.CLASS_TOKEN[classType], UnderStudentNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_GRADSTUD:
                    insertPriValue(Ontology.CLASS_TOKEN[classType], GraduateStudentNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_COURSE:
                    insertPriValue(Ontology.CLASS_TOKEN[classType], UnderCourseNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_GRADCOURSE:
                    insertPriValue(Ontology.CLASS_TOKEN[classType], GraduateCourseNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_RESEARCHGROUP:
                    insertPriValue(Ontology.CLASS_TOKEN[classType], ResearchGroupNr.indexOf(id), true);
                    break;
                default:
                    break;
            }

        }else{
            globalURL.add(id);
            switch (classType) {
                case Ontology.CS_C_UNIV:
                    UniversityNr.add(id);
                    insertPriValue(Ontology.CLASS_TOKEN[classType], UniversityNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_DEPT:
                    DepartmentNr.add(id);
                    insertPriValue(Ontology.CLASS_TOKEN[classType], DepartmentNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_FACULTY:
                    FacultyNr.add(id);
                    insertPriValue(Ontology.CLASS_TOKEN[classType], FacultyNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_FULLPROF:
                    FullProfessorNr.add(id);
                    insertPriValue(Ontology.CLASS_TOKEN[classType], FullProfessorNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_ASSOPROF:
                    AssociateProfessorNr.add(id);
                    insertPriValue(Ontology.CLASS_TOKEN[classType], AssociateProfessorNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_ASSTPROF:
                    AssistantProfessorNr.add(id);
                    insertPriValue(Ontology.CLASS_TOKEN[classType], AssistantProfessorNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_LECTURER:
                    LecturerNr.add(id);
                    insertPriValue(Ontology.CLASS_TOKEN[classType], LecturerNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_UNDERSTUD:
                    UnderStudentNr.add(id);
                    insertPriValue(Ontology.CLASS_TOKEN[classType], UnderStudentNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_GRADSTUD:
                    GraduateStudentNr.add(id);
                    insertPriValue(Ontology.CLASS_TOKEN[classType], GraduateStudentNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_COURSE:
                    UnderCourseNr.add(id);
                    insertPriValue(Ontology.CLASS_TOKEN[classType], UnderCourseNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_GRADCOURSE:
                    GraduateCourseNr.add(id);
                    insertPriValue(Ontology.CLASS_TOKEN[classType], GraduateCourseNr.indexOf(id), true);
                    break;
                case Ontology.CS_C_RESEARCHGROUP:
                    ResearchGroupNr.add(id);
                    insertPriValue(Ontology.CLASS_TOKEN[classType], ResearchGroupNr.indexOf(id), true);
                    break;
                default:
                    break;
            }
        }

        //insertPriValue(Ontology.CLASS_TOKEN[classType], globalURL.indexOf(id), true);
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

        if (this.subjects.isEmpty())
            throw new RuntimeException("Mismatched calls to writer in addProperty()");

        //String propertyUrl = String.format("%s#%s", this.ontologyUrl, Ontology.PROP_TOKEN[property]);
        //addTriple(propertyUrl, value, isResource);
        String propertyName = String.format("#%s", Ontology.PROP_TOKEN[property]);
        //TODO: add column name?
    }

    @Override
    public final void addProperty(int property, int valueClass, String valueId) {
        callbackTarget.addPropertyCB(property);
        callbackTarget.addValueClassCB(valueClass);

        if (this.subjects.isEmpty())
            throw new RuntimeException("Mismatched calls to writer in addTypedProperty()");

        //addProperty(property, valueId, true);
        //ADD the object type triple
        //addTypeTriple(valueId, valueClass);
        if(globalURL.contains(valueId)){
            switch (valueClass) {
                case Ontology.CS_C_UNIV:
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], UniversityNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], UniversityNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_DEPT:
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], DepartmentNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], DepartmentNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_FACULTY:
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], FacultyNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], FacultyNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_FULLPROF:
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], FullProfessorNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], FullProfessorNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_ASSOPROF:
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], AssociateProfessorNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], AssociateProfessorNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_ASSTPROF:
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], AssistantProfessorNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], AssistantProfessorNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_LECTURER:
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], LecturerNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], LecturerNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_UNDERSTUD:
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], UnderStudentNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], UnderStudentNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_GRADSTUD:
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], GraduateStudentNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], GraduateStudentNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_COURSE:
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], UnderCourseNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], UnderCourseNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_GRADCOURSE:
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], GraduateCourseNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], GraduateCourseNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_RESEARCHGROUP:
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], ResearchGroupNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], ResearchGroupNr.indexOf(valueId));
                    break;
                default:
                    break;
            }
        }else{
            globalURL.add(valueId);
            switch (valueClass) {
                case Ontology.CS_C_UNIV:
                    UniversityNr.add(valueId);
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], UniversityNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], UniversityNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_DEPT:
                    DepartmentNr.add(valueId);
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], DepartmentNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], DepartmentNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_FACULTY:
                    FacultyNr.add(valueId);
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], FacultyNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], FacultyNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_FULLPROF:
                    FullProfessorNr.add(valueId);
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], FullProfessorNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], FullProfessorNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_ASSOPROF:
                    AssociateProfessorNr.add(valueId);
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], AssociateProfessorNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], AssociateProfessorNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_ASSTPROF:
                    AssistantProfessorNr.add(valueId);
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], AssistantProfessorNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], AssistantProfessorNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_LECTURER:
                    LecturerNr.add(valueId);
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], LecturerNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], LecturerNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_UNDERSTUD:
                    UnderStudentNr.add(valueId);
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], UnderStudentNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], UnderStudentNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_GRADSTUD:
                    GraduateStudentNr.add(valueId);
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], GraduateStudentNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], GraduateStudentNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_COURSE:
                    UnderCourseNr.add(valueId);
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], UnderCourseNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], UnderCourseNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_GRADCOURSE:
                    GraduateCourseNr.add(valueId);
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], GraduateCourseNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], GraduateCourseNr.indexOf(valueId));
                    break;
                case Ontology.CS_C_RESEARCHGROUP:
                    ResearchGroupNr.add(valueId);
                    insertPriValue(Ontology.CLASS_TOKEN[valueClass], ResearchGroupNr.indexOf(valueId), true);
                    insertAttrValue(Ontology.PROP_TOKEN[property], ResearchGroupNr.indexOf(valueId));
                    break;
                default:
                    break;
            }

        }

        //insertPriValue(Ontology.CLASS_TOKEN[valueClass], globalURL.indexOf(valueId), true);
        //insertAttrValue(Ontology.PROP_TOKEN[property], globalURL.indexOf(valueId));
    }
}
