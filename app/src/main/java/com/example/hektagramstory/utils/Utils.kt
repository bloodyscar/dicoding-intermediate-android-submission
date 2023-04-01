package com.example.hektagramstory.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


private const val FILENAME_FORMAT = "dd-MMM-yyyy"
val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())


fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

/**
 * Most phone cameras are landscape, meaning if you take the photo in portrait,
 *the resulting photos will be rotated 90 degrees. In this case, the camera software
 * should populate the Exif data with the orientation that the photo should be viewed in.
 */
fun exif(currentPhotoPath: String): Bitmap? {
    val ei = ExifInterface(currentPhotoPath)
    val orientation: Int = ei.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_UNDEFINED
    )
    val rotatedBitmap: Bitmap? = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> TransformationUtils.rotateImage(
            BitmapFactory.decodeFile(
                currentPhotoPath
            ), 90
        )
        ExifInterface.ORIENTATION_ROTATE_180 -> TransformationUtils.rotateImage(
            BitmapFactory.decodeFile(
                currentPhotoPath
            ), 180
        )
        ExifInterface.ORIENTATION_ROTATE_270 -> TransformationUtils.rotateImage(
            BitmapFactory.decodeFile(
                currentPhotoPath
            ), 270
        )
        ExifInterface.ORIENTATION_NORMAL -> BitmapFactory.decodeFile(currentPhotoPath)
        else -> BitmapFactory.decodeFile(currentPhotoPath)
    }
    return rotatedBitmap
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}


suspend fun reduceFileImage(file: File): File = withContext(Dispatchers.IO) {
    val bitmap = exif(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > 1000000)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return@withContext file
}
