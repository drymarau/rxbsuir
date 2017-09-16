package by.toggi.rxbsuir.dagger.module;

import android.app.Application;
import by.toggi.rxbsuir.dagger.PerApp;
import by.toggi.rxbsuir.db.RxBsuirOpenHelper;
import by.toggi.rxbsuir.db.model.EmployeeStorIOISQLiteGetResolver;
import by.toggi.rxbsuir.db.model.EmployeeStorIOSQLiteDeleteResolver;
import by.toggi.rxbsuir.db.model.EmployeeStorIOSQLitePutResolver;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.db.model.LessonStorIOISQLiteGetResolver;
import by.toggi.rxbsuir.db.model.LessonStorIOSQLiteDeleteResolver;
import by.toggi.rxbsuir.db.model.LessonStorIOSQLitePutResolver;
import by.toggi.rxbsuir.rest.model.Employee;
import by.toggi.rxbsuir.rest.model.StudentGroup;
import by.toggi.rxbsuir.rest.model.StudentGroupStorIOSQLiteDeleteResolver;
import by.toggi.rxbsuir.rest.model.StudentGroupStorIOSQLiteGetResolver;
import by.toggi.rxbsuir.rest.model.StudentGroupStorIOSQLitePutResolver;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import dagger.Module;
import dagger.Provides;

@Module public class DbModule {

  @Provides @PerApp RxBsuirOpenHelper provideOpenHelper(Application application) {
    return new RxBsuirOpenHelper(application);
  }

  @Provides @PerApp StorIOSQLite provideStorIOSQLite(RxBsuirOpenHelper openHelper) {
    return DefaultStorIOSQLite.builder()
        .sqliteOpenHelper(openHelper)
        .addTypeMapping(StudentGroup.class, SQLiteTypeMapping.<StudentGroup>builder().putResolver(
            new StudentGroupStorIOSQLitePutResolver())
            .getResolver(new StudentGroupStorIOSQLiteGetResolver())
            .deleteResolver(new StudentGroupStorIOSQLiteDeleteResolver())
            .build())
        .addTypeMapping(Lesson.class,
            SQLiteTypeMapping.<Lesson>builder().putResolver(new LessonStorIOSQLitePutResolver())
                .getResolver(new LessonStorIOISQLiteGetResolver())
                .deleteResolver(new LessonStorIOSQLiteDeleteResolver())
                .build())
        .addTypeMapping(Employee.class,
            SQLiteTypeMapping.<Employee>builder().putResolver(new EmployeeStorIOSQLitePutResolver())
                .getResolver(new EmployeeStorIOISQLiteGetResolver())
                .deleteResolver(new EmployeeStorIOSQLiteDeleteResolver())
                .build())
        .build();
  }
}
