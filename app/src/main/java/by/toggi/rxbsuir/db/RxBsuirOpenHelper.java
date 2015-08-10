package by.toggi.rxbsuir.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static by.toggi.rxbsuir.db.RxBsuirContract.AcademicCalendarEntry;
import static by.toggi.rxbsuir.db.RxBsuirContract.EmployeeEntry;
import static by.toggi.rxbsuir.db.RxBsuirContract.LessonEntry;
import static by.toggi.rxbsuir.db.RxBsuirContract.StudentGroupEntry;

public class RxBsuirOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "rxbsuir.db";
    private static final int DATABASE_VERSION = 2;

    public RxBsuirOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static String getAcademicCalendarsCreateQuery() {
        return "create table " +
                AcademicCalendarEntry.TABLE_NAME + " (" +
                AcademicCalendarEntry.COL_COURSE + " integer primary key, " +
                AcademicCalendarEntry.COL_START_DATE + " text not null, " +
                AcademicCalendarEntry.COL_END_DATE + " text not null, " +
                "unique (" + AcademicCalendarEntry.COL_COURSE + ") on conflict replace" + ");";
    }

    private static String getEmployeesCreateQuery() {
        return "create table " +
                EmployeeEntry.TABLE_NAME + " (" +
                EmployeeEntry.COL_ID + " integer primary key, " +
                EmployeeEntry.COL_ACADEMIC_DEPARTMENT_LIST + " text not null, " +
                EmployeeEntry.COL_FIRST_NAME + " text not null, " +
                EmployeeEntry.COL_MIDDLE_NAME + " text, " +
                EmployeeEntry.COL_LAST_NAME + " text not null, " +
                EmployeeEntry.COL_IS_CACHED + " integer not null, " +
                "unique (" + EmployeeEntry.COL_ID + ") on conflict replace" + ");";
    }

    private static String getStudentGroupsCreateQuery() {
        return "create table " +
                StudentGroupEntry.TABLE_NAME + " (" +
                StudentGroupEntry.COL_ID + " integer primary key, " +
                StudentGroupEntry.COL_FACULTY_ID + " integer not null, " +
                StudentGroupEntry.COL_NAME + " text not null, " +
                StudentGroupEntry.COL_COURSE + " integer not null, " +
                StudentGroupEntry.COL_SPECIALITY_DEPARTMENT_EDUCATION_FORM_ID + " integer not null, " +
                StudentGroupEntry.COL_IS_CACHED + " integer not null, " +
                "unique (" + StudentGroupEntry.COL_ID + ") on conflict replace" + ");";
    }

    private static String getLessonsCreateQuery() {
        return "create table " +
                LessonEntry.TABLE_NAME + " (" +
                LessonEntry._ID + " integer primary key, " +
                LessonEntry.COL_SYNC_ID + " text not null, " +
                LessonEntry.COL_EMPLOYEE_LIST + " text, " +
                LessonEntry.COL_AUDITORY_LIST + " text, " +
                LessonEntry.COL_LESSON_TIME_START + " text not null, " +
                LessonEntry.COL_LESSON_TIME_END + " text not null, " +
                LessonEntry.COL_LESSON_TYPE + " text not null, " +
                LessonEntry.COL_NUM_SUBGROUP + " integer not null, " +
                LessonEntry.COL_STUDENT_GROUP_LIST + " text not null, " +
                LessonEntry.COL_SUBJECT + " text not null, " +
                LessonEntry.COL_WEEKDAY + " integer not null, " +
                LessonEntry.COL_NOTE + " text, " +
                LessonEntry.COL_IS_GROUP_SCHEDULE + " integer not null, " +
                LessonEntry.COL_WEEK_NUMBER_LIST + " text not null" + ");";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getAcademicCalendarsCreateQuery());
        db.execSQL(getStudentGroupsCreateQuery());
        db.execSQL(getLessonsCreateQuery());
        db.execSQL(getEmployeesCreateQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion) {
            switch (upgradeTo) {
                case 2:
                    db.execSQL(getAcademicCalendarsCreateQuery());
                    break;
            }
            upgradeTo++;
        }
    }
}
