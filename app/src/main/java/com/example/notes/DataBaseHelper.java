package com.example.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String NOTES_TABLE = "NOTES_TABLE";
    private static final String COLUMN_NOTE = "NOTE";
    private static final String COLUMN_ID = "ID";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "notes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + NOTES_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NOTE + " TEXT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(String note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NOTE, note);

        long insert = db.insert(NOTES_TABLE, null, cv);
        if(insert==-1) return false;
        else return true;
    }

    public boolean deleteOne(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        String rawQuery = "DELETE FROM " + NOTES_TABLE + " WHERE " + COLUMN_ID + " = " + note.getId();

        Cursor cursor = db.rawQuery(rawQuery , null);

        return cursor.moveToFirst();
    }

    public boolean update(Note note, String newNote){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NOTE,newNote);

        int update = db.update(NOTES_TABLE, cv, COLUMN_ID + " = " + note.getId(), null);
        return (update!=-1);
    }

    public boolean deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        String rawQuery = "DELETE FROM " + NOTES_TABLE;

        Cursor cursor = db.rawQuery(rawQuery, null);

        return cursor.moveToFirst();
    }

    public List<Note> getAll(){
        List<Note> toReturn = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String rawQuery = "SELECT * FROM " + NOTES_TABLE;

        Cursor cursor = db.rawQuery(rawQuery, null);

        if(cursor.moveToFirst()){
            //loop thru the cursor, get strings and put them in the return list
            do{
                Note note = new Note(cursor.getInt(0), cursor.getString(1));
                toReturn.add(note);
            }while (cursor.moveToNext());
        }else{

        }

        cursor.close();
        db.close();

        return toReturn;
    }
}
