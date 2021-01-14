package jp.techacademy.tsuyoshi.autoslideshowapp

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Handler
import java.util.*
import android.view.View
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private val PERMISSIONS_REQUEST_CODE = 100
    private var t: Timer? = null
    private var h = Handler()
    var cursor: Cursor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getContentsInfo()
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_CODE
                )
            }
        } else {
            getContentsInfo()
        }


        go_button.setOnClickListener {
            if (cursor!!.moveToNext()) {

            } else {
                cursor!!.moveToFirst()
            }
            val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor!!.getLong(fieldIndex)
            val imageUri =
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            imageView.setImageURI(imageUri)

        }
        return_button.setOnClickListener {
            if (cursor!!.moveToPrevious()) {

            } else {
                cursor!!.moveToLast()


            }
            val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor!!.getLong(fieldIndex)
            val imageUri =
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            imageView.setImageURI(imageUri)
        }
        start_button.setOnClickListener {

            if (t == null) {
                t = Timer()
                start_button.text = "停止"
                return_button.isClickable = false
                go_button.isClickable = false


                t!!.schedule(object : TimerTask() {
                    override fun run() {
                        h.post {
                            if (cursor!!.moveToNext()) {

                            } else {
                                cursor!!.moveToFirst()
                            }

                            val fieldIndex =
                                cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                            val id = cursor!!.getLong(fieldIndex)
                            val imageUri =
                                ContentUris.withAppendedId(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    id
                                )
                            imageView.setImageURI(imageUri)
                        }

                    }
                }, 2000, 2000)
            } else if (t != null) {
                t!!.cancel()
                t=null
                start_button.text = "再生"
                return_button.isClickable=true
                go_button.isClickable = true

            } else {
                t!!.cancel()
                t = null

                start_button.text = "再生"
            }


        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getContentsInfo()
                }
        }

    }

    private fun getContentsInfo() {

        val resolver = contentResolver
        cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )

        if (cursor!!.moveToFirst()) {
            val fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor!!.getLong(fieldIndex)
            val imageUri =
                ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
            imageView.setImageURI(imageUri)
        }

    }


}















