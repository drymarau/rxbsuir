package by.toggi.rxbsuir.db;

import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import by.toggi.rxbsuir.mvp.presenter.LessonListPresenter.SubgroupFilter;
import org.threeten.bp.DayOfWeek;

public class RxBsuirContract {

  public static class LessonEntry implements BaseColumns {

    static final String COL_SYNC_ID = "sync_id";
    static final String COL_WEEKDAY = "weekday";
    static final String COL_WEEK_NUMBER_LIST = "week_number_list";
    static final String COL_SUBJECT = "subject";
    static final String COL_STUDENT_GROUP_LIST = "student_group_list";
    static final String COL_NUM_SUBGROUP = "num_subgroup";
    static final String COL_EMPLOYEE_LIST = "employee_list";
    static final String COL_IS_GROUP_SCHEDULE = "is_group_schedule";

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

      @Override public String toString() {
        String query = COL_SYNC_ID + " = '" + syncId + "' and " + COL_IS_GROUP_SCHEDULE + " = '" + (
            isGroupSchedule ? 1 : 0) + "'";
        if (search != null) {
          query += " and ("
              + COL_SUBJECT
              + " || "
              + COL_EMPLOYEE_LIST
              + " || "
              + COL_STUDENT_GROUP_LIST
              + ") like '%"
              + search
              + "%'";
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
