package com.redkant.mymojo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.redkant.mymojo.db.Body;
import com.redkant.mymojo.db.Gki;

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

    private static final String TABLE_GKI = "GKI";
    private static final String GKI_COLUMN_ID ="ID";
    private static final String GKI_COLUMN_CREATE_DATE ="CREATE_DATE";
    private static final String GKI_COLUMN_KETO_NUMBER ="KETO_NUMBER";
    private static final String GKI_COLUMN_SUGAR_NUMBER ="GLUCOSE_NUMBER";
    private static final String GKI_COLUMN_NOTE ="NOTE";

    private static final String TABLE_BODY = "BODY";
    private static final String BODY_COLUMN_ID ="ID";
    private static final String BODY_COLUMN_CREATE_DATE ="CREATE_DATE";
    private static final String BODY_COLUMN_WEIGHT ="WEIGHT";
    private static final String BODY_COLUMN_ARM_HIGH_LEFT ="ARM_HIGH_LEFT";
    private static final String BODY_COLUMN_ARM_HIGH_RIGHT ="ARM_HIGH_RIGHT";
    private static final String BODY_COLUMN_ARM_LOW_LEFT ="ARM_LOW_LEFT";
    private static final String BODY_COLUMN_ARM_LOW_RIGHT ="ARM_LOW_RIGHT";
    private static final String BODY_COLUMN_CHEST ="CHEST";
    private static final String BODY_COLUMN_CHEST_UNDER ="CHEST_UNDER";
    private static final String BODY_COLUMN_WAIST ="WAIST";
    private static final String BODY_COLUMN_HIPS_HIGH ="HIPS_HIGH";
    private static final String BODY_COLUMN_HIPS ="HIPS";
    private static final String BODY_COLUMN_THIGH_LEFT ="THIGH_LEFT";
    private static final String BODY_COLUMN_THIGH_RIGHT ="THIGH_RIGHT";
    private static final String BODY_COLUMN_CALF_LEFT ="CALF_LEFT";
    private static final String BODY_COLUMN_CALF_RIGHT ="CALF_RIGHT";


    public MojoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE " + TABLE_GKI + "("
                + GKI_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + GKI_COLUMN_CREATE_DATE + " DATETIME,"
                + GKI_COLUMN_KETO_NUMBER + " FLOAT, "
                + GKI_COLUMN_SUGAR_NUMBER + " FLOAT, "
                + GKI_COLUMN_NOTE + " VARCHAR2(2000) "
                + ")";
        db.execSQL(script);

        script = "CREATE TABLE " + TABLE_BODY + "("
                + BODY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BODY_COLUMN_CREATE_DATE + " DATETIME,"
                + BODY_COLUMN_WEIGHT + " FLOAT, "
                + BODY_COLUMN_ARM_HIGH_LEFT + " FLOAT, "
                + BODY_COLUMN_ARM_HIGH_RIGHT + " FLOAT, "
                + BODY_COLUMN_ARM_LOW_LEFT + " FLOAT, "
                + BODY_COLUMN_ARM_LOW_RIGHT + " FLOAT, "
                + BODY_COLUMN_CHEST + " FLOAT, "
                + BODY_COLUMN_CHEST_UNDER + " FLOAT, "
                + BODY_COLUMN_WAIST + " FLOAT, "
                + BODY_COLUMN_HIPS_HIGH + " FLOAT, "
                + BODY_COLUMN_HIPS + " FLOAT, "
                + BODY_COLUMN_THIGH_LEFT + " FLOAT, "
                + BODY_COLUMN_THIGH_RIGHT + " FLOAT, "
                + BODY_COLUMN_CALF_LEFT + " FLOAT, "
                + BODY_COLUMN_CALF_RIGHT + " FLOAT "
                + ")";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GKI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BODY);
        onCreate(db);
    }

    public void addGki(Gki gki) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GKI_COLUMN_CREATE_DATE, gki.getCreateDate());
        values.put(GKI_COLUMN_KETO_NUMBER, gki.getKetoNumber());
        values.put(GKI_COLUMN_SUGAR_NUMBER, gki.getGlucoseNumber());
        values.put(GKI_COLUMN_NOTE, gki.getNote());

        db.insert(TABLE_GKI, null, values);

        db.close();
    }

    public Gki getGki(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_GKI,
                                 new String[] {
                                         GKI_COLUMN_CREATE_DATE,
                                         GKI_COLUMN_KETO_NUMBER,
                                         GKI_COLUMN_SUGAR_NUMBER,
                                         GKI_COLUMN_NOTE
                                 }, GKI_COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Gki gki = new Gki(
                cursor.getString(0),
                cursor.getFloat(1),
                cursor.getInt(2),
                cursor.getString(3)
        );

        return gki;
    }

    public void getGkiAll(List<Gki> list) {
        list.clear();

        String selectQuery = "SELECT " + GKI_COLUMN_ID +
                ", " + GKI_COLUMN_CREATE_DATE +
                ", " + GKI_COLUMN_KETO_NUMBER +
                ", " + GKI_COLUMN_SUGAR_NUMBER +
                ", " + GKI_COLUMN_NOTE + " FROM " + TABLE_GKI +
                " ORDER BY " + GKI_COLUMN_CREATE_DATE + " DESC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Gki gki = new Gki();
                gki.setID(Integer.parseInt(cursor.getString(0)));

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
                try {
                    Date d = df.parse(cursor.getString(1));
                    df.applyPattern("dd.MM.yyyy HH:mm");
                    gki.setCreateDate(df.format(d));
                } catch (ParseException e) {
                    Log.i("***", cursor.getString(1));
                }

                gki.setKetoNumber(Float.parseFloat(cursor.getString(2)));
                gki.setGlucoseNumber(Integer.parseInt(cursor.getString(3)));
                gki.setNote(cursor.getString(4));

                list.add(gki);
            } while (cursor.moveToNext());
        }
    }

    public void getGkiFiltered(List<Gki> list, String filter) {
        String where;

        if (!filter.equals("")) {
            where = " AND " + filter + " ";
        } else {
            where = "";
        }

        list.clear();

        String selectQuery = "SELECT " + GKI_COLUMN_ID +
                ", " + GKI_COLUMN_CREATE_DATE +
                ", " + GKI_COLUMN_KETO_NUMBER +
                ", " + GKI_COLUMN_SUGAR_NUMBER +
                ", " + GKI_COLUMN_NOTE + " FROM " + TABLE_GKI +
                " WHERE 1=1 " +
                where +
                " ORDER BY " + GKI_COLUMN_CREATE_DATE + " DESC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Gki gki = new Gki();
                gki.setID(Integer.parseInt(cursor.getString(0)));

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
                try {
                    Date d = df.parse(cursor.getString(1));
                    df.applyPattern("dd.MM.yyyy HH:mm");
                    gki.setCreateDate(df.format(d));
                } catch (ParseException e) {
                    Log.i("***", cursor.getString(1));
                }

                gki.setKetoNumber(Float.parseFloat(cursor.getString(2)));
                gki.setGlucoseNumber(Integer.parseInt(cursor.getString(3)));
                gki.setNote(cursor.getString(4));

                list.add(gki);
            } while (cursor.moveToNext());
        }
    }

    public List<Gki> getGkiAll() {
        List<Gki> GkiList = new ArrayList<Gki>();

        String selectQuery = "SELECT " + GKI_COLUMN_ID +
                ", " + GKI_COLUMN_CREATE_DATE +
                ", " + GKI_COLUMN_KETO_NUMBER +
                ", " + GKI_COLUMN_SUGAR_NUMBER +
                ", " + GKI_COLUMN_NOTE + " FROM " + TABLE_GKI +
                " ORDER BY " + GKI_COLUMN_CREATE_DATE + " DESC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Gki gki = new Gki();
                gki.setID(Integer.parseInt(cursor.getString(0)));

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
                try {
                    Date d = df.parse(cursor.getString(1));
                    df.applyPattern("dd.MM.yyyy HH:mm");
                    gki.setCreateDate(df.format(d));
                } catch (ParseException e) {
                    Log.i("***", cursor.getString(1));
                }

                gki.setKetoNumber(Float.parseFloat(cursor.getString(2)));
                gki.setGlucoseNumber(Integer.parseInt(cursor.getString(3)));
                gki.setNote(cursor.getString(4));

                GkiList.add(gki);
            } while (cursor.moveToNext());
        }

        return GkiList;
    }

    public int getGkiCount() {
/*
        String countQuery = "SELECT count(*) AS cnt FROM " + TABLE_GKI;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getInt(0);
        cursor.close();

        return count;
*/
        String countQuery = "SELECT  * FROM " + TABLE_GKI;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;

    }

    public int updateGki(Gki gki) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GKI_COLUMN_CREATE_DATE, gki.getCreateDate());
        values.put(GKI_COLUMN_KETO_NUMBER, gki.getKetoNumber());
        values.put(GKI_COLUMN_SUGAR_NUMBER, gki.getGlucoseNumber());
        values.put(GKI_COLUMN_NOTE, gki.getNote());

        // updating row
        return db.update(TABLE_GKI, values, GKI_COLUMN_ID + " = ?",
                new String[]{String.valueOf(gki.getID())});
    }

    public void deleteGki(Gki gki) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GKI, GKI_COLUMN_ID + " = ?",
                new String[] { String.valueOf(gki.getID()) });
        db.close();
    }

    public void addBody(Body body) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BODY_COLUMN_CREATE_DATE, body.getCreateDate());
        values.put(BODY_COLUMN_WEIGHT, body.getWeight());
        values.put(BODY_COLUMN_ARM_HIGH_LEFT, body.getArmHighLeft());
        values.put(BODY_COLUMN_ARM_HIGH_RIGHT, body.getArmHighRight());
        values.put(BODY_COLUMN_ARM_LOW_LEFT, body.getArmLowLeft());
        values.put(BODY_COLUMN_ARM_LOW_RIGHT, body.getArmLowRight());
        values.put(BODY_COLUMN_CHEST, body.getChest());
        values.put(BODY_COLUMN_CHEST_UNDER, body.getChestUnder());
        values.put(BODY_COLUMN_WAIST, body.getWaist());
        values.put(BODY_COLUMN_HIPS_HIGH, body.getHipsHigh());
        values.put(BODY_COLUMN_HIPS, body.getHips());
        values.put(BODY_COLUMN_THIGH_LEFT, body.getThighLeft());
        values.put(BODY_COLUMN_THIGH_RIGHT, body.getThighRight());
        values.put(BODY_COLUMN_CALF_LEFT, body.getCalfLeft());
        values.put(BODY_COLUMN_CALF_RIGHT, body.getCalfRight());

        db.insert(TABLE_BODY, null, values);

        db.close();
    }

    public int updateBody(Body body) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BODY_COLUMN_CREATE_DATE, body.getCreateDate());
        values.put(BODY_COLUMN_WEIGHT, body.getWeight());
        values.put(BODY_COLUMN_ARM_HIGH_LEFT, body.getArmHighLeft());
        values.put(BODY_COLUMN_ARM_HIGH_RIGHT, body.getArmHighRight());
        values.put(BODY_COLUMN_ARM_LOW_LEFT, body.getArmLowLeft());
        values.put(BODY_COLUMN_ARM_LOW_RIGHT, body.getArmLowRight());
        values.put(BODY_COLUMN_CHEST, body.getChest());
        values.put(BODY_COLUMN_CHEST_UNDER, body.getChestUnder());
        values.put(BODY_COLUMN_WAIST, body.getWaist());
        values.put(BODY_COLUMN_HIPS_HIGH, body.getHipsHigh());
        values.put(BODY_COLUMN_HIPS, body.getHips());
        values.put(BODY_COLUMN_THIGH_LEFT, body.getThighLeft());
        values.put(BODY_COLUMN_THIGH_RIGHT, body.getThighRight());
        values.put(BODY_COLUMN_CALF_LEFT, body.getCalfLeft());
        values.put(BODY_COLUMN_CALF_RIGHT, body.getCalfRight());

        // updating row
        return db.update(TABLE_BODY, values, BODY_COLUMN_ID + " = ?",
                new String[]{String.valueOf(body.getID())});
    }

    public void deleteBody(Body body) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BODY, BODY_COLUMN_ID + " = ?",
                new String[] { String.valueOf(body.getID()) });
        db.close();
    }

    public void getBodyFiltered(List<Body> list, String filter) {
        String where;

        if (!filter.equals("")) {
            where = " AND " + filter + " ";
        } else {
            where = "";
        }

        list.clear();

        String selectQuery = "SELECT " + BODY_COLUMN_ID +
                ", " + BODY_COLUMN_CREATE_DATE +
                ", " + BODY_COLUMN_WEIGHT +
                ", " + BODY_COLUMN_ARM_HIGH_LEFT +
                ", " + BODY_COLUMN_ARM_HIGH_RIGHT +
                ", " + BODY_COLUMN_ARM_LOW_LEFT +
                ", " + BODY_COLUMN_ARM_LOW_RIGHT +
                ", " + BODY_COLUMN_CHEST +
                ", " + BODY_COLUMN_CHEST_UNDER +
                ", " + BODY_COLUMN_WAIST +
                ", " + BODY_COLUMN_HIPS_HIGH +
                ", " + BODY_COLUMN_HIPS +
                ", " + BODY_COLUMN_THIGH_LEFT +
                ", " + BODY_COLUMN_THIGH_RIGHT +
                ", " + BODY_COLUMN_CALF_LEFT +
                ", " + BODY_COLUMN_CALF_RIGHT +
                " FROM " + TABLE_BODY +
                " WHERE 1=1 " +
                where +
                " ORDER BY " + BODY_COLUMN_CREATE_DATE + " DESC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Body body = new Body();
                body.setID(Integer.parseInt(cursor.getString(0)));

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.ENGLISH);
                try {
                    Date d = df.parse(cursor.getString(1));
                    df.applyPattern("dd.MM.yyyy HH:mm");
                    body.setCreateDate(df.format(d));
                } catch (ParseException e) {
                    Log.i("***", cursor.getString(1));
                }

                body.setWeight(Float.parseFloat(cursor.getString(2)));
                body.setArmHighLeft(Float.parseFloat(cursor.getString(3)));
                body.setArmHighRight(Float.parseFloat(cursor.getString(4)));
                body.setArmLowLeft(Float.parseFloat(cursor.getString(5)));
                body.setArmLowRight(Float.parseFloat(cursor.getString(6)));
                body.setChest(Float.parseFloat(cursor.getString(7)));
                body.setChestUnder(Float.parseFloat(cursor.getString(8)));
                body.setWaist(Float.parseFloat(cursor.getString(9)));
                body.setHipsHigh(Float.parseFloat(cursor.getString(10)));
                body.setHips(Float.parseFloat(cursor.getString(11)));
                body.setThighLeft(Float.parseFloat(cursor.getString(12)));
                body.setThighRight(Float.parseFloat(cursor.getString(13)));
                body.setCalfLeft(Float.parseFloat(cursor.getString(14)));
                body.setCalfRight(Float.parseFloat(cursor.getString(15)));

                list.add(body);
            } while (cursor.moveToNext());
        }
    }

}
