package lab5.avdyushkin.tru.pointofsalesimulation.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class ItemDatabase {

    public ItemDatabase() {
    }

    /* Inner class that defines the table contents */
    public static abstract class DatabaseEntry implements BaseColumns {
        public static final String TABLE_NAME = "items";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_ITEM_NAME = "itemName";
        public static final String COLUMN_ITEM_PRICE = "itemPrice";
        public static final String COLUMN_ITEM_IMAGE = "itemImage";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseEntry.TABLE_NAME + " (" +
                    DatabaseEntry._ID + " INTEGER PRIMARY KEY," +
                    DatabaseEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_ITEM_NAME + TEXT_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_ITEM_PRICE + REAL_TYPE + COMMA_SEP +
                    DatabaseEntry.COLUMN_ITEM_IMAGE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseEntry.TABLE_NAME;

    public static class DatabaseHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Items.db";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

        public static Cursor getResultsFromDB(Context context) {
            ItemDatabase.DatabaseHelper mDbHelper = new DatabaseHelper(context);
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from " + DatabaseEntry.TABLE_NAME, null);
            cursor.moveToFirst();
            db.close();
            return cursor;
        }

        public static int removeItem(Context context, long rowId) {
            ItemDatabase.DatabaseHelper mDbHelper = new DatabaseHelper(context);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            // Define 'where' part of query.
            String selection = DatabaseEntry._ID + " LIKE ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = {String.valueOf(rowId)};
            // Issue SQL statement.
            int result = db.delete(DatabaseEntry.TABLE_NAME, selection, selectionArgs);
            db.close();
            return result;
        }

    }
}