package com.AixAic.sss.ui.task

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class DownLoadCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if ("android.intent.action.MYDOWNLOAD_COMPLETE" == intent.action) {
            //在广播中取出下载任务的id
            val filename = intent.getStringExtra("filename")
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            Toast.makeText(context, "文件：${filename}"+"编号：" + id + "的下载任务已经完成！", Toast.LENGTH_SHORT).show()
        }
    }
}
//            val query = DownloadManager.Query()
//            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//            query.setFilterById(id)
//            val c: Cursor? = dm.query(query)
//            if (c != null) {
//                try {
//                    if (c.moveToFirst()) {
//                        //获取文件下载路径
//                        val fileUriIdx = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
//                        val fileUri = c.getString(fileUriIdx)
//                        var filename: String? = null
//                        if (fileUri != null) {
//                            val mFile = File(Uri.parse(fileUri).path)
//                            filename = mFile.absolutePath
//                        }
//                        val filename: String =
//                            c.getString(c.getColumnIndex(ContentResolver.))
//                        val status: Int =
//                            c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
//                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
//                            //启动更新
//                            val uri: Uri = Uri.fromFile(File(filename))
//                            if (uri != null) {
//                                val install = Intent(Intent.ACTION_VIEW)
//                                install.setDataAndType(
//                                    uri,
//                                    "application/vnd.android.package-archive"
//                                )
//                                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//
//                                context.startActivity(install)
//                            }
//                        }
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    return
//                } finally {
//                    c.close()
//                }
//            }