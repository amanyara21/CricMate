package com.aman.cricmate.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import com.aman.cricmate.model.Ball
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions

fun detectBallPositionsFromVideo(context: Context, videoUri: Uri, time: String?): List<Ball> {
    val retriever = MediaMetadataRetriever()
    val positions = mutableListOf<Ball>()
    val videoTime = time?.toLong() ?: 0L

    try {
        retriever.setDataSource(context, videoUri)

        val durationMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L
        val frameInterval = 50L

        val model = LocalModel.Builder()
            .setAssetFilePath("custom_model.tflite")
            .build()

        val options = CustomObjectDetectorOptions.Builder(model)
            .setDetectorMode(CustomObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()

        val detector = ObjectDetection.getClient(options)

        var currentTimeMs = 0L
        while (currentTimeMs < durationMs) {
            val timeUs = currentTimeMs * 1000
            val frame = retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST)
            Log.d("VideoResolution", "Width: ${frame?.width}, Height: ${frame?.height}")

            frame?.let {
                val argbFrame = it.copy(Bitmap.Config.ARGB_8888, true)
                val image = InputImage.fromBitmap(argbFrame, 0)

                val detectedObjects = Tasks.await(detector.process(image))
                for (obj in detectedObjects) {
                    val label = obj.labels.firstOrNull()
                    if (label != null) {
                        val box = obj.boundingBox
                        val centerX = box.exactCenterX()
                        val centerY = box.exactCenterY()

                        if (label.index == 2) {
                            positions.add(
                                Ball(
                                    frameTimeMs = videoTime + currentTimeMs,
                                    x = centerX,
                                    y = centerY
                                )
                            )
                        }
                    }

                }
            }

            currentTimeMs += frameInterval
        }

    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        retriever.release()
    }

    return positions
}

//fun formatTime(ms: Long): String {
//    val seconds = (ms / 1000) % 60
//    val minutes = (ms / (1000 * 60)) % 60
//    val hours = (ms / (1000 * 60 * 60)) % 24
//    val millis = ms % 1000
//
//    return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis)
//}




