package by.toggi.rxbsuir.rest.model;

import by.toggi.rxbsuir.GroupModel;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = GroupModel.TABLE_NAME) public class StudentGroup {

  @StorIOSQLiteColumn(name = GroupModel.ID, key = true) public int id;
  @StorIOSQLiteColumn(name = GroupModel.NAME) public String name;
  @StorIOSQLiteColumn(name = GroupModel.COURSE) public int course;
  @StorIOSQLiteColumn(name = GroupModel.FACULTY_ID) public long facultyId;
  @StorIOSQLiteColumn(name = GroupModel.SPECIALITY_DEPARTMENT_EDUCATION_FORM_ID) public long
      specialityDepartmentEducationFormId;
  @StorIOSQLiteColumn(name = GroupModel.IS_CACHED) public boolean isCached;

  StudentGroup() {
  }

  @Override public String toString() {
    return name;
  }
}
