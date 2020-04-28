package com.david.coursework;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.TextView;

import static com.david.coursework.AddBulletJournal.currentDate;

public class DatabaseManipulator {

    private static final String DATABASE_NAME = "mydatabase.db";
    private static int DATABASE_VERSION = 2;
    static final String TABLE_NAME = "newtable";
    static final String TABLE_MONKEY = "monkeydiary";
    static final String TABLE_QUOTE = "quotetable";
    private static Context context;
    static SQLiteDatabase db;
    private SQLiteStatement insertStmt;
    private SQLiteStatement insertMonkey;
    private SQLiteStatement insertQuote;

    private static final String INSERT = "insert into " + TABLE_NAME
            + " (datetoday,grateful,great,affirmations) values (?,?,?,?)";

    private static final String INSERT_MONKEY = "insert into " + TABLE_MONKEY
        + " (monkeydate, monkeytext) values (?,?)";

    public static final String INSERT_QUOTE = "insert into " + TABLE_QUOTE
            + " (quotedate, quote) values (?,?)";

    public DatabaseManipulator(Context context) {
        DatabaseManipulator.context = context;
        OpenHelper openHelper = new OpenHelper(this.context);
        DatabaseManipulator.db = openHelper.getWritableDatabase();
        this.insertStmt = DatabaseManipulator.db.compileStatement(INSERT);
        this.insertMonkey = DatabaseManipulator.db.compileStatement(INSERT_MONKEY);
        this.insertQuote = DatabaseManipulator.db.compileStatement(INSERT_QUOTE);
    }

