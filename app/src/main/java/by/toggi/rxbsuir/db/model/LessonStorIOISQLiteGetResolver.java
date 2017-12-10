package by.toggi.rxbsuir.db.model;

import android.database.Cursor;
import android.support.annotation.NonNull;
import by.toggi.rxbsuir.LessonModel;
import by.toggi.rxbsuir.rest.model.Employee;
import com.google.gson.Gson;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;
import java.util.ArrayList;
import java.util.Arrays;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalTime;

public class LessonStorIOISQLiteGetResolver extends DefaultGetResolver<Lesson> {

  private final Gson mGson;

  public LessonStorIOISQLiteGetResolver(Gson gson) {
    this.mGson = gson;
  }

  @NonNull @Override public Lesson mapFromCursor(@NonNull Cursor cursor) {
    String[] auditoryArray =
        mGson.fromJson(cursor.getString(cursor.getColumnIndex(LessonModel.AUDITORY_LIST)),
            String[].class);
    if (auditoryArray == null) {
      auditoryArray = new String[] {};
    }
    Employee[] employeeArray =
        mGson.fromJson(cursor.getString(cursor.getColumnIndex(LessonModel.EMPLOYEE_LIST)),
            Employee[].class);
    if (employeeArray == null) {
      employeeArray = new Employee[] {};
    }
    String[] studentGroupArray =
        mGson.fromJson(cursor.getString(cursor.getColumnIndex(LessonModel.STUDENT_GROUP_LIST)),
            String[].class);
    Integer[] weekNumberArray =
        mGson.fromJson(cursor.getString(cursor.getColumnIndex(LessonModel.WEEK_NUMBER_LIST)),
            Integer[].class);

    return new Lesson(cursor.getLong(cursor.getColumnIndex(LessonModel._ID)),
        cursor.getString(cursor.getColumnIndex(LessonModel.SYNC_ID)),
        new ArrayList<>(Arrays.asList(auditoryArray)),
        new ArrayList<>(Arrays.asList(employeeArray)),
        LocalTime.parse(cursor.getString(cursor.getColumnIndex(LessonModel.LESSON_TIME_START))),
        LocalTime.parse(cursor.getString(cursor.getColumnIndex(LessonModel.LESSON_TIME_END))),
        cursor.getString(cursor.getColumnIndex(LessonModel.LESSON_TYPE)),
        cursor.getString(cursor.getColumnIndex(LessonModel.NOTE)),
        cursor.getInt(cursor.getColumnIndex(LessonModel.NUM_SUBGROUP)),
        new ArrayList<>(Arrays.asList(studentGroupArray)),
        cursor.getString(cursor.getColumnIndex(LessonModel.SUBJECT)),
        new ArrayList<>(Arrays.asList(weekNumberArray)),
        DayOfWeek.valueOf(cursor.getString(cursor.getColumnIndex(LessonModel.WEEKDAY))),
        cursor.getInt(cursor.getColumnIndex(LessonModel.IS_GROUP_SCHEDULE)) != 0);
  }
}
