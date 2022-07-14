package MySche22.com.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;

import java.util.Date;

import MySche22.com.Fragment.ProfileFragment;
import MySche22.com.MainActivity;
import MySche22.com.R;

public class Database extends SQLiteOpenHelper {

    public static final String TB_USER = "USER";
    public static final String TB_PERSONAL = "PERSONAL";
    public static final String TB_GROUPSCHEDULE = "GROUPSCHEDULE";
    public static final String TB_TASK = "todo";


    public static String TB_USER_ACCOUNT = "ACCOUNT";
    public static String TB_USER_PASSWORD = "PASSWORD";
    public static String TB_USER_ID = "USERID";
    public static String TB_USER_FIRSTNAME = "USERFIRSTNAME";
    public static String TB_USER_LASTNAME = "USERLASTNAME";
    public static String TB_USER_PHONENUMBER = "PHONENUMBER";
    public static String TB_USER_EMAIL = "EMAIL";

    public static String TB_PERSONAL_ID = "PERSONALID";
    public static String TB_PERSONAL_TIME = "PERSONALTIME";
    public static String TB_PERSONAL_EVENT = "PERSONALEVENT";

    public static String TB_GROUP_ID = "GROUPID";
    public static String TB_GROUP_MEMBERS = "GROUPMEMBERS";
    public static String TB_GROUP_LEADER = "GROUPLEADER";
    public static String TB_GROUP_EVENT = "GROUPEVENT";
    public static String TB_GROUP_TIME = "GROUPTIME";


    public Database(Context context) {
        super(context, "Mysche", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tbUSER = " CREATE TABLE " + TB_USER + " ( "
                + TB_USER_ACCOUNT + " TEXT , "
                + TB_USER_PASSWORD + " TEXT , "
                + TB_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_USER_FIRSTNAME + " TEXT, "
                + TB_USER_LASTNAME + " TEXT, "
                + TB_USER_EMAIL + " TEXT, "
                + TB_USER_PHONENUMBER + " TEXT )";

        String tbPERSONAL = " CREATE TABLE " + TB_PERSONAL + " ( "
                + TB_PERSONAL_ID + " TEXT PRIMARY KEY , "
                + TB_PERSONAL_EVENT + " TEXT, "
                + TB_PERSONAL_TIME + " DATE )";

        String tbGROUP = " CREATE TABLE " + TB_GROUPSCHEDULE + " ( "
                + TB_GROUP_ID + "INTEGER PRIMARY KEY , "
                + TB_GROUP_LEADER + " TEXT, "
                + TB_GROUP_MEMBERS + " TEXT, "
                + TB_GROUP_EVENT + " TEXT, "
                + TB_GROUP_TIME + " TEXT )";

        db.execSQL(tbUSER);
        db.execSQL(tbPERSONAL);
        db.execSQL(tbGROUP);
        db.execSQL("CREATE TABLE " + TB_TASK
                + "(id INTEGER PRIMARY KEY, "
                + "task TEXT, task_at TEXT, time DATETIME,"
                + " status INTEGER, user TEXT)"
        );


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TB_PERSONAL);
        db.execSQL("DROP TABLE IF EXISTS " + TB_GROUPSCHEDULE);
        db.execSQL("DROP TABLE IF EXISTS " + TB_TASK);
        onCreate(db);
    }

    public Boolean insertData(String ACCOUNT, String PASSWORD, String USERFIRSTNAME,
                              String USERLASTNAME, String PHONENUMBER, String EMAIL) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ACCOUNT", ACCOUNT);
        contentValues.put("PASSWORD", PASSWORD);
        contentValues.put("USERFIRSTNAME", USERFIRSTNAME);
        contentValues.put("USERLASTNAME", USERLASTNAME);
        contentValues.put("PHONENUMBER", PHONENUMBER);
        contentValues.put("EMAIL", EMAIL);

        long result = db.insert("USER", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkUserName(String ACCOUNT) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USER WHERE ACCOUNT = ?"
                , new String[]{ACCOUNT});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkUserNamePassword(String ACCOUNT, String PASSWORD) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from USER where ACCOUNT = ? and PASSWORD = ?"
                , new String[]{ACCOUNT, PASSWORD});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

public Cursor getInfo(String account) {
    SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM " + TB_USER
            + " WHERE " + TB_USER_ACCOUNT + " = '" + account + "'", null);
    return cursor;
}

    public Cursor getInfoNAV(String account) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursorNAV = sqLiteDatabase.rawQuery(" SELECT * FROM " + TB_USER
                + " WHERE " + TB_USER_ACCOUNT + " = '" + account + "'", null);
        return cursorNAV;
    }

    public Cursor getInfo1(String account1) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor1 = sqLiteDatabase.rawQuery(" SELECT * FROM " + TB_USER
                + " WHERE " + TB_USER_ACCOUNT + " = '" + account1 + "'", null);
        return cursor1;
    }

///////////////////////////////////////////TASK////////////////////////////////////////////////////




public boolean insertTask(String task, String task_at, String account, String time) {
    Date date;
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("task", task);
    contentValues.put("task_at", task_at);
    contentValues.put("time", time);
    contentValues.put("status", 0);
    contentValues.put("user", account);
    db.insert(TB_TASK, null, contentValues);
    return true;
}

    public boolean updateTask(String id, String task, String task_at, String time, String account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task", task);
        contentValues.put("task_at", task_at);
        contentValues.put("time", time);
        contentValues.put("user",  account);
        db.update(TB_TASK, contentValues, "id = ? ", new String[]{id});
        return true;
    }

    public boolean deleteTask(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TB_TASK, "id = ? ", new String[]{id});
        return true;
    }

    public boolean updateTaskStatus(String id, Integer status, String task_at, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        contentValues.put("task_at", task_at);
        contentValues.put("time", time);
        db.update(TB_TASK, contentValues, "id = ? ", new String[]{id});
        return true;
    }


    public Cursor getSingleTask(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TB_TASK + " WHERE id = '" + id
                + "' order by id desc", null);
        return res;

    }

    public Cursor getTodayTask( String account) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TB_TASK
                + " WHERE date(task_at) = date('now', 'localtime')" + " AND user = " + account , null);
//        + " AND " + "user=" + account
        return res;

    }


    public Cursor getTomorrowTask(String account) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TB_TASK
                + " WHERE date(task_at) = date('now', '+1 day', 'localtime')" + " AND user = " + account, null);
//        + " AND user(account) = " + account
        return res;

    }


    public Cursor getUpcomingTask(String account) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TB_TASK
                + " WHERE date(task_at) > date('now', '+1 day', 'localtime')"+ " AND user = " + account, null);
        return res;

    }

    public Cursor getPreviousdaysTask() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TB_TASK
                + " WHERE date(task_at) = date('now', '-1 day', 'localtime') order by id desc", null);
        return res;

    }

}