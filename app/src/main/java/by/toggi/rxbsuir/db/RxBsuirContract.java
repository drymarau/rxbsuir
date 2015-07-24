package by.toggi.rxbsuir.db;

import android.provider.BaseColumns;

public class RxBsuirContract {

    public static class StudentGroupEntry {

        public static final String TABLE_NAME = "students_groups";

        public static final String COL_ID = "id";
        public static final String COL_NAME = "name";
        public static final String COL_COURSE = "course";
        public static final String COL_FACULTY_ID = "faculty_id";
        public static final String COL_SPECIALITY_DEPARTMENT_EDUCATION_FORM_ID = "speciality_department_education_form_id";

    }

    public static class LessonEntry implements BaseColumns {

        public static final String TABLE_NAME = "lessons";

        public static final String COL_WEEKDAY = "weekday";
        public static final String COL_WEEK_NUMBER_LIST = "week_number_list";
        public static final String COL_SUBJECT = "subject";
        public static final String COL_STUDENT_GROUP_LIST = "student_group_list";
        public static final String COL_NUM_SUBGROUP = "num_subgroup";
        public static final String COL_NOTE = "note";
        public static final String COL_LESSON_TIME = "lesson_time";
        public static final String COL_LESSON_TYPE = "lesson_type";
        public static final String COL_EMPLOYEE_LIST = "employee_list";
        public static final String COL_AUDITORY_LIST = "auditory_list";
        public static final String COL_IS_GROUP_SCHEDULE = "is_group_schedule";

        public static String filterByWeekNumber(int weekNumber) {
            return COL_WEEK_NUMBER_LIST + " like '%" + weekNumber + "%'";
        }

        public static String filterByGroupNumber(String groupNumber) {
            return COL_STUDENT_GROUP_LIST + " like '%" + groupNumber + "%'";
        }

        public static String filterByGroupNumberAndWeekNumber(String groupNumber, int weekNumber) {
            return filterByGroupNumber(groupNumber) + " and " + filterByWeekNumber(weekNumber);
        }

    }

}
