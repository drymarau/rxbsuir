package by.toggi.rxbsuir.db.model;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

import static by.toggi.rxbsuir.db.RxBsuirContract.LessonEntry;

public class LessonStorIOSQLitePutResolver extends DefaultPutResolver<Lesson> {

    @NonNull
    @Override
    protected InsertQuery mapToInsertQuery(@NonNull Lesson object) {
        return InsertQuery.builder()
                .table(LessonEntry.TABLE_NAME)
                .build();
    }

    @NonNull
    @Override
    protected UpdateQuery mapToUpdateQuery(@NonNull Lesson object) {
        return UpdateQuery.builder()
                .table(LessonEntry.TABLE_NAME)
                .where(LessonEntry._ID + " = ?")
                .whereArgs(object.getId())
                .build();
    }

    @NonNull
    @Override
    protected ContentValues mapToContentValues(@NonNull Lesson object) {
        ContentValues contentValues = new ContentValues(11);

        Gson gson = new Gson();

        contentValues.put(LessonEntry._ID, object.getId());
        contentValues.put(LessonEntry.COL_WEEKDAY, object.getWeekday());
        contentValues.put(LessonEntry.COL_LESSON_TIME, object.getLessonTime());
        contentValues.put(LessonEntry.COL_LESSON_TYPE, object.getLessonType());
        contentValues.put(LessonEntry.COL_NOTE, object.getNote());
        contentValues.put(LessonEntry.COL_EMPLOYEE_LIST, gson.toJson(object.getEmployeeList()));
        contentValues.put(LessonEntry.COL_AUDITORY_LIST, gson.toJson(object.getAuditoryList()));
        contentValues.put(LessonEntry.COL_NUM_SUBGROUP, object.getNumSubgroup());
        contentValues.put(LessonEntry.COL_SUBJECT, object.getSubject());
        contentValues.put(LessonEntry.COL_STUDENT_GROUP_LIST, gson.toJson(object.getStudentGroupList()));
        contentValues.put(LessonEntry.COL_WEEK_NUMBER_LIST, gson.toJson(object.getWeekNumberList()));
        contentValues.put(LessonEntry.COL_IS_GROUP_SCHEDULE, object.isGroupSchedule() ? 1 : 0);

        return contentValues;
    }
}
