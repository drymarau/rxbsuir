package by.toggi.rxbsuir.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static by.toggi.rxbsuir.db.RxBsuirContract.StudentGroupEntry;

public class RxBsuirOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "rxbsuir.db";
    public static final int DATABASE_VERSION = 1;

    public RxBsuirOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static String getStudentGroupsCreateQuery() {
        return "create table " +
                StudentGroupEntry.TABLE_NAME + " (" +
                StudentGroupEntry._ID + " integer primary key, " +
                StudentGroupEntry.COL_ID + " integer unique not null, " +
                StudentGroupEntry.COL_FACULTY_ID + " integer not null, " +
                StudentGroupEntry.COL_NAME + " name not null, " +
                StudentGroupEntry.COL_COURSE + " integer not null, " +
                StudentGroupEntry.COL_SPECIALITY_DEPARTMENT_EDUCATION_FORM_ID + " integer not null, " +
                "unique (" + StudentGroupEntry.COL_ID + ") on conflict replace" + ");";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getStudentGroupsCreateQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
