package com.iriad11.recipes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String RECIPE_TABLE_NAME = "recipe";
    public static final String RECIPE_COLUMN_ID = "id";
    public static final String RECIPE_COLUMN_NAME = "name";
    public static final String RECIPE_COLUMN_INGRDNT = "ingredients";
    public static final String RECIPE_COLUMN_PROCESS = "process";
    public static final String RECIPE_COLUMN_LINK = "link";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table recipe " +
                        "( id integer primary key autoincrement, name text, ingredients text, process text, link text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS recipe");
        onCreate(db);
    }

    public void upgrade(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists recipe");
        onCreate(db);
    }

    public boolean insertrecipe (String name, String ingredients, String process, String link) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("process", process);
        contentValues.put("ingredients", ingredients);
        contentValues.put("link", link);
        db.insert("recipe", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from recipe where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, RECIPE_TABLE_NAME);
        return numRows;
    }
    /*
        public boolean updaterecipe (Integer id, String name, String ingredients, String process) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("process", process);
            contentValues.put("ingredients", ingredients);
            db.update("recipe", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
            return true;
        }

        public Integer deleteContact (Integer id) {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete("recipe ",
                    "id = ? ",
                    new String[] { Integer.toString(id) });
        }
    */
    public ArrayList<String> getAllRecipe() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from recipe", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(RECIPE_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}