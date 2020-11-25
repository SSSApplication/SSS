package com.AixAic.sss.util

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object HttpUtil {
    fun generateUploadBody(file: File, fileKey: String, keyValues: HashMap<String, String>.() -> Unit = {}): RequestBody {

        val body = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        // EncodeUtil.urlEncodeUtf8  这里是为了避免中文文件名引起的乱码问题。 EncodeUtil.urlEncodeUtf8(file.name),
        val part=  MultipartBody.Part.createFormData(fileKey, null, body)

        val params = HashMap<String, String>().apply(keyValues)

        val mulBody = MultipartBody.Builder().apply {
            addPart(part)

            params.map {
                addFormDataPart(it.key, it.value)
            }
        }.build()
        return mulBody
    }
    fun generateUploadBody1(file: File, fileKey: String): RequestBody {
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("fileKey", fileKey)
            .addFormDataPart("file", file.name, RequestBody.create(MediaType.parse("image/*"), file))
            .build()
        return body
    }
}