package by.toggi.rxbsuir.db;

import android.provider.BaseColumns;

public class RxBsuirContract {

    public static class StudentGroupEntry implements BaseColumns {

        public static final String TABLE_NAME = "students_groups";

        public static final String COL_ID = "id";
        public static final String COL_NAME = "name";
        public static final String COL_COURSE = "course";
        public static final String COL_FACULTY_ID = "faculty_id";
        public static final String COL_SPECIALITY_DEPARTMENT_EDUCATION_FORM_ID = "speciality_department_education_form_id";

    }

}
