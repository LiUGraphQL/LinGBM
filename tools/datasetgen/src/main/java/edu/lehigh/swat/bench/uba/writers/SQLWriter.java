package edu.lehigh.swat.bench.uba.writers;

import edu.lehigh.swat.bench.uba.GeneratorCallbackTarget;
import edu.lehigh.swat.bench.uba.model.Ontology;

public class SQLWriter extends SQLFlatWriter {
    public SQLWriter(GeneratorCallbackTarget target, String ontologyUrl) {
        super(target, ontologyUrl);
    }

    @Override
    protected void insertPriValue(String className, int valueID, boolean isResource){
        if (isResource) {
            out.format("insert into %s(nr) values (%s);", className, valueID);
            out.println();
        }
    };
    @Override
    protected void insertAttrValue(String propertyType, String objeClass, int valueID){
        //insertPriValue(objeClass, valueID, true);
        //out.println();
        out.format("UPDATE %s set %s= %s where nr = %s;", this.getCurrentType(),propertyType, valueID, getIdOfCurrentSubject());
        out.println();
    };

}
