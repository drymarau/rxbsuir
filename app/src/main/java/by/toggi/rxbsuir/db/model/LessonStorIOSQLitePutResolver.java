package by.toggi.rxbsuir.db.model;

import static by.toggi.rxbsuir.db.RxBsuirContract.LessonEntry;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

public class LessonStorIOSQLitePutResolver extends DefaultPutResolver<Lesson> {

  private final Gson mGson;

  public LessonStorIOSQLitePutResolver(Gson gson) {
    this.mGson = gson;
  }

  @NonNull @Override protected InsertQuery mapToInsertQuery(@NonNull Lesson object) {
    return InsertQuery.builder().table(LessonEntry.TABLE_NAME).build();
  }

  @NonNull @Override protected UpdateQuery mapToUpdateQuery(@NonNull Lesson object) {
    return UpdateQuery.builder()
        .table(LessonEntry.TABLE_NAME)
        .where(LessonEntry._ID + " = ?")
        .whereArgs(object.getId())
        .build();
  }

  @NonNull @Override protected ContentValues mapToContentValues(@NonNull Lesson object) {
    var contentValues = new ContentValues(13);

    contentValues.put(LessonEntry._ID, object.getId());
    contentValues.put(LessonEntry.COL_SYNC_ID, object.getSyncId());
    contentValues.put(LessonEntry.COL_WEEKDAY, object.getWeekday().toString());
    contentValues.put(LessonEntry.COL_LESSON_TIME_START, object.getLessonTimeStart().toString());
    contentValues.put(LessonEntry.COL_LESSON_TIME_END, object.getLessonTimeEnd().toString());
    contentValues.put(LessonEntry.COL_LESSON_TYPE, object.getLessonType());
    contentValues.put(LessonEntry.COL_NOTE, object.getNote());
    contentValues.put(LessonEntry.COL_EMPLOYEE_LIST, mGson.toJson(object.getEmployeeList()));
    contentValues.put(LessonEntry.COL_AUDITORY_LIST, mGson.toJson(object.getAuditoryList()));
    contentValues.put(LessonEntry.COL_NUM_SUBGROUP, object.getNumSubgroup());
    contentValues.put(LessonEntry.COL_SUBJECT, object.getSubject());
    contentValues.put(LessonEntry.COL_STUDENT_GROUP_LIST,
        mGson.toJson(object.getStudentGroupList()));
    contentValues.put(LessonEntry.COL_WEEK_NUMBER_LIST, mGson.toJson(object.getWeekNumberList()));
    contentValues.put(LessonEntry.COL_IS_GROUP_SCHEDULE, object.isGroupSchedule() ? 1 : 0);

    return contentValues;
  }
}
