package com.example.kamil.ebookyourchildshealth.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kamil.ebookyourchildshealth.model.Child;
import com.example.kamil.ebookyourchildshealth.model.Visit;

/**
 * Created by kamil on 2016-10-31.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static MyDatabaseHelper sInstance;


    public static final String DATABASE_NAME = "child.db";
    public static final String CHILD_TABLE_NAME = "child_table";
    public static final String VISIT_TABLE_NAME = "visit_table";

    public static final String CHILD_COL_1 = "ID";
    public static final String CHILD_COL_2 = "NAME";
    public static final String CHILD_COL_3 = "SURNAME";
    public static final String CHILD_COL_4 = "PESEL";
    public static final String CHILD_COL_5 = "SEX";
    public static final String CHILD_COL_6 = "BLOOD_GROUP";
    public static final String CHILD_COL_7 = "BIRTH_DATE";
    public static final String CHILD_COL_8 = "BIRTH_PLACE";
    public static final String CHILD_COL_9 = "MOTHER";
    public static final String CHILD_COL_10 = "FATHER";
    public static final String CHILD_COL_11 = "IMAGE_URI";

    public static final String VISIT_COL_1 = "ID";
    public static final String VISIT_COL_2 = "CHILD_ID";
    public static final String VISIT_COL_3 = "NAME";
    public static final String VISIT_COL_4 = "DOCTOR";
    public static final String VISIT_COL_5 = "DISEASE";
    public static final String VISIT_COL_6 = "DATE";
    public static final String VISIT_COL_7 = "DESCRIPTION";
    public static final String VISIT_COL_8 = "RECOMMENDATIONS";
    public static final String VISIT_COL_9 = "MEDICINES";


    public static final String DATABASE_SQL_QUERY_CREATE_CHILD_TABLE =
            "CREATE TABLE " + CHILD_TABLE_NAME +
                    " (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "NAME TEXT," +
                        "SURNAME TEXT," +
                        "PESEL TEXT," +
                        "SEX TEXT," +
                        "BLOOD_GROUP TEXT," +
                        "BIRTH_DATE TEXT," +
                        "BIRTH_PLACE TEXT," +
                        "MOTHER TEXT," +
                        "FATHER TEXT," +
                        "IMAGE_URI TEXT" +
                    ")";

    public static final String DATABASE_SQL_QUERY_CREATE_VISIT_TABLE =
            "CREATE TABLE " + VISIT_TABLE_NAME +
                    " (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "CHILD_ID INTEGER REFERENCES " + CHILD_TABLE_NAME + ", " +
                    "NAME TEXT," +
                    "DOCTOR TEXT," +
                    "DISEASE TEXT," +
                    "DATE TEXT," +
                    "DESCRIPTION TEXT," +
                    "RECOMMENDATIONS TEXT," +
                    "MEDICINES TEXT" +
                    ") ";


    public static final String DATABASE_SQL_QUERY_DROP_CHILD_TABLE =
            "DROP TABLE IF EXISTS " + CHILD_TABLE_NAME;

    public static final String DATABASE_SQL_QUERY_DROP_VISIT_TABLE =
            "DROP TABLE IF EXISTS " + VISIT_TABLE_NAME;


    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
//        SQLiteDatabase db = this.getWritableDatabase();
    }


    public static synchronized MyDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new MyDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * SQLiteDatabase is the base class for working with a SQLite database
     * in Android and provides methods to open, query, update and close the database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_SQL_QUERY_CREATE_VISIT_TABLE);
        db.execSQL(DATABASE_SQL_QUERY_CREATE_CHILD_TABLE);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_SQL_QUERY_DROP_CHILD_TABLE);
        db.execSQL(DATABASE_SQL_QUERY_DROP_VISIT_TABLE);
        onCreate(db);
    }

    /**
     * Do zapisywania danych (w zasadzie do przekazywania ich do zapytania)
     * służy klasa ContentValues. Za pomocą metod put(…) umieszczamy w niej pary klucz-wartość
     * (gdzie klucz jest nazwą kolumny w naszej tabeli).
     * Następnie cały zbiór takich wartości (w postaci obiektu ContentValues)
     * przekazujemy do metody insert(…). Zwraca ona id ostatnio zapisanego wiersza, lub -1 w wypadku błędu
     */
