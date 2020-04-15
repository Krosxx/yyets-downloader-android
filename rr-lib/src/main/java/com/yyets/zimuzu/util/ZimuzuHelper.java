package com.yyets.zimuzu.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.view.View;
import android.webkit.MimeTypeMap;

import androidx.collection.SimpleArrayMap;

import com.yyets.freeappbase.util.MD5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ZimuzuHelper {
    public static final String ACTION_MAIN_TYPE = "com.yyets.pro.main";
    public static final String ACTION_MEMAIN_TYPE = "com.yyets.pro.memain";
    public static int ACTIVITY_GONE = 10010;
    private static final float BITMAP_SCALE = 0.4f;
    public static final String CHANNEL = "channel";
    public static final String CODE_KEY = "34abbaf8fac7d2c71e3c1753a86e5958";
    public static String DEFAULT_TIME_FORMAT = "HH:mm:ss";
//    public static final String DOWNLOAD_PATH = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/yyets/download/");
    public static final String EXTRA_FILM_CACHE = "extra_film_cache";
    public static final String EXTRA_FILM_EPISODE = "extra_film_episode";
    public static final String EXTRA_FILM_HEADERS = "extra_film_headers";
    public static final String EXTRA_FILM_NAME = "extra_film_name";
    public static final String EXTRA_FILM_SEASON = "extra_film_season";
    public static final String EXTRA_FROM_PATH = "extra_from_path";
    public static final String EXTRA_FROM_TYPE = "extra_from_type";
    public static final String EXTRA_ONLINE_VID = "extra_online_vid";
    public static final String EXTRA_PATH = "extra_path";
    public static final String EXT_CHANNEL_TYPE = "channeltype";
    public static final String EXT_COVER_URL = "image";
    public static final String EXT_DATE_TIME = "datetime";
    public static final String EXT_DOWNLOAD_CACHE = "download_cache";
    public static final String EXT_DOWNLOAD_CACHELISTS = "download_cache_lists";
    public static final String EXT_DOWNLOAD_CACHE_LIST = "download_cache_list";
    public static final String EXT_DOWNLOAD_TYPE = "download_type";
    public static final String EXT_ENTITLE = "entitle";
    public static final String EXT_EPISODE = "episode";
    public static final String EXT_FILM_ID = "film_id";
    public static final String EXT_ID = "id";
    public static final String EXT_ITEM_ID = "itemid";
    public static final String EXT_MULTI_FILM_ID = "ext_multi_film_id";
    public static final String EXT_MULTI_FILM_IMGURL = "ext_multi_film_imgurl";
    public static final String EXT_MULTI_FILM_NAME = "ext_multi_film_name";
    public static final String EXT_ONLY_ADD_TASK = "onlyAddTask";
    public static final String EXT_OPENFULLVIDEO_AD = "open_FullVideo_Ad";
    public static final String EXT_PLAYER_AD = "player_Ad";
    public static final String EXT_RESOURCE_TIP = "resource_tip";
    public static final String EXT_SEASON = "season";
    public static final String EXT_SUBTITLE_ID = "subtitle_id";
    public static final String EXT_TITLE = "title";
    public static final String EXT_WEB_SITE = "website";
    public static final String INVITATION_CODE = "invitationCode";
    public static final String ITEM_ID = "newsId";
    public static final String KEY_MESSAGE = "message";
    public static final String MOVE_FILE = "move_file";
    public static final String NETINOF_TYPE = "com.yyets.zimuzu.me.netinfo_not_wifi";
    public static final String NEWS_ID = "newsId";
    public static final String OPEN_AD = "com.tk.action.OPEN_AD";
    public static final String REPLY_CHANNEL = "reply_channel";
    public static final String REPLY_ITEM_ID = "reply_itemid";
    public static final String REPLY_REPLY_ID = "reply_replyid";
    public static final String TAB_CACHE = "cache";
    public static final String TAB_DISCOVERY = "discovery";
    public static final String TAB_FILM = "film";
    public static final String TAB_ME = "me";
    public static final String TAB_RECOMMEND = "recommend";
    public static final String TAB_TASK = "task";
    private static final int THUNDER_VERSIONCODE_5_18 = 10610;
    public static final String WEB_NUM = "webnumber";
    public static final String WEB_PATH = "webpath";
    public static int appStartAdTime = 20;
    public static boolean closeDownloadAd = false;
    public static boolean hasNoDownloadTask = true;
    public static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static boolean isBecameBackground = false;
    public static int isFullscreenPlay = -1;
    public static boolean isInitOk = false;
    public static boolean isMainForeground = false;
    public static boolean isMeMainForeground = false;
    public static boolean isSupperUser = false;
    public static int is_internal_group = 0;
    private static String language;
    public static int onBecameForeground = -1;
    public static int playAdTime = 20;
    public static int popUpAdTime = 20;
    public static int reviewing = 0;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static boolean springFestivalWelfare = false;
    public static boolean testAd = false;
    public static boolean testMode = false;
    public static int upgradeType = -1;
    public static int userLogin = -1;
    public static int user_upgrade = 0;
    public static boolean weekendBenefits = false;

    public static String getSystemTime() {
        return new SimpleDateFormat(DEFAULT_TIME_FORMAT).format(Calendar.getInstance().getTime());
    }

    public static String getFilmCacheKey(String filmName, String season, String episode) {
        return "film-cache-key-" + MD5.md5(filmName + season + episode);
    }


    @SuppressLint("WrongConstant")
    public static int getDayByDate(String date) {
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
            return c.get(5);
        } catch (ParseException e) {
            ThrowableExtension.printStackTrace(e);
            return 0;
        }
    }


    @SuppressLint("WrongConstant")
    public static String getNextDayByDate(String sDate) {
        try {
            Date date = ConverToDate(sDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(5, calendar.get(5) + 1);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            ThrowableExtension.printStackTrace(e);
            return "2015-01-01";
        }
    }

    public static Date ConverToDate(String strDate) throws Exception {
        return sdf.parse(strDate);
    }

    public static String getTodayDate() {
        return sdf.format(new Date());
    }

    @SuppressLint("WrongConstant")
    public static String getTomorrowDate() {
        try {
            Date date = ConverToDate(getTodayDate());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(5, calendar.get(5) + 1);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "2015-01-01";
        }
    }


    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(hexDigits[(bytes[j] >> 4) & 15]);
            buf.append(hexDigits[bytes[j] & 15]);
        }
        return buf.toString();
    }

    public static String encode(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(String strSrc) {
        byte[] bt = strSrc.getBytes();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(bt);
            return bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Invalid algorithm.");
            return null;
        }
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        for (byte b : bts) {
            String tmp = Integer.toHexString(b & 255);
            if (tmp.length() == 1) {
                des = des + "0";
            }
            des = des + tmp;
        }
        return des;
    }

    public static String getDateTimeFromMillisecond(Long millisecond) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(millisecond.longValue()));
    }

    public static String getDateTimeFromWebMillisecond(Long millisecond) {
        return new SimpleDateFormat("yy/MM/dd").format(new Date(millisecond.longValue() * 1000));
    }

    public static String utcConvertDateTime(String utc) {
        return new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(((long) Integer.parseInt(utc)) * 1000));
    }

    public static String utcConvertDateTime(int utc) {
        return new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(((long) utc) * 1000));
    }

    public static String utcConvertDateTime(long utc) {
        return new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(utc * 1000));
    }

    public static boolean isAppInstalledByPackage(Context context, String packageName) {
        List<PackageInfo> pinfo = context.getPackageManager().getInstalledPackages(0);
        List<String> pName = new ArrayList<>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                pName.add(pinfo.get(i).packageName);
            }
        }
        return pName.contains(packageName);
    }

    public static String getFlvcdKey(int t, String url) {
        return MD5.md5("5xoxf5xMMt3O1zZalnesMaaio2lkx7wU" + url + t);
    }

    public static String formatSpeed(int speed) {
        int speed2 = speed / 1024;
        if (speed2 <= 1024) {
            return String.valueOf(speed2) + "KB/s";
        }
        return String.valueOf(speed2 / 1024) + "." + String.valueOf(((speed2 % 1024) * 10) / 1024) + "MB/s";
    }


    public static String getPercent(int x, int y) {
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format(((double) x) / ((double) y));
        return result.substring(0, result.lastIndexOf("%"));
    }

    public static String getSysLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= 24) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        language = locale.getLanguage();
        return language;
    }

    private String getMIMEType(File file) {
        String name = file.getName();
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(name.substring(name.lastIndexOf(".") + 1, name.length()).toLowerCase());
    }
