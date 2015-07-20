package by.toggi.rxbsuir.rest.model;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import org.simpleframework.xml.Element;

import static by.toggi.rxbsuir.db.RxBsuirContract.StudentGroupEntry;

@StorIOSQLiteType(table = StudentGroupEntry.TABLE_NAME)
public class StudentGroup {

    @StorIOSQLiteColumn(name = StudentGroupEntry.COL_ID, key = true)
    @Element(name = "id")
    long id;
    @StorIOSQLiteColumn(name = StudentGroupEntry.COL_NAME)
    @Element(name = "name")
    int name;
    @StorIOSQLiteColumn(name = StudentGroupEntry.COL_COURSE)
    @Element(name = "course")
    int course;
    @StorIOSQLiteColumn(name = StudentGroupEntry.COL_FACULTY_ID)
    @Element(name = "facultyId")
    long facultyId;
    @StorIOSQLiteColumn(name = StudentGroupEntry.COL_SPECIALITY_DEPARTMENT_EDUCATION_FORM_ID)
    @Element(name = "specialityDepartmentEducationFormId")
    long specialityDepartmentEducationFormId;

    StudentGroup() {}

}
