package ukk1451070.ac.kingston.httpkunet.transporttracker.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Jaquan on 22/01/2018.
 */
//these sites helped me to create this
//https://www.youtube.com/watch?v=aQAIMY-HzL8&t
//https://github.com/mitchtabian/SaveReadWriteDeleteSQLite/tree/master/SaveAndDisplaySQL/app/src/main
public class OfflineDatabase extends SQLiteOpenHelper{

    private static final String TAG = "OfflineDatabase";
    public static final String DATABASE_NAME = "Transport Tracker";
    public static final String TABLE_NAME = "Routes";
    public static final String COL_1 = "RouteID";
    public static final String COL_2 = "startAddress";
    public static final String COL_3 = "endAddress";

    public static final String TABLE2_NAME = "Steps";
    public static final String COL2_1 = "StepID";
    public static final String COL2_2 = "html_instructions";
    public static final String COL2_3 = "duration";
    public static final String COL2_4 = "RouteID";




    public OfflineDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (RouteID INTEGER PRIMARY KEY AUTOINCREMENT, startAddress TEXT, endAddress TEXT)");
        db.execSQL("create table " + TABLE2_NAME + " (StepID INTEGER PRIMARY KEY AUTOINCREMENT, html_instructions TEXT, duration TEXT, RouteID INTEGER, FOREIGN KEY (RouteID) REFERENCES Routes(RouteID) ON DELETE CASCADE)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE2_NAME);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    public boolean addTable1Data(String startAddress, String endAddress) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, startAddress);
        contentValues.put(COL_3, endAddress);

        Log.d(TAG, "addData: Adding " + startAddress + " and " + endAddress + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addTable2Data(String instructions, String duration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2_2, instructions);
        contentValues.put(COL2_3, duration);

        Log.d(TAG, "addData: Adding " + instructions + " and " + duration + " to " + TABLE2_NAME);

        long result = db.insert(TABLE2_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public void setForeignKey (int key) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE2_NAME + " SET " + COL2_4 + " = IFNULL( " + COL2_4 + ", " + key + " )");
    }


    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getData2(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE2_NAME + " WHERE " + COL2_4 + " = " + id ;
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getRouteID(String startAddress){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL_1 + " FROM " + TABLE_NAME +
                " WHERE " + COL_2 + " = '" + startAddress + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteRoute(int id, String startAddress){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL_1 + " = '" + id + "'" +
                " AND " + COL_2 + " = '" + startAddress + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + id + " from database.");
        db.execSQL(query);
    }
}
