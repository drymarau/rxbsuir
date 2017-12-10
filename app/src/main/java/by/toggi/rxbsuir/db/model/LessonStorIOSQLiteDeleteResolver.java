package by.toggi.rxbsuir.db.model;

import android.support.annotation.NonNull;
import by.toggi.rxbsuir.LessonModel;
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

public class LessonStorIOSQLiteDeleteResolver extends DefaultDeleteResolver<Lesson> {

  @NonNull @Override protected DeleteQuery mapToDeleteQuery(@NonNull Lesson object) {
    return DeleteQuery.builder()
        .table(LessonModel.TABLE_NAME)
        .where(LessonModel._ID + " = ?")
        .whereArgs(object.getId())
        .build();
  }
}