//
//    public static String getChanelName(String channel, Context context) {
//        if ("tv".equals(channel)) {
//            return context.getResources().getString(R.string.tv_play);
//        }
//        if ("movie".equals(channel)) {
//            return context.getResources().getString(R.string.rr_movie);
//        }
//        return context.getResources().getString(R.string.documentary);
//    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof List) {
            return ((List) obj).size() == 0;
        } else if (obj instanceof String) {
            return ((String) obj).trim().equals("");
        } else {
            if ((obj instanceof CharSequence) && obj.toString().length() == 0) {
                return true;
            }
            if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
                return true;
            }
            if ((obj instanceof Collection) && ((Collection) obj).isEmpty()) {
                return true;
            }
            if ((obj instanceof Map) && ((Map) obj).isEmpty()) {
                return true;
            }
            if ((obj instanceof SimpleArrayMap) && ((SimpleArrayMap) obj).isEmpty()) {
                return true;
            }
            if ((obj instanceof SparseArray) && ((SparseArray) obj).size() == 0) {
                return true;
            }
            if ((obj instanceof SparseBooleanArray) && ((SparseBooleanArray) obj).size() == 0) {
                return true;
            }
            if ((obj instanceof SparseIntArray) && ((SparseIntArray) obj).size() == 0) {
                return true;
            }
            if (Build.VERSION.SDK_INT >= 18 && (obj instanceof SparseLongArray) && ((SparseLongArray) obj).size() == 0) {
                return true;
            }
            if ((obj instanceof LongSparseArray) && ((LongSparseArray) obj).size() == 0) {
                return true;
            }
            if (Build.VERSION.SDK_INT < 16 || !(obj instanceof LongSparseArray) || ((LongSparseArray) obj).size() != 0) {
                return false;
            }
            return true;
        }
    }

    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName), "utf-8"));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    public static boolean isThunderInstalled(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo("com.xunlei.downloadprovider", 0);
        } catch (PackageManager.NameNotFoundException e) {
        }
        if (info != null) {
            return true;
        }
        return false;
    }

    public static int getThunderVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo("com.xunlei.downloadprovider", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    public static boolean isCreateTaskSupported(Context context) {
        return getThunderVersionCode(context) >= THUNDER_VERSIONCODE_5_18;
    }

    public static boolean isEmojiCharacter(char codePoint) {
        return !(codePoint == 0 || codePoint == 9 || codePoint == 10 || codePoint == 13 || (codePoint >= ' ' && codePoint <= 55295)) || (codePoint >= 57344 && codePoint <= 65533) || (codePoint >= 0 && codePoint <= 65535);
    }

    public static String getTextFromClip(Context context) {
        String text = "";
        @SuppressLint("WrongConstant") ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService("clipboard");
        if (!clipboardManager.hasPrimaryClip()) {
            return null;
        }
        ClipData clipData = clipboardManager.getPrimaryClip();
        ClipData.Item item = clipData.getItemAt(0);
        if (clipData != null && !isEmpty(clipData) && !TextUtils.isEmpty(item.getText())) {
            text = item.getText().toString();
        }
        return text;
    }

    public static String saveImage(Activity activity, View v, String name) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(new Rect());
        File tmpFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/shareCode/");
        if (tmpFile.exists()) {
            tmpFile.delete();
        }
        tmpFile.mkdirs();
        File sharePath = new File(tmpFile.getAbsolutePath(), name + ".png");
        Bitmap bitmap = view.getDrawingCache();
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        try {
            bitmap = Bitmap.createBitmap(bitmap, location[0], location[1], v.getWidth(), v.getHeight());
            FileOutputStream fout = new FileOutputStream(sharePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            fout.flush();
            fout.close();
            return sharePath.getPath();
        } catch (FileNotFoundException e) {
            ThrowableExtension.printStackTrace(e);
            Log.e("test", "生成预览图片失败：" + e);
        } catch (IllegalArgumentException e2) {
            Log.e("test", "width is <= 0, or height is <= 0");
        } catch (IOException e3) {
            ThrowableExtension.printStackTrace(e3);
        } finally {
            v.destroyDrawingCache();
            v.setDrawingCacheEnabled(false);
            view.destroyDrawingCache();
            view.setDrawingCacheEnabled(false);
            bitmap.recycle();
        }
        return null;
    }

    private static String intIP2StringIP(int ip) {
        return (ip & 255) + "." + ((ip >> 8) & 255) + "." + ((ip >> 16) & 255) + "." + ((ip >> 24) & 255);
    }

}
