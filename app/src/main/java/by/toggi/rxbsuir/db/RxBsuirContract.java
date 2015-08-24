package by.toggi.rxbsuir.db;

import android.provider.BaseColumns;

public class RxBsuirContract {

    public static class EmployeeEntry implements HelperColumns {

        public static final String TABLE_NAME = "employees";

        public static final String COL_ID = "id";
        public static final String COL_ACADEMIC_DEPARTMENT_LIST = "academic_department_list";
        public static final String COL_FIRST_NAME = "first_name";
        public static final String COL_MIDDLE_NAME = "middle_name";
        public static final String COL_LAST_NAME = "last_name";

    }

    public static class StudentGroupEntry implements HelperColumns {

        public static final String TABLE_NAME = "students_groups";

        public static final String COL_ID = "id";
        public static final String COL_NAME = "name";
        public static final String COL_COURSE = "course";
        public static final String COL_FACULTY_ID = "faculty_id";
        public static final String COL_SPECIALITY_DEPARTMENT_EDUCATION_FORM_ID = "speciality_department_education_form_id";

    }

    public static class LessonEntry implements BaseColumns {

        public static final String TABLE_NAME = "lessons";

        public static final String COL_SYNC_ID = "sync_id";
        public static final String COL_WEEKDAY = "weekday";
        public static final String COL_WEEK_NUMBER_LIST = "week_number_list";
        public static final String COL_SUBJECT = "subject";
        public static final String COL_STUDENT_GROUP_LIST = "student_group_list";
        public static final String COL_NUM_SUBGROUP = "num_subgroup";
        public static final String COL_NOTE = "note";
        public static final String COL_LESSON_TIME_START = "lesson_time_start";
        public static final String COL_LESSON_TIME_END = "lesson_time_end";
        public static final String COL_LESSON_TYPE = "lesson_type";
        public static final String COL_EMPLOYEE_LIST = "employee_list";
        public static final String COL_AUDITORY_LIST = "auditory_list";
        public static final String COL_IS_GROUP_SCHEDULE = "is_group_schedule";

        /**
         * Get sql query with syncId and schedule type.
         *
         * @return where query
         */
        public static String getSyncIdAndTypeQuery() {
            return COL_SYNC_ID + " = ?" + " and " + COL_IS_GROUP_SCHEDULE + " = ?";
        }

        /**
         * Gets sync id type subgroup and week number query.
         *
         * @param subgroupNumber the subgroup number
         * @return where query
         */
        public static String getSyncIdTypeSubgroupAndWeekNumberQuery(int subgroupNumber) {
            return getSyncIdAndTypeQuery() + " and " + COL_WEEK_NUMBER_LIST + " like ?" + " and (" + filterBySubgroup(subgroupNumber) + ")";
        }

        private static String filterBySubgroup(int subgroupNumber) {
            String commonQuery = COL_NUM_SUBGROUP + " = 0";
            String subgroup1Query = COL_NUM_SUBGROUP + " = 1";
            String subgroup2Query = COL_NUM_SUBGROUP + " = 2";
            switch (subgroupNumber) {
                case 0:
                    return commonQuery + " or " + subgroup1Query + " or " + subgroup2Query;
                case 1:
                    return commonQuery + " or " + subgroup1Query;
                case 2:
                    return commonQuery + " or " + subgroup2Query;
                case 3:
                    return commonQuery;
                default:
                    throw new IllegalArgumentException("Unknown subgroup number: " + subgroupNumber);
            }
        }

    }

}
