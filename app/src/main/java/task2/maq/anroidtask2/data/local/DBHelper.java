package task2.maq.anroidtask2.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Posts.db";

    private final String TEXT_TYPE = " TEXT";

    private final String INTEGER_TYPE = " INTEGER";

    private final String SQL_CREATE_FRIEND_TABLE = "CREATE TABLE " +
            PostEntry.TABLE_NAME + " (" +
            PostEntry.ID_COLUMN + INTEGER_TYPE + " PRIMARY KEY," +
            PostEntry.AUTHOR_NAME_COLUMN + TEXT_TYPE + "," +
            PostEntry.TEXT_COLUMN + TEXT_TYPE + "," +
            PostEntry.CREATE_DATE_COLUMN + TEXT_TYPE + "," +
            PostEntry.IMAGE_COLUMN + TEXT_TYPE +
            " )";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FRIEND_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}