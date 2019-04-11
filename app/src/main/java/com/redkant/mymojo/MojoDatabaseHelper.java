package com.redkant.mymojo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.redkant.mymojo.db.Mojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MojoDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mojo_db";

    private static final String TABLE_MOJO = "MOJO";

    private static final String COLUMN_ID ="ID";
    private static final String COLUMN_CREATE_DATE ="CREATE_DATE";
    private static final String COLUMN_KETO_NUMBER ="KETO_NUMBER";
    private static final String COLUMN_SUGAR_NUMBER ="SUGAR_NUMBER";
    private static final String COLUMN_WEIGHT ="WEIGHT";

    public MojoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE " + TABLE_MOJO + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CREATE_DATE + " STRING,"
                + COLUMN_KETO_NUMBER + " FLOAT, "
                + COLUMN_SUGAR_NUMBER + " FLOAT, "
                + COLUMN_WEIGHT + " FLOAT "
                + ")";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOJO);
        onCreate(db);
    }

    public void addMojo(Mojo mojo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CREATE_DATE, mojo.getCreateDate());
        values.put(COLUMN_KETO_NUMBER, mojo.getKetoNumber());
        values.put(COLUMN_SUGAR_NUMBER, mojo.getGlucoseNumber());
        values.put(COLUMN_WEIGHT, mojo.getWeight());

        db.insert(TABLE_MOJO, null, values);

        db.close();
    }

    public Mojo getMojo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MOJO,
                                 new String[] {
                                         COLUMN_CREATE_DATE,
                                         COLUMN_KETO_NUMBER,
                                         COLUMN_SUGAR_NUMBER,
                                         COLUMN_WEIGHT
                                 }, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Mojo mojo = new Mojo(
                cursor.getString(0),
                cursor.getFloat(1),
                cursor.getFloat(2),
                cursor.getFloat(3)
        );

        return mojo;
    }

    public List<Mojo> getAllMojo() {
        List<Mojo> mogoList = new ArrayList<Mojo>();

        String selectQuery = "SELECT " + COLUMN_ID +
                ", " + COLUMN_CREATE_DATE +
                ", " + COLUMN_KETO_NUMBER +
                ", " + COLUMN_SUGAR_NUMBER +
                ", " + COLUMN_WEIGHT + " FROM " + TABLE_MOJO +
                " ORDER BY " + COLUMN_CREATE_DATE + " DESC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Mojo mojo = new Mojo();
                mojo.setID(Integer.parseInt(cursor.getString(0)));

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
                try {
                    Date d = df.parse(cursor.getString(1));
                    df.applyPattern("dd.MM.yyyy HH:mm");
                    mojo.setCreateDate(df.format(d));
                } catch (ParseException e) {
                    Log.i("***", cursor.getString(1));
                }

                mojo.setKetoNumber(Float.parseFloat(cursor.getString(2)));
                mojo.setGlucoseNumber(Float.parseFloat(cursor.getString(3)));
                mojo.setWeight(Float.parseFloat(cursor.getString(4)));

                mogoList.add(mojo);
            } while (cursor.moveToNext());
        }

        return mogoList;
    }

    public int getMojoCount() {
/*
        String countQuery = "SELECT count(*) AS cnt FROM " + TABLE_MOJO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getInt(0);
        cursor.close();

        return count;
*/
        String countQuery = "SELECT  * FROM " + TABLE_MOJO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;

    }

    public int updateMojo(Mojo mojo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CREATE_DATE, mojo.getCreateDate());
        values.put(COLUMN_KETO_NUMBER, mojo.getKetoNumber());
        values.put(COLUMN_SUGAR_NUMBER, mojo.getGlucoseNumber());
        values.put(COLUMN_WEIGHT, mojo.getWeight());

        // updating row
        return db.update(TABLE_MOJO, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(mojo.getID())});
    }

    public void deleteNote(Mojo mojo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOJO, COLUMN_ID + " = ?",
                new String[] { String.valueOf(mojo.getID()) });
        db.close();
    }

    public void createDefaultMojoIfNeed()  {
        int count = this.getMojoCount();

        if(count ==0 ) {
            Mojo note1 = new Mojo((new SimpleDateFormat("dd.MM.yyyy HH:mm")).format(new Date()),
                    2.1f,
                    69f,
                    72.1f);
            this.addMojo(note1);
        }
    }

}
