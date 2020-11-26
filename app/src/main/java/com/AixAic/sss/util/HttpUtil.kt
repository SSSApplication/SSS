package com.AixAic.sss.util

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object HttpUtil {
    fun generateUploadBody1(file: File, fileKey: String): RequestBody {
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("fileKey", fileKey)
            .addFormDataPart("file", file.name, RequestBody.create(MediaType.parse("image/*"), file))
            .build()
        return body
    }
}