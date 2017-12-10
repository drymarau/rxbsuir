package by.toggi.rxbsuir.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import by.toggi.rxbsuir.EmployeeModel;
import by.toggi.rxbsuir.GroupModel;
import by.toggi.rxbsuir.LessonModel;

public class RxBsuirOpenHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "rxbsuir.db";
  private static final int DATABASE_VERSION = 3;

  public RxBsuirOpenHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override public void onCreate(@NonNull SQLiteDatabase db) {
    db.execSQL(GroupModel.CREATE_TABLE);
    db.execSQL(LessonModel.CREATE_TABLE);
    db.execSQL(EmployeeModel.CREATE_TABLE);
  }

  @Override public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
    int upgradeTo = oldVersion + 1;
    while (upgradeTo <= newVersion) {
      switch (upgradeTo) {
        case 3:
          db.execSQL(LessonModel.SWITCH_SYNC_ID);
          break;
      }
      upgradeTo++;
    }
  }
}
