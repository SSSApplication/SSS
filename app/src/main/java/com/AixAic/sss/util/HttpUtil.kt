package com.AixAic.sss.util

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object HttpUtil {
    fun imageUploadBody(file: File, jid: String): RequestBody {
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("jid", jid)
            .addFormDataPart("file", file.name, RequestBody.create(MediaType.parse("image/*"), file))
            .build()
        return body
    }
    fun fileUploadBody(file: File, jid: String): RequestBody {
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("jid", jid)
            .addFormDataPart("file", file.name, RequestBody.create(MediaType.parse("file/*"), file))
            .build()
        return body
    }
}