    public long insertBulletDiary(String date, String grateful, String great, String affirmations) {
        // Check if diary for today date is present
        String b1 = new String();
        String whereC = "datetoday =" + "'" + date + "'";
        Cursor cursor = db.query(TABLE_NAME, new String[]{"datetoday"}, whereC, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                b1 = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor.close();
        // If present then use edit query
        if (b1.equals(date)) {
           Log.e("lol", "hello i am here");
           this.db.execSQL("UPDATE " + TABLE_NAME + " SET grateful = \'" +grateful +"\'" + " WHERE datetoday = \'" + date +"\'");
           this.db.execSQL("UPDATE " + TABLE_NAME + " SET great = \'" + great +"\'" + " WHERE datetoday = \'" + date +"\'");
           this.db.execSQL("UPDATE " + TABLE_NAME + " SET affirmations = \'" + affirmations +"\'" + " WHERE datetoday = \'" + date +"\'");
        return 0;
        }
        // Else insert query
        this.insertStmt.bindString(1, date);
        this.insertStmt.bindString(2, grateful);
        this.insertStmt.bindString(3, great);
        this.insertStmt.bindString(4, affirmations);
        return this.insertStmt.executeInsert();
    }


    public long insertMonkeyDiary(String date, String monkeyText) {
        // Check if diary for today date is present
        String b1 = new String();
        String whereC = "monkeydate =" + "'" + date + "'";
        Cursor cursor = db.query(TABLE_MONKEY, new String[]{"monkeydate"}, whereC, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                b1 = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor.close();
        // If present then use edit query
        if (b1.equals(date)) {
            this.db.execSQL("UPDATE " + TABLE_MONKEY + " SET monkeytext = \'" + monkeyText +"\'" + " WHERE monkeydate = \'" + date +"\'");
            return 0;
        }
        // Else insert query
        this.insertMonkey.bindString(1, date);
        this.insertMonkey.bindString(2, monkeyText);
        return this.insertMonkey.executeInsert();
    }

    public long insertQuote(String date, String quote) {
        // Check if quote for today is already present
        String b1 = new String();
        String whereC = "quotedate = " + "'" + date + "'";
        Cursor cursor = db.query(TABLE_QUOTE, new String[]{"quotedate"}, whereC, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                b1 = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor.close();
        // If present then use edit query
        if(b1.equals(date)) {
            this.db.execSQL("UPDATE " +TABLE_QUOTE + " SET quote = \'" + quote + "\'" + " WHERE quotedate = \'" + date + "\'");
        return 0;
        }
        // Else insert query
        this.insertQuote.bindString(1, date);
        this.insertQuote.bindString(2, quote);
        return  this.insertQuote.executeInsert();
    }


    public void deleteAll() {
        db.delete(TABLE_NAME, null, null);
    }

    public List<String[]> selectAllBullet() {
        List<String[]> list = new ArrayList<String[]>();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"datetoday","grateful", "great", "affirmations"}, null, null, null, null, "datetoday DESC");
        int x = 0;
        if (cursor.moveToFirst()) {
            do {
                String[] b1 = new String[]{cursor.getString(0),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3)};
                list.add(b1);
                x++;
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor.close();
        return list;
    }

    public String[] selectBulletDiary(String date) {
        // If the info is present in the db then retrieve it and display it in the fields
        String[] myBulletInfo = {"", "", "", ""};
        String whereClause = "datetoday ="+"'"+date+"'";
        Cursor cursor = db.query(TABLE_NAME, new String[]{"datetoday","grateful", "great", "affirmations"}, whereClause, null, null, null, null);
        // If the info is present then display it
        if (cursor.moveToFirst()) {
                myBulletInfo[0] = cursor.getString(0);
                myBulletInfo[1] = cursor.getString(1);
                myBulletInfo[2] = cursor.getString(2);
                myBulletInfo[3] = cursor.getString(3);
        }
        // If the info is not present then just set the fields to be empty
        else {
            myBulletInfo[0] = "";
            myBulletInfo[1] = "";
            myBulletInfo[2] = "";
            myBulletInfo[3] = "";
        }
        // Close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor.close();
        return  myBulletInfo;
    }

    public String[] selectMonkeyDiary(String date) {
        String[] myMonkeyInfo = {"", ""};
        String whereClause = "monkeydate = " +"'"+date+"'";
        // Create a cursor in order to get the info from the monkey table
        Cursor cursor = db.query(TABLE_MONKEY, new String[]{"monkeydate", "monkeytext"}, whereClause, null, null, null, null);
        // If the info is present then display it
        if(cursor.moveToFirst()) {
            myMonkeyInfo[0] = cursor.getString(0);
            myMonkeyInfo[1] = cursor.getString(1);
            //Log.e("found element date", date);
        }
        // If the info is not present empty the fields
        else {
            //Log.e("NOT found element date", date);
            myMonkeyInfo[0] = "";
            myMonkeyInfo[1] = "";
        }
        // Close the cursor
        if (cursor!=null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor.close();
        return myMonkeyInfo;
    }

    public String[] selectQuote(String date) {
        String[] myQuoteInfo = {"", ""};
        String whereClause = "quotedate = " + "'" + date + "'";
        // Create a cursor
        Cursor cursor = db.query(TABLE_QUOTE, new String[]{"quotedate", "quote"}, whereClause, null, null, null, null);
        // If info is present then display it
        if(cursor.moveToFirst()) {
            myQuoteInfo[0] = cursor.getString(0);
            myQuoteInfo[1] = cursor.getString(1);
        } else {
            myQuoteInfo[0] = "";
            myQuoteInfo[1] = "";
        }
        // Close the cursor
        if (cursor!=null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor.close();
        return myQuoteInfo;
    }




    public List<String[]> selectAllQuote() {
        List<String[]> list = new ArrayList<String[]>();
        Cursor cursor = db.query(TABLE_QUOTE, new String[]{"quotedate", "quote"}, null, null, null, null, "quotedate DESC");
        int x = 0;
        if (cursor.moveToFirst()) {
            do {
                String[] b1 = new String[]{cursor.getString(0),
                        cursor.getString(1)};
                list.add(b1);
                x++;
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor.close();
        return list;
    }

    public List<String[]> selectAllMonkeyDiary() {
        List<String[]> list = new ArrayList<String[]>();
        Cursor cursor = db.query(TABLE_MONKEY, new String[]{"monkeydate", "monkeytext"}, null, null, null, null, "monkeydate DESC");
        int x = 0;
        if (cursor.moveToFirst()) {
            do {
                String[] b1 = new String[]{cursor.getString(0),
                        cursor.getString(1)};
                list.add(b1);
                x++;
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor.close();
        return list;
    }


    private static class OpenHelper extends SQLiteOpenHelper {
        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "
                    + TABLE_QUOTE
                    + " (quotedate TEXT PRIMARY KEY, quote TEXT)");
            db.execSQL("CREATE TABLE "
                    + TABLE_NAME
                    + " (datetoday TEXT PRIMARY KEY, grateful TEXT, great TEXT, affirmations TEXT)");

            db.execSQL("CREATE TABLE "
                    + TABLE_MONKEY
                    + " (monkeydate TEXT PRIMARY KEY, monkeytext TEXT)");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            DATABASE_VERSION = newVersion;
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUOTE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONKEY);
            onCreate(db);
        }
    }

    public static String currentDate() {
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String todayDateString = formatter.format(todayDate);
        return todayDateString;
    }
}
