package by.toggi.rxbsuir.rest.model;

import static by.toggi.rxbsuir.db.RxBsuirContract.StudentGroupEntry;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = StudentGroupEntry.TABLE_NAME)
public class StudentGroup {

    @StorIOSQLiteColumn(name = StudentGroupEntry.COL_ID, key = true)
    public int id;
    @StorIOSQLiteColumn(name = StudentGroupEntry.COL_NAME)
    public String name;
    @StorIOSQLiteColumn(name = StudentGroupEntry.COL_COURSE)
    public int course;
    @StorIOSQLiteColumn(name = StudentGroupEntry.COL_FACULTY_ID)
    public long facultyId;
    @StorIOSQLiteColumn(name = StudentGroupEntry.COL_SPECIALITY_DEPARTMENT_EDUCATION_FORM_ID)
    public long specialityDepartmentEducationFormId;
    @StorIOSQLiteColumn(name = StudentGroupEntry.COL_IS_CACHED)
    public boolean isCached;

    StudentGroup() {
    }

    @Override @NonNull public String toString() {
        return name;
    }
}
