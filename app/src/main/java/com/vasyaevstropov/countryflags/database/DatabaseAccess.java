package com.vasyaevstropov.countryflags.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by в on 05.12.2016.
 */

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    ArrayList<String> questionList;

    private DatabaseAccess(Context context){
        this.openHelper = new DatabaseOpenHelper(context);
    }
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }
    public void open() {
        this.database = openHelper.getReadableDatabase();
    }
    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }


    public ArrayList<String> getQuestion(Integer id){
        questionList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM country WHERE _id = "+id, null);
        cursor.moveToFirst();
        questionList.add(""+id);
        questionList.add(cursor.getString(cursor.getColumnIndex("country_rus")));
        questionList.add(cursor.getString(cursor.getColumnIndex("area")));
        questionList.add(cursor.getString(cursor.getColumnIndex("country_en")));
        cursor.close();
       return  questionList;
    }




}
