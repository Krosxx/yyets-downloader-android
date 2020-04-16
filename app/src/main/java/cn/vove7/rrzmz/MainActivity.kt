package cn.vove7.rrzmz

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.yyets.zimuzu.db.DBCache
import com.yyets.zimuzu.db.bean.FilmCacheBean
import com.yyets.zimuzu.fileloader.RRFilmDownloadManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_cache.view.*
import tv.zimuzu.sdk.p4pclient.P4PClientEvent
import tv.zimuzu.sdk.p4pclient.P4PStat
import java.io.File
import java.util.*


typealias DlStatus = Int

val DlStatus.toString: String
    get() {
        return when (this) {
            RRFilmDownloadManager.STATUS_COMPLETE -> "完成"
            RRFilmDownloadManager.STATUS_PAUSED -> "暂停"
            RRFilmDownloadManager.STATUS_DOWNLOADING -> "0k/s"
            RRFilmDownloadManager.STATUS_WAITING -> "等待"
            else -> "未知"
        }

    }

class MainActivity : AppCompatActivity(), P4PClientEvent {

    private val caches: ArrayList<FilmCacheBean> by lazy {
        DBCache.instance.allCacheItemsByTime
    }

    private val adapter by lazy {
        Adapter(caches)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    "android.permission.WRITE_EXTERNAL_STORAGE",
                    "android.permission.READ_EXTERNAL_STORAGE"
                ), 1
            )
        }

        list_view.adapter = adapter
        RRFilmDownloadManager.instance.setP4PListener(this)
    }

    fun onViewClick(view: View) = when (view.id) {
        R.id.add_task -> {
            AlertDialog.Builder(this).setTitle("新建下载")
                .setView(R.layout.dialog_new_task)
                .setPositiveButton("确定") { d, _ ->
                    val et: EditText? = (d as AlertDialog).findViewById(R.id.edit_text)
                    val uri = et?.text.toString()
                    if (uri.startsWith("yyets://") && uri.endsWith("|")) {
                        val cacheBean = FilmCacheBean.parseFromUri(
                            uri,
                            uri.hashCode().toString(),
                            ""
                        )
                        RRFilmDownloadManager.instance.downloadFilm(cacheBean)
                        caches.add(cacheBean)
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this, "下载链接错误", Toast.LENGTH_LONG).show()
                    }
                }.show()
        }
        R.id.pause_all -> {
            RRFilmDownloadManager.instance.pauseAllLoading()
            adapter.notifyDataSetChanged()
        }
        R.id.start_all -> {
            RRFilmDownloadManager.downloadUncompleteTask()
            adapter.notifyDataSetChanged()
        }
        else -> {
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
        p4PStat?.stats?.forEach {
            Log.d("Debug :", "onTaskStat  ----> ${it.finishedSize}/${it.fileSize}")
            val fid = it.id
            caches.find { i -> i.mFileId == fid }?.apply {
                mLoadPosition = it.finishedSize
            }
            adapter.notifySpeed(fid, "${it.downSpeed shr 10}K/s")
        }
        log(p4PStat?.string())
    }

    private fun log(s: String?) {
        Log.d("RRFilmDownloadManager", s)
    }

}

class Adapter(val caches: List<FilmCacheBean>) :
    BaseAdapter() {

    private val speedMap = mutableMapOf<String, String>()

    fun notifySpeed(fileId: String, speed: String) {
        speedMap[fileId] = speed
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = getItem(position)
        return (convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_cache, parent, false)).apply {

            val status = RRFilmDownloadManager.getStatus(item)

            status_view.text = status?.let {
                if (it == RRFilmDownloadManager.STATUS_DOWNLOADING)
                    speedMap[item.mFileId] + "\t\t ${item.mLoadPosition}/${item.mLength}" else it.toString
            }
            name_view.text = "${item.mFilmName}  S${item.mSeason}E${item.mEpisode}"
            action_button.text = when (status) {
                RRFilmDownloadManager.STATUS_DOWNLOADING -> "暂停"
                RRFilmDownloadManager.STATUS_WAITING -> "等待"
                RRFilmDownloadManager.STATUS_PAUSED -> "开始"
                RRFilmDownloadManager.STATUS_COMPLETE -> "播放"
                else -> "。。。"
            }
            progress_view.progress = (item.mLoadPosition * 100 / item.mLength).toInt()

            action_button.setOnClickListener {
                when (status) {
                    RRFilmDownloadManager.STATUS_DOWNLOADING -> {
                        RRFilmDownloadManager.instance.pauseLoading(item)
                        notifyDataSetChanged()
                    }
                    RRFilmDownloadManager.STATUS_WAITING -> "等待"
                    RRFilmDownloadManager.STATUS_PAUSED -> {
                        RRFilmDownloadManager.instance.downloadFilm(item)
                        notifyDataSetChanged()
                    }
                    RRFilmDownloadManager.STATUS_COMPLETE -> {
                        val intentShareFile = Intent(Intent.ACTION_VIEW)
                        intentShareFile.type = "video/*"
                        val f = File(item.mFileName)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            val contentUri: Uri = FileProvider.getUriForFile(
                                context, BuildConfig.APPLICATION_ID + ".fileProvider",
                                f
                            )
                            intentShareFile.data = contentUri
                            intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        } else {
                            intentShareFile.data = Uri.fromFile(f)
                        }
                        context.startActivity(Intent.createChooser(intentShareFile, "播放"))
                    }
                    else -> "。。。"
                }
            }
        }
    }

    override fun getItem(position: Int): FilmCacheBean = caches[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = caches.size
}
