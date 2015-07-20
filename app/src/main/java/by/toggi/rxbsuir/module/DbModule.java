package by.toggi.rxbsuir.module;

import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

import javax.inject.Singleton;

import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.db.RxBsuirOpenHelper;
import by.toggi.rxbsuir.rest.model.StudentGroup;
import by.toggi.rxbsuir.rest.model.StudentGroupStorIOSQLiteDeleteResolver;
import by.toggi.rxbsuir.rest.model.StudentGroupStorIOSQLiteGetResolver;
import by.toggi.rxbsuir.rest.model.StudentGroupStorIOSQLitePutResolver;
import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {

    @Provides
    @Singleton
    RxBsuirOpenHelper provideOpenHelper(RxBsuirApplication application) {
        return new RxBsuirOpenHelper(application);
    }

    @Provides
    @Singleton
    StorIOSQLite provideStorIOSQLite(RxBsuirOpenHelper openHelper) {
        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(openHelper)
                .addTypeMapping(StudentGroup.class, SQLiteTypeMapping.<StudentGroup>builder()
                        .putResolver(new StudentGroupStorIOSQLitePutResolver())
                        .getResolver(new StudentGroupStorIOSQLiteGetResolver())
                        .deleteResolver(new StudentGroupStorIOSQLiteDeleteResolver())
                        .build()
                ).build();
    }

}
