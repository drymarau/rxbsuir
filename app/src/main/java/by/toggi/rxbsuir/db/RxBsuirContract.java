package by.toggi.rxbsuir.db;

import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.time.DayOfWeek;

import by.toggi.rxbsuir.mvp.presenter.LessonListPresenter.SubgroupFilter;

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

        public static class Query {

            private final String syncId;
            private final boolean isGroupSchedule;
            private final SubgroupFilter subgroupFilter;
            private final DayOfWeek weekDay;
            private final Integer weekNumber;
            private final String search;

            private Query(Builder builder) {
                syncId = builder.syncId;
                isGroupSchedule = builder.isGroupSchedule;
                subgroupFilter = builder.subgroupFilter;
                weekDay = builder.weekDay;
                weekNumber = builder.weekNumber;
                search = builder.search;
            }

            public static Builder builder(@Nullable String syncId, boolean isGroupSchedule) {
                return new Builder(syncId, isGroupSchedule);
            }

            @NonNull @Override public String toString() {
                var query = COL_SYNC_ID + " = '" + syncId + "' and " +
                        COL_IS_GROUP_SCHEDULE + " = '" + (isGroupSchedule ? 1 : 0) + "'";
                if (search != null) {
                    query += " and (" + COL_SUBJECT + " || " + COL_EMPLOYEE_LIST + " || " + COL_STUDENT_GROUP_LIST + ") like '%" + search + "%'";
                }
                if (weekDay != null) {
                    query += " and " + COL_WEEKDAY + " = '" + weekDay.toString() + "'";
                }
                if (weekNumber != null) {
                    query += " and " + COL_WEEK_NUMBER_LIST + " like '%" + weekNumber + "%'";
                }
                if (subgroupFilter != null) {
                    switch (subgroupFilter) {
                        case BOTH:
                            query += " and " + COL_NUM_SUBGROUP + " in ('0', '1', '2')";
                            break;
                        case FIRST:
                            query += " and " + COL_NUM_SUBGROUP + " in ('0', '1')";
                            break;
                        case SECOND:
                            query += " and " + COL_NUM_SUBGROUP + " in ('0', '2')";
                            break;
                        case NONE:
                            query += " and " + COL_NUM_SUBGROUP + " = '0'";
                            break;
                    }
                }
                return query;
            }

            public static class Builder {
                // Required parameters
                private final String syncId;
                private final boolean isGroupSchedule;
                // Optional parameters
                private SubgroupFilter subgroupFilter;
                private DayOfWeek weekDay;
                private Integer weekNumber;
                private String search;

                private Builder(@Nullable String syncId, boolean isGroupSchedule) {
                    this.syncId = syncId;
                    this.isGroupSchedule = isGroupSchedule;
                }

                public Builder weekDay(DayOfWeek weekDay) {
                    this.weekDay = weekDay;
                    return this;
                }

                public Builder weekNumber(int weekNumber) {
                    this.weekNumber = weekNumber;
                    return this;
                }

                public Builder subgroupFilter(SubgroupFilter subgroupFilter) {
                    this.subgroupFilter = subgroupFilter;
                    return this;
                }

                public Builder search(String search) {
                    this.search = search;
                    return this;
                }

                public Query build() {
                    return new Query(this);
                }

            }
        }

    }

}
