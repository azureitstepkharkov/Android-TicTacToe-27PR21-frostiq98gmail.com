package com.example.frost.tictactoe_android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "tictactoe", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("create table tictactoe (name varchar,winner int not null);"));

        db.execSQL(String.format("INSERT INTO tictactoe(name,winner)VALUES('O',0)"));

        db.execSQL(String.format("INSERT INTO tictactoe(name,winner)VALUES('X',0)"));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
