package com.yyets.zimuzu.db.bean;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.yyets.zimuzu.net.entity.FilmEpisodeCacheFile;
import com.yyets.zimuzu.util.ThrowableExtension;
import com.yyets.zimuzu.util.ZimuzuHelper;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilmCacheBean implements Serializable, Comparable<FilmCacheBean> {
    public static final int STATE_LOAD = 1;
    public static final int STATE_PAUSE = 0;
    private static final long serialVersionUID = 1;
    public boolean channletype;
    public boolean isCheck;
    public boolean isDeleted;
    public long mDownloadTime;
    public String mDownloadUrl;
    public String mEpisode;
    public String mFileId;
    public String mFileName;
    public String mFilmId;
    public String mFilmImg;
    public String mFilmName;
    public String mFormatted;
    public long mLength;
    public long mLoadPosition;
    public long mLoadRate;
    public String mP4PUrl;
    public float mProgress;
    public String mSeason;
    public long mSize;
    public int mStatus;
    public String mSubtitle;

    public boolean isChannletype() {
        return this.channletype;
    }

    public void setChannletype(boolean channletype2) {
        this.channletype = channletype2;
    }

    public boolean isCheck() {
        return this.isCheck;
    }

    public void setCheck(boolean check) {
        this.isCheck = check;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public String toString() {
        return "FilmCacheBean{mFilmId='" + this.mFilmId + '\'' + ", mFilmName='" + this.mFilmName + '\'' + ", mFilmImg='" + this.mFilmImg + '\'' + ", mFileId='" + this.mFileId + '\'' + ", mFileName='" + this.mFileName + '\'' + ", mDownloadUrl='" + this.mDownloadUrl + '\'' + ", mSeason='" + this.mSeason + '\'' + ", mEpisode='" + this.mEpisode + '\'' + ", mSize=" + this.mSize + ", mFormatted='" + this.mFormatted + '\'' + ", mSubtitle='" + this.mSubtitle + '\'' + ", mLength=" + this.mLength + ", mLoadPosition=" + this.mLoadPosition + ", mDownloadTime=" + this.mDownloadTime + ", mP4PUrl='" + this.mP4PUrl + '\'' + ", mProgress=" + this.mProgress + ", mLoadRate=" + this.mLoadRate + ", mStatus=" + this.mStatus + ", channletype=" + this.channletype + ", isCheck=" + this.isCheck + ", isDeleted=" + this.isDeleted + '}';
    }

    public FilmCacheBean() {
        this.mFilmId = "";
        this.mFilmName = "";
        this.mFilmImg = "";
        this.mFileId = "";
        this.mFileName = "";
        this.mDownloadUrl = "";
        this.mSeason = "";
        this.mEpisode = "";
        this.mFormatted = "";
        this.mLength = 0;
        this.mLoadPosition = 0;
        this.mDownloadTime = 0;
        this.mP4PUrl = "";
        this.mProgress = 0.0f;
        this.mLoadRate = 0;
        this.mStatus = 0;
        this.isCheck = false;
        this.isDeleted = false;
    }

    public FilmCacheBean(String filmId, String fileId, String filmName, String fileName, String url, String season, String episode, long size, String resFmt, String subtitle, String p4pUrl, String filmImg) {
        this(filmId, fileId, filmName, fileName, url, season, episode, size, resFmt, subtitle, 0, size, p4pUrl, filmImg);
    }

    public FilmCacheBean(String filmId, String fileId, String filmName, String fileName, String url, String season, String episode, long size, String resFmt, String subtitle, long loadPos, long len, String p4pUrl, String filmImg) {
        this.mFilmId = "";
        this.mFilmName = "";
        this.mFilmImg = "";
        this.mFileId = "";
        this.mFileName = "";
        this.mDownloadUrl = "";
        this.mSeason = "";
        this.mEpisode = "";
        this.mFormatted = "";
        this.mLength = 0;
        this.mLoadPosition = 0;
        this.mDownloadTime = 0;
        this.mP4PUrl = "";
        this.mProgress = 0.0f;
        this.mLoadRate = 0;
        this.mStatus = 0;
        this.isCheck = false;
        this.isDeleted = false;
        this.mFilmId = filmId;
        this.mFileId = fileId;
        this.mFilmName = filmName;
        this.mFileName = fileName;
        this.mDownloadUrl = url;
        this.mSeason = season;
        this.mEpisode = episode;
        this.mSize = size;
        this.mFormatted = resFmt;
        this.mLoadPosition = loadPos;
        this.mLength = len;
        this.mSubtitle = subtitle;
        this.mP4PUrl = p4pUrl;
        this.mFilmImg = filmImg;
    }

    public List<FilmEpisodeCacheFile> getSubtitleList() {
        List<FilmEpisodeCacheFile> subtitleList = new ArrayList<>();
        if (!TextUtils.isEmpty(this.mSubtitle)) {
            try {
                JSONArray subtitleJa = new JSONArray(this.mSubtitle);
                for (int i = 0; i < subtitleJa.length(); i++) {
                    JSONObject subtitleJs = subtitleJa.getJSONObject(i);
                    FilmEpisodeCacheFile subtitle = new FilmEpisodeCacheFile();
                    subtitle.address = subtitleJs.getString("address");
                    if (!TextUtils.isEmpty(subtitle.address) && subtitle.address.endsWith("srt")) {
                        subtitle.way = subtitleJs.getString("way");
                        subtitle.itemid = subtitleJs.getString(ZimuzuHelper.EXT_ITEM_ID);
                        subtitle.dateline = subtitleJs.getString("dateline");
                        subtitleList.add(subtitle);
                    }
                }
            } catch (JSONException e) {
                ThrowableExtension.printStackTrace(e);
            }
        }
        return subtitleList;
    }

//    public static boolean isCacheFinish(String url) {
//        FilmCacheBean cache = DBCache.instance.getFilmDownloadBean(url);
//        return cache != null && cache.mLength > 0 && cache.mLoadPosition >= cache.mLength;
//    }

//    public static void playFilmCache(Context contex, String url) {
//        FilmCacheBean cache = DBCache.instance.getFilmDownloadBean(url);
//        if (cache != null && cache.mLoadPosition >= cache.mLength) {
//            NavigationUtil.toFilmFullscreenPlayActivity(contex, cache, cache.mFileName);
//        }
//    }

    public boolean isFinished() {
        return this.mLength > 0 && this.mLoadPosition >= this.mLength;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilmCacheBean that = (FilmCacheBean) o;
        if (this.mFileName != null) {
            return this.mFileName.equals(that.mFileName);
        }
        if (this.mFilmId != null) {
            return this.mFilmId.equals(that.mFilmId);
        }
        if (that.mFileName != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this.mFileName != null) {
            return this.mFileName.hashCode();
        }
        return 0;
    }

    public int compareTo(@NonNull FilmCacheBean o) {
        int s1;
        int s2;
        int e1;
        int e2;
        int ret = this.mFilmName.compareTo(o.mFilmName);
        if (ret != 0) {
            return ret;
        }
        if (StringUtils.isNumeric(this.mSeason)) {
            s1 = Integer.parseInt(this.mSeason);
        } else {
            s1 = 0;
        }
        if (StringUtils.isNumeric(o.mSeason)) {
            s2 = Integer.parseInt(o.mSeason);
        } else {
            s2 = 0;
        }
        int ret2 = s1 - s2;
        if (ret2 != 0) {
            return ret2;
        }
        if (StringUtils.isNumeric(this.mEpisode)) {
            e1 = Integer.parseInt(this.mEpisode);
        } else {
            e1 = 0;
        }
        if (StringUtils.isNumeric(o.mEpisode)) {
            e2 = Integer.parseInt(o.mEpisode);
        } else {
            e2 = 0;
        }
        return e1 - e2;
    }
}
