package by.toggi.rxbsuir.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static by.toggi.rxbsuir.db.RxBsuirContract.LessonEntry;
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
                StudentGroupEntry.COL_ID + " integer primary key, " +
                StudentGroupEntry.COL_FACULTY_ID + " integer not null, " +
                StudentGroupEntry.COL_NAME + " name not null, " +
                StudentGroupEntry.COL_COURSE + " integer not null, " +
                StudentGroupEntry.COL_SPECIALITY_DEPARTMENT_EDUCATION_FORM_ID + " integer not null, " +
                "unique (" + StudentGroupEntry.COL_ID + ") on conflict replace" + ");";
    }

    private static String getLessonsCreateQuery() {
        return "create table " +
                LessonEntry.TABLE_NAME + " (" +
                LessonEntry._ID + " integer primary key, " +
                LessonEntry.COL_EMPLOYEE_LIST + " text, " +
                LessonEntry.COL_AUDITORY_LIST+ " text, " +
                LessonEntry.COL_LESSON_TIME + " text not null, " +
                LessonEntry.COL_LESSON_TYPE + " text not null, " +
                LessonEntry.COL_NUM_SUBGROUP + " integer not null, " +
                LessonEntry.COL_STUDENT_GROUP_LIST + " text not null, " +
                LessonEntry.COL_SUBJECT + " text not null, " +
                LessonEntry.COL_WEEKDAY + " integer not null, " +
                LessonEntry.COL_NOTE + " text, " +
                LessonEntry.COL_WEEK_NUMBER_LIST+ " text not null" + ");";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getStudentGroupsCreateQuery());
        db.execSQL(getLessonsCreateQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + StudentGroupEntry.TABLE_NAME);
        db.execSQL("drop table if exists " + LessonEntry.TABLE_NAME);
        onCreate(db);
    }
}
