package com.example.pixplore.data.repository

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.example.pixplore.domain.repository.Downloader
import java.io.File

class AndroidImageDownloader(
    context : Context
) : Downloader {
    private val downloadManager = context
        .getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    override fun downlaodFile(url: String, fileName: String?) {
             try{
                 val title = fileName ?: "New Image"
                 // first we create the request then we pass this request through downloadManager.enqueue
                 val request = DownloadManager.Request(url.toUri())
                     .setMimeType("image/*")  // what we need to download  , * image is of any type

                     // setNotificationVisibility when complete donwlaod then we get notification
                     .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                     .setTitle(title) // title of the downladed file

                     // Destination of the downloaded file
                     .setDestinationInExternalPublicDir(
                         // here we say that download those image to picture directory
                         Environment.DIRECTORY_PICTURES,
                         File.separator + title + ".jpg"
                     )
                 // we provide out request to our donwload manager
                     downloadManager.enqueue(
                          request
                     )
             }catch (e: Exception){
                 e.printStackTrace()
             }
    }

}