package by.toggi.rxbsuir.db.model;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import by.toggi.rxbsuir.LessonModel;
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
    return InsertQuery.builder().table(LessonModel.TABLE_NAME).build();
  }

  @NonNull @Override protected UpdateQuery mapToUpdateQuery(@NonNull Lesson object) {
    return UpdateQuery.builder()
        .table(LessonModel.TABLE_NAME)
        .where(LessonModel._ID + " = ?")
        .whereArgs(object.getId())
        .build();
  }

  @NonNull @Override protected ContentValues mapToContentValues(@NonNull Lesson object) {
    ContentValues contentValues = new ContentValues(13);

    contentValues.put(LessonModel._ID, object.getId());
    contentValues.put(LessonModel.SYNC_ID, object.getSyncId());
    contentValues.put(LessonModel.WEEKDAY, object.getWeekday().toString());
    contentValues.put(LessonModel.LESSON_TIME_START, object.getLessonTimeStart().toString());
    contentValues.put(LessonModel.LESSON_TIME_END, object.getLessonTimeEnd().toString());
    contentValues.put(LessonModel.LESSON_TYPE, object.getLessonType());
    contentValues.put(LessonModel.NOTE, object.getNote());
    contentValues.put(LessonModel.EMPLOYEE_LIST, mGson.toJson(object.getEmployeeList()));
    contentValues.put(LessonModel.AUDITORY_LIST, mGson.toJson(object.getAuditoryList()));
    contentValues.put(LessonModel.NUM_SUBGROUP, object.getNumSubgroup());
    contentValues.put(LessonModel.SUBJECT, object.getSubject());
    contentValues.put(LessonModel.STUDENT_GROUP_LIST, mGson.toJson(object.getStudentGroupList()));
    contentValues.put(LessonModel.WEEK_NUMBER_LIST, mGson.toJson(object.getWeekNumberList()));
    contentValues.put(LessonModel.IS_GROUP_SCHEDULE, object.isGroupSchedule() ? 1 : 0);

    return contentValues;
  }
}
