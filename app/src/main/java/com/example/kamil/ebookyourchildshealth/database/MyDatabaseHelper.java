package com.example.kamil.ebookyourchildshealth.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kamil.ebookyourchildshealth.MyDebugger;
import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.model.Child;
import com.example.kamil.ebookyourchildshealth.model.Disease;
import com.example.kamil.ebookyourchildshealth.model.Note;
import com.example.kamil.ebookyourchildshealth.model.Reminder;
import com.example.kamil.ebookyourchildshealth.model.Visit;

import butterknife.ButterKnife;

/**
 * Created by kamil on 2016-10-31.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    MyDebugger myDebugger = new MyDebugger();

    private static Context context;
    private static MyDatabaseHelper myDatabaseHelperInstance;

    public static String DATABASE_NAME = "child.db";
    public static String CHILD_TABLE_NAME = "";
    public static String MEDICAL_VISITS_TABLE_NAME = "";
    public static String DISEASES_TABLE_NAME = "";
    public static String DISEASES_NOTES_TABLE_NAME = "";
    public static String REMINDER_TABLE_NAME = "";

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
    public static final String VISIT_COL_5 = "DISEASE_ID";
    public static final String VISIT_COL_6 = "DATE";
    public static final String VISIT_COL_7 = "DESCRIPTION";
    public static final String VISIT_COL_8 = "RECOMMENDATIONS";
    public static final String VISIT_COL_9 = "MEDICINES";

    public static final String DISEASE_COL_1 = "ID";
    public static final String DISEASE_COL_2 = "CHILD_ID";
    public static final String DISEASE_COL_3 = "NAME";
    public static final String DISEASE_COL_4 = "DATE";

    public static final String NOTES_COL_1 = "ID";
    public static final String NOTES_COL_2 = "DISEASES_ID";
    public static final String NOTES_COL_3 = "DATE";
    public static final String NOTES_COL_4 = "MESSAGE";

    public static final String REMINDER_COL_1 = "ID";
    public static final String REMINDER_COL_2 = "VISIT_ID";
    public static final String REMINDER_COL_3 = "CALENDAR_ID";

    public static String DATABASE_SQL_QUERY_CREATE_CHILD_TABLE = "";
    public static String DATABASE_SQL_QUERY_CREATE_VISIT_TABLE = "";
    public static String DATABASE_SQL_QUERY_CREATE_DISEASES_TABLE = "";
    public static String DATABASE_SQL_QUERY_CREATE_DISEASES_NOTES_TABLE = "";
    public static String DATABASE_SQL_QUERY_CREATE_REMINDER_TABLE = "";

    public static String DATABASE_SQL_QUERY_DROP_CHILD_TABLE= "";
    public static String DATABASE_SQL_QUERY_DROP_VISIT_TABLE= "";
    public static String DATABASE_SQL_QUERY_DROP_DISEASES = "";
    public static String DATABASE_SQL_QUERY_DROP_DISEASES_NOTES = "";
    public static String DATABASE_SQL_QUERY_DROP_REMINDER = "";

    public static String tableName = "tableName";
    public static String DATABASE_SQL_QUERY_CHOOSE_CHILD = "";
    public static String DATABASE_SQL_QUERY_SELECT_WHERE_CHILD_ID = "";
    public static String DATABASE_SQL_QUERY_SELECT_NOTES_WHERE_DISEASES_ID = "";
    public static String DATABASE_SQL_QUERY_SELECT_REMINDERS_WHERE_VISIT_ID = "";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

        setContext(context);
        setVariablesFromStaticResources();
//        SQLiteDatabase db = this.getWritableDatabase();
    }

    public static synchronized MyDatabaseHelper getMyDatabaseHelperInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (myDatabaseHelperInstance == null) {
            myDatabaseHelperInstance = new MyDatabaseHelper(context.getApplicationContext());
        }
        return myDatabaseHelperInstance;
    }

    /**
     * SQLiteDatabase is the base class for working with a SQLite database
     * in Android and provides methods to open, query, update and close the database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_SQL_QUERY_CREATE_VISIT_TABLE);
        db.execSQL(DATABASE_SQL_QUERY_CREATE_CHILD_TABLE);
        db.execSQL(DATABASE_SQL_QUERY_CREATE_DISEASES_TABLE);
        db.execSQL(DATABASE_SQL_QUERY_CREATE_DISEASES_NOTES_TABLE);
        db.execSQL(DATABASE_SQL_QUERY_CREATE_REMINDER_TABLE);
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
        db.execSQL(DATABASE_SQL_QUERY_DROP_DISEASES);
        db.execSQL(DATABASE_SQL_QUERY_DROP_DISEASES_NOTES);
        db.execSQL(DATABASE_SQL_QUERY_DROP_REMINDER);
        onCreate(db);
    }

    private void setVariablesFromStaticResources() {
        CHILD_TABLE_NAME = getStringFromResources(R.string.child_table_name);
        MEDICAL_VISITS_TABLE_NAME = getStringFromResources(R.string.medical_visit_table_name);
        DISEASES_TABLE_NAME = getStringFromResources(R.string.diseases_table_name);
        DISEASES_NOTES_TABLE_NAME = getStringFromResources(R.string.diseases_notes_table_name);
        REMINDER_TABLE_NAME = getStringFromResources(R.string.reminder_table_name);

        DATABASE_SQL_QUERY_CREATE_CHILD_TABLE =
                getStringFromResources(R.string.database_sql_query_create_child_table);
        DATABASE_SQL_QUERY_CREATE_VISIT_TABLE =
                getStringFromResources(R.string.database_sql_query_create_visit_table);
        DATABASE_SQL_QUERY_CREATE_DISEASES_TABLE =
                getStringFromResources(R.string.database_sql_query_create_diseases_table);
        DATABASE_SQL_QUERY_CREATE_DISEASES_NOTES_TABLE =
                getStringFromResources(R.string.database_sql_query_create_diseases_notes_table);
        DATABASE_SQL_QUERY_CREATE_REMINDER_TABLE =
                getStringFromResources(R.string.database_sql_query_create_reminder_table);

        DATABASE_SQL_QUERY_DROP_CHILD_TABLE =
                getStringFromResources(R.string.database_sql_query_drop_table) + CHILD_TABLE_NAME;
        DATABASE_SQL_QUERY_DROP_VISIT_TABLE =
                getStringFromResources(R.string.database_sql_query_drop_table)+ MEDICAL_VISITS_TABLE_NAME;
        DATABASE_SQL_QUERY_DROP_DISEASES =
                getStringFromResources(R.string.database_sql_query_drop_table)+ DISEASES_TABLE_NAME;
        DATABASE_SQL_QUERY_DROP_DISEASES_NOTES =
                getStringFromResources(R.string.database_sql_query_drop_table)+ DISEASES_NOTES_TABLE_NAME;
        DATABASE_SQL_QUERY_DROP_REMINDER =
                getStringFromResources(R.string.database_sql_query_drop_table)+ REMINDER_TABLE_NAME;

//        DATABASE_SQL_QUERY_CREATE_VISIT_TABLE = getStringFromResources(context,R.string.database_sql_query_create_visit_table);
    }

    private String getStringFromResources(int path) {
        return getContext().getResources().getString(path);
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        MyDatabaseHelper.context = context;
    }

    /**
     * Do zapisywania danych (w zasadzie do przekazywania ich do zapytania)
     * służy klasa ContentValues. Za pomocą metod put(…) umieszczamy w niej pary klucz-wartość
     * (gdzie klucz jest nazwą kolumny w naszej tabeli).
     * Następnie cały zbiór takich wartości (w postaci obiektu ContentValues)
     * przekazujemy do metody insert(…). Zwraca ona id ostatnio zapisanego wiersza, lub -1 w wypadku błędu
     */
    public boolean insertDataIntoChildTable(Child child) {

        long result=0;

        SQLiteDatabase database = this.getWritableDatabase();
        database.beginTransaction();

        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put(CHILD_COL_2, child.getName());
            contentValues.put(CHILD_COL_3, child.getSurname());
            contentValues.put(CHILD_COL_4, child.getPesel());
            contentValues.put(CHILD_COL_5, child.getSex());
            contentValues.put(CHILD_COL_6, child.getBloodGroup());
            contentValues.put(CHILD_COL_7, child.getBirthDate());
            contentValues.put(CHILD_COL_8, child.getBirthPlace());
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
            contentValues.put(VISIT_COL_2, visit.getChildId());
            contentValues.put(VISIT_COL_3, visit.getName());
            contentValues.put(VISIT_COL_4, visit.getDoctor());
            contentValues.put(VISIT_COL_5, visit.getDiseaseId());
            contentValues.put(VISIT_COL_6, visit.getDate());
            contentValues.put(VISIT_COL_7, visit.getDescription());
            contentValues.put(VISIT_COL_8, visit.getRecommendations());
            contentValues.put(VISIT_COL_9, visit.getMedicines());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            result = database.insertOrThrow(MEDICAL_VISITS_TABLE_NAME, null, contentValues);
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

    public boolean insertDataIntoDiseaseTable(Disease disease) {
        long result=0;

        SQLiteDatabase database = this.getWritableDatabase();
        database.beginTransaction();

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DISEASE_COL_2, disease.getChildId());
            contentValues.put(DISEASE_COL_3, disease.getName());
            contentValues.put(DISEASE_COL_4, disease.getDate());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            result = database.insertOrThrow(DISEASES_TABLE_NAME, null, contentValues);
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

    public boolean insertDataIntoNotesTable(Note note) {
        long result=0;

        SQLiteDatabase database = this.getWritableDatabase();
        database.beginTransaction();

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(NOTES_COL_2, note.getDiseaseId());
            contentValues.put(NOTES_COL_3, note.getDate());
            contentValues.put(NOTES_COL_4, note.getNoteText());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            result = database.insertOrThrow(DISEASES_NOTES_TABLE_NAME, null, contentValues);
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

    public boolean insertDataIntoReminderTable(Reminder reminder) {
        long result=0;

        SQLiteDatabase database = this.getWritableDatabase();
        database.beginTransaction();

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(REMINDER_COL_2, reminder.getVisitId());
            contentValues.put(REMINDER_COL_3, reminder.getCalendarId());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            result = database.insertOrThrow(REMINDER_TABLE_NAME, null, contentValues);
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
    public Cursor readChildData(int childId) {
        SQLiteDatabase database = this.getReadableDatabase();

        String DATABASE_SQL_QUERY_SELECT_WHERE_ID =
                getStringFromResources(R.string.database_sql_query_select_where_id);

        DATABASE_SQL_QUERY_SELECT_WHERE_ID =
                DATABASE_SQL_QUERY_SELECT_WHERE_ID.replace(tableName, CHILD_TABLE_NAME);

        Cursor result = database.rawQuery(DATABASE_SQL_QUERY_SELECT_WHERE_ID ,
                new String[] { String.valueOf(childId) });
        return result;
    }

    public Cursor readAllChildsIdNamesImages() {
        SQLiteDatabase database = this.getReadableDatabase();

        DATABASE_SQL_QUERY_CHOOSE_CHILD =
                getStringFromResources(R.string.database_sql_query_select_id_name_uri_from_child);
        DATABASE_SQL_QUERY_CHOOSE_CHILD =
                DATABASE_SQL_QUERY_CHOOSE_CHILD.replace(tableName, CHILD_TABLE_NAME);


        Cursor result = database.rawQuery(DATABASE_SQL_QUERY_CHOOSE_CHILD, null);
        return result;
    }

    public Cursor readChildAllMedicalVisitsData(int childId) {
        SQLiteDatabase database = this.getWritableDatabase();

        DATABASE_SQL_QUERY_SELECT_WHERE_CHILD_ID =
                getStringFromResources(R.string.database_sql_query_select_where_child_id);

        DATABASE_SQL_QUERY_SELECT_WHERE_CHILD_ID =
                DATABASE_SQL_QUERY_SELECT_WHERE_CHILD_ID.replace(tableName, MEDICAL_VISITS_TABLE_NAME);

        Cursor result = database.rawQuery(DATABASE_SQL_QUERY_SELECT_WHERE_CHILD_ID ,
                new String[] { String.valueOf(childId) });
        return result;
    }

    public Cursor readSingleDiseaseNotesData(int diseaseId) {
        SQLiteDatabase database = this.getWritableDatabase();

        DATABASE_SQL_QUERY_SELECT_NOTES_WHERE_DISEASES_ID =
                getStringFromResources(R.string.database_sql_query_select_notes_where_diseases_id);

        DATABASE_SQL_QUERY_SELECT_NOTES_WHERE_DISEASES_ID =
                DATABASE_SQL_QUERY_SELECT_NOTES_WHERE_DISEASES_ID.replace(tableName, DISEASES_NOTES_TABLE_NAME);

        Cursor result = database.rawQuery(DATABASE_SQL_QUERY_SELECT_NOTES_WHERE_DISEASES_ID ,
                new String[] { String.valueOf(diseaseId) });
        return result;
    }

    public Cursor readSingleVisitRemindersData(int visitId) {
        SQLiteDatabase database = this.getWritableDatabase();

        DATABASE_SQL_QUERY_SELECT_REMINDERS_WHERE_VISIT_ID =
                getStringFromResources(R.string.database_sql_query_select_reminders_where_visit_id);

        DATABASE_SQL_QUERY_SELECT_REMINDERS_WHERE_VISIT_ID =
                DATABASE_SQL_QUERY_SELECT_REMINDERS_WHERE_VISIT_ID.replace(tableName, REMINDER_TABLE_NAME);

        Cursor result = database.rawQuery(DATABASE_SQL_QUERY_SELECT_REMINDERS_WHERE_VISIT_ID ,
                new String[] { String.valueOf(visitId) });

        return result;
    }

    public Cursor readChildAllDiseasesData(int childId) {
        SQLiteDatabase database = this.getWritableDatabase();

        DATABASE_SQL_QUERY_SELECT_WHERE_CHILD_ID =
                getStringFromResources(R.string.database_sql_query_select_where_child_id);

        DATABASE_SQL_QUERY_SELECT_WHERE_CHILD_ID =
                DATABASE_SQL_QUERY_SELECT_WHERE_CHILD_ID.replace(tableName, DISEASES_TABLE_NAME);

        Cursor result = database.rawQuery(DATABASE_SQL_QUERY_SELECT_WHERE_CHILD_ID ,
                new String[] { String.valueOf(childId) });
        return result;
    }

    public Cursor readMedicalVisitData(int id) {
        SQLiteDatabase database = this.getWritableDatabase();

        String DATABASE_SQL_QUERY_SELECT_WHERE_ID =
                getStringFromResources(R.string.database_sql_query_select_where_id);

        DATABASE_SQL_QUERY_SELECT_WHERE_ID =
                DATABASE_SQL_QUERY_SELECT_WHERE_ID.replace(tableName, MEDICAL_VISITS_TABLE_NAME);

        Cursor result = database.rawQuery(DATABASE_SQL_QUERY_SELECT_WHERE_ID ,
                new String[] { String.valueOf(id) });
        return result;
    }

    public Cursor readDiseaseData(int id) {
        SQLiteDatabase database = this.getWritableDatabase();

        String DATABASE_SQL_QUERY_SELECT_WHERE_ID =
                getStringFromResources(R.string.database_sql_query_select_where_id);

        DATABASE_SQL_QUERY_SELECT_WHERE_ID =
                DATABASE_SQL_QUERY_SELECT_WHERE_ID.replace(tableName, DISEASES_TABLE_NAME);

        Cursor result = database.rawQuery(DATABASE_SQL_QUERY_SELECT_WHERE_ID ,
                new String[] { String.valueOf(id) });
        return result;
    }

    public Cursor readDiseaseNameData(int id) {
        SQLiteDatabase database = this.getWritableDatabase();

        String DATABASE_SQL_QUERY_SELECT_WHERE_ID =
                getStringFromResources(R.string.database_sql_query_select_where_id);

        DATABASE_SQL_QUERY_SELECT_WHERE_ID =
                DATABASE_SQL_QUERY_SELECT_WHERE_ID.replace(tableName, DISEASES_NOTES_TABLE_NAME);

        Cursor result = database.rawQuery(DATABASE_SQL_QUERY_SELECT_WHERE_ID ,
                new String[] { String.valueOf(id) });
        return result;
    }

    public boolean updateMedicalVisitData(Visit visit, int visitID) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put(VISIT_COL_2, visit.getChildId());
        contentValues.put(VISIT_COL_3, visit.getName());
        contentValues.put(VISIT_COL_4, visit.getDoctor());
        //contentValues.put(VISIT_COL_5, visit.getDiseaseId());
        contentValues.put(VISIT_COL_6, visit.getDate());
        contentValues.put(VISIT_COL_7, visit.getDescription());
        contentValues.put(VISIT_COL_8, visit.getRecommendations());
        contentValues.put(VISIT_COL_9, visit.getMedicines());

        String selection = VISIT_COL_1 + "= ?";
        String[] selectionArgs = new String[] { String.valueOf(visitID) };

        int numberOfRowsAffected = database.update(MEDICAL_VISITS_TABLE_NAME, contentValues, selection, selectionArgs);

        if (numberOfRowsAffected==1)
            return true;
        else
            return false;
    }

    public boolean updateDiseaseData(Disease disease, int diseaseID) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DISEASE_COL_2, disease.getChildId());
        contentValues.put(DISEASE_COL_3, disease.getName());
        contentValues.put(DISEASE_COL_4, disease.getDate());

        String selection = DISEASE_COL_1 + "= ?";
        String[] selectionArgs = new String[] { String.valueOf(diseaseID) };

        int numberOfRowsAffected = database.update(DISEASES_TABLE_NAME, contentValues, selection, selectionArgs);

        if (numberOfRowsAffected==1)
            return true;
        else
            return false;
    }

    public boolean deleteMedicalVisitData(int visitID) {
        SQLiteDatabase database = this.getWritableDatabase();

        String selection = VISIT_COL_1 + "= ?";
        String[] selectionArgs = new String[] { String.valueOf(visitID) };

        int numberOfRowsAffected = database.delete(MEDICAL_VISITS_TABLE_NAME, selection, selectionArgs);

        if (numberOfRowsAffected==1)
            return true;
        else
            return false;
    }

    public boolean deleteDiseaseData(int diseaseID) {
        SQLiteDatabase database = this.getWritableDatabase();

        String selection = DISEASE_COL_1 + "= ?";
        String[] selectionArgs = new String[] { String.valueOf(diseaseID) };

        int numberOfRowsAffected = database.delete(DISEASES_TABLE_NAME, selection, selectionArgs);

        if (numberOfRowsAffected==1)
            return true;
        else
            return false;
    }

    public boolean deleteAllDiseaseNoteData(int diseaseID) {
        SQLiteDatabase database = this.getWritableDatabase();

        String selection = NOTES_COL_2 + "= ?";
        String[] selectionArgs = new String[] { String.valueOf(diseaseID) };

        int numberOfRowsAffected = database.delete(DISEASES_NOTES_TABLE_NAME, selection, selectionArgs);

        if (numberOfRowsAffected==1)
            return true;
        else
            return false;
    }

    public boolean deleteDiseaseNoteData(int noteID) {
        SQLiteDatabase database = this.getWritableDatabase();

        String selection = NOTES_COL_1 + "= ?";
        String[] selectionArgs = new String[] { String.valueOf(noteID) };

        int numberOfRowsAffected = database.delete(DISEASES_NOTES_TABLE_NAME, selection, selectionArgs);

        if (numberOfRowsAffected==1)
            return true;
        else
            return false;
    }

    public boolean deleteReminderData(int reminderId) {
        SQLiteDatabase database = this.getWritableDatabase();

        String selection = REMINDER_COL_1 + "= ?";
        String[] selectionArgs = new String[] { String.valueOf(reminderId) };

        int numberOfRowsAffected = database.delete(REMINDER_TABLE_NAME, selection, selectionArgs);

        if (numberOfRowsAffected==1)
            return true;
        else
            return false;
    }

}