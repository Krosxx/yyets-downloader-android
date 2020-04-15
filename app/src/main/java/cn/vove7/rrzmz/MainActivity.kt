package cn.vove7.rrzmz

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.yyets.zimuzu.db.bean.FilmCacheBean
import com.yyets.zimuzu.fileloader.RRFilmDownloadManager
import kotlinx.android.synthetic.main.activity_main.*
import tv.zimuzu.sdk.p4pclient.P4PClientEvent
import tv.zimuzu.sdk.p4pclient.P4PStat
import java.io.File
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class MainActivity : AppCompatActivity(), P4PClientEvent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getDownloadDir(0).mkdirs()
        getDownloadDir(1).mkdirs()
//        RRFilmDownloadManager.instance.init(application)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    "android.permission.WRITE_EXTERNAL_STORAGE",
                    "android.permission.READ_EXTERNAL_STORAGE"
                ), 1
            )
        }


        RRFilmDownloadManager.instance.setP4PListener(this)
    }

    fun dn(view: View) {
        if (RRFilmDownloadManager.instance.mFilmCacheMap.contains("650ed8f2a2fecc011f17e0edf3154b6f2e878cf2")) {
            RRFilmDownloadManager.instance.stopLoading("650ed8f2a2fecc011f17e0edf3154b6f2e878cf2")
            log("停止下载")
            return
        }

        val cacheBean = FilmCacheBean(
            "111",
            "650ed8f2a2fecc011f17e0edf3154b6f2e878cf2",
            "惊异传奇",
            "惊异传奇.Amazing.Stories.S01E01.中英字幕.WEBrip.720P-人人影视.mp4",
            "650ed8f2a2fecc011f17e0edf3154b6f2e878cf2",
            "1",
            "1",
            645322560L,
            "mp4",
            null as String?,
            "yyets://N=惊异传奇.Amazing.Stories.S01E01.中英字幕.WEBrip.720P-人人影视.mp4|S=645322560|H=650ed8f2a2fecc011f17e0edf3154b6f2e878cf2|",
            ""
        )

        try {
            log("开始下载")
            RRFilmDownloadManager.instance.downloadFilm(
                cacheBean
            )
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun onP4PClientInited() {
        log("onP4PClientInited")
    }

    override fun onP4PClientRestarted() {
        log("onP4PClientRestarted")
    }

    override fun onP4PClientStarted() {
        log("onP4PClientStarted")
    }

    override fun onTaskStat(p4PStat: P4PStat?) {
        log(p4PStat?.string())
    }

    private fun log(s: String?) {
        Log.d("RRFilmDownloadManager", s)
        log_text.append(s + "\n")
    }

    private fun getDownloadDir(type: Int): File {
        val paths: String = Environment.getExternalStorageDirectory().path

        return File(
            paths,
            "Android/data/$packageName/${if (type == 0) "." else ""}download"
        )
    }

}
