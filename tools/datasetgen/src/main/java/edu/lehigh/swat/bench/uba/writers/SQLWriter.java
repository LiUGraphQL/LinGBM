package edu.lehigh.swat.bench.uba.writers;

import edu.lehigh.swat.bench.uba.GeneratorCallbackTarget;
import edu.lehigh.swat.bench.uba.model.Ontology;

public class SQLWriter extends SQLFlatWriter {
    public SQLWriter(GeneratorCallbackTarget target, String ontologyUrl) {
        super(target, ontologyUrl);
    }

    @Override
    protected void insertPriValue(String className, int valueID){
        out.format("insert ignore into %s(nr) values (%s);", className, valueID);
        out.println();
    };
    @Override
    protected void insertAttrValue(String propertyType, String valueID, String objectType, boolean isResource){
        if (isResource) {
            if(propertyType.equals("teacherOf")){
                out.format("UPDATE %s set %s= %s where nr = %s;", objectType, "teacher", getIdOfCurrentSubject(), valueID);
            }
            else if(propertyType.equals("publicationAuthor")){
                if((objectType.contains("professor"))||(objectType.equals("lecturer"))){
                    out.format("UPDATE %s set mainAuthor= %s where nr = %s;", this.getCurrentType(), valueID, getIdOfCurrentSubject());
                }
                else if(objectType.equals("graduateStudent")){
                    out.format("insert ignore into coAuthorOfPublication(publicationID, graduateStudentID) values (%s, %s);", getIdOfCurrentSubject(), valueID);
                }
            }
            else if(propertyType.equals("teachingAssistantOf")){
                //System.out.println(getIdOfCurrentSubject());
                out.format("UPDATE %s set %s= %s where nr = %s;", "undergraduateCourse", "teachingAssistant", getIdOfCurrentSubject(), valueID);
            }
            else if(propertyType.equals("takesCourse")&&(objectType.equals("undergraduateCourse"))){
                out.format("insert ignore into undergraduateStudentTakeCourse(undergraduateStudentID, undergraduateCourseID) values (%s, %s);",getIdOfCurrentSubject(), valueID);
            }
            else if(propertyType.equals("takesCourse")&&(objectType.equals("graduateCourse"))){
                out.format("insert ignore into graduateStudentTakeCourse(graduateStudentID, graduateCourseID) values (%s, %s);",getIdOfCurrentSubject(), valueID);
            }
            else if(this.getCurrentType().equals("fullProfessor")){
                if (propertyType.equals("headOf")){
                    out.format("UPDATE %s set %s= %s where nr = %s;","professor", propertyType, valueID, getIdOfCurrentSubject());
                }
                else{
                    out.format("UPDATE %s set %s= %s where nr = %s;","faculty", propertyType, valueID, getIdOfCurrentSubject());
                }
            }
            else if(this.getCurrentType().equals("associateProfessor")){
                out.format("UPDATE %s set %s= %s where nr = %s;","faculty", propertyType, valueID, getIdOfCurrentSubject());
            }
            else if(this.getCurrentType().equals("assistantProfessor")){
                out.format("UPDATE %s set %s= %s where nr = %s;","faculty", propertyType, valueID, getIdOfCurrentSubject());
            }
            else if(this.getCurrentType().equals("lecturer")){
                out.format("UPDATE %s set %s= %s where nr = %s;","faculty", propertyType, valueID, getIdOfCurrentSubject());
            }
            else{
                out.format("UPDATE %s set %s= %s where nr = %s;", this.getCurrentType(), propertyType, valueID, getIdOfCurrentSubject());
            }

        } else {
            if(propertyType.equals("age")){
                out.format("UPDATE %s set %s= %s where nr = %s;",this.getCurrentType(), "age", valueID, getIdOfCurrentSubject());
            }
            else if(this.getCurrentType().equals("fullProfessor")){
                if(propertyType.equals("researchInterest")){
                    out.format("UPDATE %s set %s= \"%s\" where nr = %s;","professor", propertyType, valueID, getIdOfCurrentSubject());
                }
                else if(propertyType.equals("professorType")){
                    out.format("UPDATE %s set %s= \"%s\" where nr = %s;","professor", "professorType", objectType, valueID);
                }
                else{
                    out.format("UPDATE %s set %s= \"%s\" where nr = %s;","faculty", propertyType, valueID, getIdOfCurrentSubject());
                }
            }
            else if(this.getCurrentType().equals("associateProfessor")){
                if(propertyType.equals("researchInterest")){
                    out.format("UPDATE %s set %s= \"%s\" where nr = %s;","professor", propertyType, valueID, getIdOfCurrentSubject());
                }
                else if(propertyType.equals("professorType")){
                    out.format("UPDATE %s set %s= \"%s\" where nr = %s;","professor", "professorType", objectType, valueID);
                }
                else{
                    out.format("UPDATE %s set %s= \"%s\" where nr = %s;","faculty", propertyType, valueID, getIdOfCurrentSubject());
                }
                out.format("UPDATE %s set %s= \"%s\" where nr = %s;","faculty", propertyType, valueID, getIdOfCurrentSubject());
            }
            else if(this.getCurrentType().equals("assistantProfessor")){
                if(propertyType.equals("researchInterest")){
                    out.format("UPDATE %s set %s= \"%s\" where nr = %s;","professor", propertyType, valueID, getIdOfCurrentSubject());
                }
                else if(propertyType.equals("professorType")){
                    out.format("UPDATE %s set %s= \"%s\" where nr = %s;","professor", "professorType", objectType, valueID);
                }
                else{
                    out.format("UPDATE %s set %s= \"%s\" where nr = %s;","faculty", propertyType, valueID, getIdOfCurrentSubject());
                }
            }
            else if(this.getCurrentType().equals("lecturer")){
                out.format("UPDATE %s set %s= \"%s\" where nr = %s;","faculty", propertyType, valueID, getIdOfCurrentSubject());
            }
            else{
                out.format("UPDATE %s set %s= \"%s\" where nr = %s;", this.getCurrentType(), propertyType, valueID, getIdOfCurrentSubject());
            }
        }
        out.println();

    };

}
