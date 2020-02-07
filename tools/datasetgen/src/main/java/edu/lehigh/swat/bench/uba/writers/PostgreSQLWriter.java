package edu.lehigh.swat.bench.uba.writers;

import edu.lehigh.swat.bench.uba.GeneratorCallbackTarget;
import edu.lehigh.swat.bench.uba.model.Ontology;

public class PostgreSQLWriter extends SQLFlatWriter {
    public PostgreSQLWriter(GeneratorCallbackTarget target, String ontologyUrl) {
        super(target, ontologyUrl);
    }

    @Override
    protected void insertPriValue(String className, int valueID){
        out.format("insert into %s(nr) values (%s) ON CONFLICT (nr) DO NOTHING;", className, valueID);
        out.println();
    };
    @Override
    protected void insertAttrValue(String propertyType, String valueID, String objectType, boolean isResource){
        if (isResource) {
            if(propertyType.equals("teacherOf")){
                insertPriValue(objectType, Integer.parseInt(valueID));
                out.format("UPDATE %s set %s=%s where nr=%s;", objectType, "teacher", getIdOfCurrentSubject(), valueID);
            }
            else if(propertyType.equals("publicationAuthor")){
                if((objectType.contains("professor"))||(objectType.equals("lecturer"))){
                    out.format("UPDATE %s set mainAuthor=%s where nr=%s;", this.getCurrentType(), valueID, getIdOfCurrentSubject());
                }
                else if(objectType.equals("graduateStudent")){
                    out.format("insert into coAuthorOfPublication(publicationID, graduateStudentID) values (%s, %s) ON CONFLICT (publicationID, graduateStudentID) DO NOTHING;", getIdOfCurrentSubject(), valueID);
                }
            }
            else if(propertyType.equals("teachingAssistantOf")){
                insertPriValue(objectType, Integer.parseInt(valueID));
                out.format("UPDATE %s set %s=%s where nr=%s;", "undergraduateCourse", "teachingAssistant", getIdOfCurrentSubject(), valueID);
            }
            else if(propertyType.equals("takesCourse")&&(objectType.equals("undergraduateCourse"))){
                out.format("insert into undergraduateStudentTakeCourse(undergraduateStudentID, undergraduateCourseID) values (%s, %s) ON CONFLICT (undergraduateStudentID, undergraduateCourseID) DO NOTHING;",getIdOfCurrentSubject(), valueID);
            }
            else if(propertyType.equals("takesCourse")&&(objectType.equals("graduateCourse"))){
                out.format("insert into graduateStudentTakeCourse(graduateStudentID, graduateCourseID) values (%s, %s) ON CONFLICT (graduateStudentID, graduateCourseID) DO NOTHING;",getIdOfCurrentSubject(), valueID);
            }
            else if(this.getCurrentType().equals("fullProfessor")){
                if (propertyType.equals("headOf")){
                    out.format("UPDATE %s set %s=%s where nr=%s;","professor", propertyType, valueID, getIdOfCurrentSubject());
                }
                else{
                    out.format("UPDATE %s set %s=%s where nr=%s;","faculty", propertyType, valueID, getIdOfCurrentSubject());
                }
            }
            else if(this.getCurrentType().equals("associateProfessor")){
                out.format("UPDATE %s set %s=%s where nr=%s;","faculty", propertyType, valueID, getIdOfCurrentSubject());
            }
            else if(this.getCurrentType().equals("assistantProfessor")){
                out.format("UPDATE %s set %s=%s where nr=%s;","faculty", propertyType, valueID, getIdOfCurrentSubject());
            }
            else if(this.getCurrentType().equals("lecturer")){
                out.format("UPDATE %s set %s=%s where nr=%s;","faculty", propertyType, valueID, getIdOfCurrentSubject());
            }
            else{
                out.format("UPDATE %s set %s=%s where nr=%s;", this.getCurrentType(), propertyType, valueID, getIdOfCurrentSubject());
            }

        } else {
            if(propertyType.equals("age")){
                out.format("UPDATE %s set %s=%s where nr=%s;",this.getCurrentType(), "age", valueID, getIdOfCurrentSubject());
            }
            else if(this.getCurrentType().equals("fullProfessor")){
                if(propertyType.equals("researchInterest")){
                    out.format("UPDATE %s set %s=\'%s\' where nr=%s;","professor", propertyType, valueID, getIdOfCurrentSubject());
                }
                else if(propertyType.equals("professorType")){
                    out.format("UPDATE %s set %s=\'%s\' where nr=%s;","professor", "professorType", objectType, valueID);
                }
                else{
                    out.format("UPDATE %s set %s=\'%s\' where nr=%s;","faculty", propertyType, valueID, getIdOfCurrentSubject());
                }
            }
            else if(this.getCurrentType().equals("associateProfessor")){
                if(propertyType.equals("researchInterest")){
                    out.format("UPDATE %s set %s=\'%s\' where nr=%s;","professor", propertyType, valueID, getIdOfCurrentSubject());
                }
                else if(propertyType.equals("professorType")){
                    out.format("UPDATE %s set %s=\'%s\' where nr=%s;","professor", "professorType", objectType, valueID);
                }
                else{
                    out.format("UPDATE %s set %s=\'%s\' where nr=%s;","faculty", propertyType, valueID, getIdOfCurrentSubject());
                }
            }
            else if(this.getCurrentType().equals("assistantProfessor")){
                if(propertyType.equals("researchInterest")){
                    out.format("UPDATE %s set %s=\'%s\' where nr=%s;","professor", propertyType, valueID, getIdOfCurrentSubject());
                }
                else if(propertyType.equals("professorType")){
                    out.format("UPDATE %s set %s=\'%s\' where nr=%s;","professor", "professorType", objectType, valueID);
                }
                else{
                    out.format("UPDATE %s set %s=\'%s\' where nr=%s;","faculty", propertyType, valueID, getIdOfCurrentSubject());
                }
            }
            else if(this.getCurrentType().equals("lecturer")){
                out.format("UPDATE %s set %s=\'%s\' where nr=%s;","faculty", propertyType, valueID, getIdOfCurrentSubject());
            }
            else{
                out.format("UPDATE %s set %s=\'%s\' where nr=%s;", this.getCurrentType(), propertyType, valueID, getIdOfCurrentSubject());
            }
        }
        out.println();

    };

}
