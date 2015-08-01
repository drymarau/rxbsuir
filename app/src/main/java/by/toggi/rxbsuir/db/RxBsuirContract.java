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
         * Filter by group.
         *
         * @param groupNumber the group number
         * @return where query
         */
        public static String filterByGroup(String groupNumber) {
            return COL_SYNC_ID + " = " + groupNumber + " and " + filterByScheduleType(true);
        }

        /**
         * Filter by employee.
         *
         * @param employeeId the employee id
         * @return where query
         */
        public static String filterByEmployee(String employeeId) {
            return COL_SYNC_ID + " = " + employeeId + " and " + filterByScheduleType(false);
        }

        /**
         * Filter by group, subgroup, and week.
         *
         * @param groupNumber    the group number
         * @param subgroupNumber the subgroup number
         * @param weekNumber     the week number
         * @return where query
         */
        public static String filterByGroupSubgroupAndWeek(String groupNumber, int subgroupNumber, int weekNumber) {
            return filterByGroupAndWeek(groupNumber, weekNumber)
                    + " and (" + filterBySubgroup(subgroupNumber) + ")";
        }

        /**
         * Filter by employee, subgroup and week.
         *
         * @param employeeId     the employee id
         * @param subgroupNumber the subgroup number
         * @param weekNumber     the week number
         * @return where query
         */
        public static String filterByEmployeeSubgroupAndWeek(String employeeId, int subgroupNumber, int weekNumber) {
            return filterByEmployeeAndWeek(employeeId, weekNumber)
                    + " and (" + filterBySubgroup(subgroupNumber) + ")";
        }

        private static String filterByWeek(int weekNumber) {
            return COL_WEEK_NUMBER_LIST + " like '%" + weekNumber + "%'";
        }

        private static String filterByScheduleType(boolean isGroupSchedule) {
            return COL_IS_GROUP_SCHEDULE + " = " + (isGroupSchedule ? 1 : 0);
        }

        private static String filterByGroupAndWeek(String groupNumber, int weekNumber) {
            return filterByGroup(groupNumber) + " and " + filterByWeek(weekNumber);
        }

        private static String filterByEmployeeAndWeek(String employeeId, int weekNumber) {
            return filterByEmployee(employeeId) + " and " + filterByWeek(weekNumber);
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