//    public boolean insertDataIntoChildTable(String name, String surname, String pesel,
//                                            String sex, String blood_group, String birth_date,
//                                            String birth_place, String mother, String father) {
    public boolean insertDataIntoChildTable(Child child) {

        long result=0;

        SQLiteDatabase database = this.getWritableDatabase();
        database.beginTransaction();

//        result = database.insert(CHILD_TABLE_NAME, null, contentValues);
        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put(CHILD_COL_2, child.getName());
            contentValues.put(CHILD_COL_3, child.getSurname());
            contentValues.put(CHILD_COL_4, child.getPesel());
            contentValues.put(CHILD_COL_5, child.getSex());
            contentValues.put(CHILD_COL_6, child.getBlood_group());
            contentValues.put(CHILD_COL_7, child.getBirth_date());
            contentValues.put(CHILD_COL_8, child.getBirth_place());
            contentValues.put(CHILD_COL_9, child.getMother());
            contentValues.put(CHILD_COL_10, child.getFather());
            contentValues.put(CHILD_COL_11, child.getImageUri().toString());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            result = database.insertOrThrow(CHILD_TABLE_NAME, null, contentValues);
            database.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            database.endTransaction();
            if (result == -1)
                return false;
            else
                return true;
        }
    }

    public boolean insertDataIntoVisitTable(Visit visit) {
        long result=0;

        SQLiteDatabase database = this.getWritableDatabase();
        database.beginTransaction();

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(VISIT_COL_2, visit.getChild_id());
            contentValues.put(VISIT_COL_3, visit.getName());
            contentValues.put(VISIT_COL_4, visit.getDoctor());
            contentValues.put(VISIT_COL_5, visit.getDisease());
            contentValues.put(VISIT_COL_6, visit.getDate());
            contentValues.put(VISIT_COL_7, visit.getDescription());
            contentValues.put(VISIT_COL_8, visit.getDescription());
            contentValues.put(VISIT_COL_9, visit.getMedicines());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            result = database.insertOrThrow(VISIT_TABLE_NAME, null, contentValues);
            database.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            database.endTransaction();
            if (result == -1)
                return false;
            else
                return true;
        }

    }

    /**
     *     A query returns a Cursor object. A Cursor represents the result of a query
     *     and basically points to one row of the query result.
     *     This way Android can buffer the query results efficiently;
     *     as it does not have to load all data into memory.
     */
    public Cursor readChildData(String name) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor result = database.rawQuery("select * from " + CHILD_TABLE_NAME + " WHERE name = ? ;" ,
                new String[] { name });
        return result;
    }

    public Cursor readAllChildIdNamesImages() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor result = database.rawQuery("select id, name, IMAGE_URI from " + CHILD_TABLE_NAME, null);
        return result;
    }
//    public Cursor readChildMedicalVisitsData(int childId) {
//        SQLiteDatabase database = this.getReadableDatabase();
//        Cursor result = database.rawQuery("select * from " + VISIT_TABLE_NAME , null);
//        return result;
//    }

    public Cursor readChildMedicalVisitsData(int childId) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor result = database.rawQuery("select * from " + VISIT_TABLE_NAME + " WHERE CHILD_ID = ? ;" ,
                new String[] { String.valueOf(childId) });
        return result;
    }

    public Cursor readMedicalVisitData(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor result = database.rawQuery("select * from " + VISIT_TABLE_NAME + " WHERE id = ? ;" ,
                new String[] { String.valueOf(id) });
        return result;
    }

}