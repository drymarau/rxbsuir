package by.toggi.rxbsuir.rest.model;

import org.simpleframework.xml.Element;

public class StudentGroup {

    @Element(name = "id")
    public int id;
    @Element(name = "name")
    public int name;
    @Element(name = "course")
    public int course;
    @Element(name = "facultyId")
    public long facultyId;
    @Element(name = "specialityDepartmentEducationFormId")
    public long specialityDepartmentEducationFormId;

}
