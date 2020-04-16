package com.yyets.zimuzu.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String COL_AD_CLICK = "click";
    public static final String COL_AD_DISPLAYORDER = "displayOrder";
    public static final String COL_AD_DURATION = "duration";
    public static final String COL_AD_ID = "ad_id";
    public static final String COL_AD_PIC = "pic";
    public static final String COL_AD_SHOW_TIME = "show_time";
    public static final String COL_AD_STATUS = "status";
    public static final String COL_AD_SUBHEAD = "subhead";
    public static final String COL_AD_SUBJECT = "subject";
    public static final String COL_AD_TYPE = "ad_type";
    public static final String COL_AD_VIDEO = "video";
    public static final String COL_AD_WH = "wh";
    public static final String COL_DATA = "data";
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
    public static final String COL_KEY = "key";
    public static final String COL_MY_RECORD_EPISODE = "episode";
    public static final String COL_MY_RECORD_ID = "resource_id";
    public static final String COL_MY_RECORD_IMAGE = "image";
    public static final String COL_MY_RECORD_KIND = "kind";
    public static final String COL_MY_RECORD_KIND_IMAGE = "kind_image";
    public static final String COL_MY_RECORD_SEASON = "season";
    public static final String COL_MY_RECORD_TIME = "time";
    public static final String COL_MY_RECORD_TITLE = "title";
    public static final String COL_MY_RECORD_URL = "url";
    public static final String COL_P4P_URL = "p4p_url";
    public static final String COL_RESOURCE_EPISODE = "episode";
    public static final String COL_RESOURCE_ID = "resource_id";
    public static final String COL_RESOURCE_IMAGE_URL = "image_url";
    public static final String COL_RESOURCE_KIND = "kind";
    public static final String COL_RESOURCE_LAST_RECORD = "record";
    public static final String COL_RESOURCE_STATUS = "status";
    public static final String COL_RESOURCE_TEAM = "team";
    public static final String COL_RESOURCE_TITLE = "title";
    public static final String COL_RESOURCE_UPDATE_AT = "update_at";
    public static final String COL_SEARCH_KIND = "kind";
    public static final String COL_SEARCH_TITLE = "title";
    public static final String COL_SETTING_MOBILE_DOWNLOAD_ALLOW = "mobile_download_allow";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_USER_ACCOUNT = "account";
    public static final String COL_USER_AVATOR = "userpic";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_GROUP_NAME = "groupName";
    public static final String COL_USER_ID = "id";
    public static final String COL_USER_IS_INTERNAL_GROUP = "is_internal_group";
    public static final String COL_USER_IS_LOGIN = "isLogin";
    public static final String COL_USER_NICK_NAME = "nickname";
    public static final String COL_USER_PASSWORD = "password";
    public static final String COL_USER_SEX = "sex";
    public static final String COL_USER_TOKEN = "token";
    public static final String COL_USER_UID = "uid";
    public static final String COL_VALIDITY = "validity";
    private static final String DB_NAME = "renren.db";
    private static final int DB_VERSION = 7;
    public static final String TB_FILE_DOWNLOAD = "file_download";

    public DBHelper(Context context) {
        super(context, DB_NAME, (SQLiteDatabase.CursorFactory) null, 7);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS db_cache;");
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.setLength(0);
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS ").append(TB_FILE_DOWNLOAD).append(" (").append("url").append(" TEXT NOT NULL PRIMARY KEY, ").append(COL_FILM_NAME).append(" TEXT, ").append(COL_FILE_NAME).append(" TEXT, ").append("film_id").append(" VARCHAR, ").append(COL_FILE_ID).append(" VARCHAR, ").append(COL_FILE_LENGTH).append(" VARCHAR, ").append("season").append(" VARCHAR, ").append("episode").append(" VARCHAR, ").append(COL_FILE_SIZE).append(" VARCHAR, ").append(COL_FILM_SUBTITLE).append(" VARCHAR, ").append(COL_FILM_FORMATTED).append(" VARCHAR, ").append(COL_DOWNLOAD_TIME).append(" VARCHAR, ").append(COL_FILE_LOAD_POS).append(" VARCHAR, ").append(COL_P4P_URL).append(" TEXT ").append(COL_IMG_URL).append(" TEXT ").append(");");
        sqLiteDatabase.execSQL(sqlBuilder.toString());
        sqLiteDatabase.execSQL(sqlBuilder.toString());
    }

    public void onOpen(SQLiteDatabase sqLiteDatabase) {

    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }
}
