package com.example.pruebatecnica

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.ByteArrayOutputStream
import java.io.File


class MainActivity : AppCompatActivity() {
    private val RESULT_LOAD_IMAGE = 10


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCamara.setOnClickListener {
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            startActivityForResult(intent, RESULT_LOAD_IMAGE)
        }
    }

    // Checking API functionality..
    private fun createPerson(name: String) {

        val thread = Thread(Runnable {
            try {
                val client = OkHttpClient()

                val mediaType = "application/x-www-form-urlencoded".toMediaTypeOrNull()
                val body = RequestBody.create(mediaType, "name=${name}")
                val request = Request.Builder()
                    .url("https://luxand-cloud-face-recognition.p.rapidapi.com/subject")
                    .post(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader(
                        "x-rapidapi-key",
                        "jOQNOLsfRdM6nK5z1DA2cOL2KbmUpK8Vf0vRZ17V82FsffxF01ZNWupjjZy/h6lRpvZ26aVay1XUT2p90jGf7qmKBzXXkX57OHIUDNpA2bdwgRaOM5sHRZxGrjt/kcY77hpK1LB7s/P+1oT6BKvLNxoYAUSTjqGLbs+FqZl4gG8="
//                        "ae1c770749msh1259bec7364a2f8p1b0fa5jsnb6fc54a5563c"
                    )
                    .addHeader("x-rapidapi-host", "luxand-cloud-face-recognition.p.rapidapi.com")
                    .build()

                val response = client.newCall(request).execute()
                Log.e("TEST", response.message)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        thread.start()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode === Activity.RESULT_OK && requestCode === RESULT_LOAD_IMAGE) {
            val photo = data?.getExtras()?.get("data") as Bitmap?
            photo?.let {
                val uri = getImageUri(this, it)
                if (uri != null) {
                    val file = File(getRealPathFromURI(uri))
                    // TODO: check file and upload + name..

                    createPerson("Juan")
                }
            }
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }

    fun getRealPathFromURI(uri: Uri): String? {

        var path = ""
        if (contentResolver != null) {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx: Int = cursor.getColumnIndex(Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }



}