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
    ArrayList<String> globalURL = new ArrayList<String>();
    private final Stack<String> type = new Stack<String>();

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
        if (this.subjects.isEmpty())
            throw new RuntimeException("Mismatched calls to writer in getCurrentSubject()");
        return globalURL.indexOf(this.subjects.peek());
    }
    protected String getCurrentSubject() {
        if (this.subjects.isEmpty())
            throw new RuntimeException("Mismatched calls to writer in getCurrentSubject()");
        return this.subjects.peek();
    }
    protected String getCurrentType() {
        if (this.type.isEmpty())
            throw new RuntimeException("Mismatched calls to writer in getCurrentSubject()");
        return this.type.peek();
    }

    //protected abstract void addTriple(String property, String object, boolean isResource);

    //protected abstract void addTypeTriple(String subject, int classType);

    protected abstract void insertPriValue(String className, int valueID, boolean isResource);
    protected abstract void insertAttrValue(String propertyType, String objectName, int valueID);

    @Override
    public final void startSection(int classType, String id) {
        callbackTarget.startSectionCB(classType);
        newSection(classType, id);
    }

    protected void newSection(int classType, String id) {
        if (!this.subjects.isEmpty()) {
            // Nested section which we don't support directly
            // Link the existing subject to the new subject
            //addTriple(this.getCurrentSubject(), id, true);
            this.subjects.push(id);
            this.type.push(Ontology.CLASS_TOKEN[classType]);
            if(globalURL.contains(id)){

            }else
                globalURL.add(id);

            // Add type triple
            //insert primary key value
            //addTypeTriple(id, classType);
        } else {
            // Top level section
            this.subjects.push(id);
            this.type.push(Ontology.CLASS_TOKEN[classType]);
            if(globalURL.contains(id)){

            }else
                globalURL.add(id);
            //addTypeTriple(id, classType);
        }
        insertPriValue(Ontology.CLASS_TOKEN[classType], globalURL.indexOf(id), true);
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

        }else
            globalURL.add(valueId);
        insertPriValue(Ontology.CLASS_TOKEN[valueClass], globalURL.indexOf(valueId), true);
        insertAttrValue(Ontology.PROP_TOKEN[property], valueId, globalURL.indexOf(valueId));
    }
}
