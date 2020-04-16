package com.yyets.zimuzu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String COL_DOWNLOAD_TIME = "download_time";
    public static final String COL_FILE_ID = "file_id";
    public static final String COL_FILE_LENGTH = "length";
    public static final String COL_FILE_LOAD_POS = "load_pos";
    public static final String COL_FILE_NAME = "file_name";
    public static final String COL_FILE_SIZE = "size";
    public static final String COL_FILE_URL = "url";
    public static final String COL_FILM_EPISODE = "episode";
    public static final String COL_FILM_FORMATTED = "formatted";
    public static final String COL_FILM_ID = "film_id";
    public static final String COL_FILM_NAME = "film_name";
    public static final String COL_FILM_SEASON = "season";
    public static final String COL_FILM_SUBTITLE = "subtitle";
    public static final String COL_IMG_URL = "film_img";
    public static final String COL_P4P_URL = "p4p_url";
    private static final String DB_NAME = "renren.db";
    public static final String TB_FILE_DOWNLOAD = "file_download";

    public DBHelper(Context context) {
        super(context, DB_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS file_download (" +
                "url TEXT NOT NULL PRIMARY KEY, " +
                "film_name TEXT," +
                " file_name TEXT," +
                " film_id VARCHAR," +
                " file_id VARCHAR, " +
                "length VARCHAR, " +
                "season VARCHAR, " +
                "episode VARCHAR, " +
                "size VARCHAR, " +
                "subtitle VARCHAR, " +
                "formatted VARCHAR, " +
                "download_time VARCHAR, " +
                "load_pos VARCHAR, " +
                "p4p_url TEXT, film_img TEXT );"
        );
    }

    public void onOpen(SQLiteDatabase sqLiteDatabase) {

    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }
}
