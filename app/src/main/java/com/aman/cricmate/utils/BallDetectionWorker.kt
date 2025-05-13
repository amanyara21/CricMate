package com.aman.cricmate.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.database.FirebaseDatabase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File

@HiltWorker
class BallDetectionWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val apiService: ApiService
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("VideoResolution", "working")
        val videoPath = inputData.getString("videoPath") ?: return Result.failure()
        val time = inputData.getString("time")
        val videoUri = Uri.fromFile(File(videoPath))

        return try {
            val positions = detectBallPositionsFromVideo(applicationContext, videoUri, time)

            val sessionId = inputData.getString("sessionId") ?: return Result.failure()
            val userId = inputData.getString("userId") ?: return Result.failure()
            val angle = inputData.getString("angle") ?: "Unknown"

            // Upload to Firebase
            val database = FirebaseDatabase.getInstance().reference
            val sessionRef = database.child("sessions").child(userId).child(sessionId)
            sessionRef.child(angle).setValue(positions)

            val response = apiService.analyseBall(userId, sessionId)

            if(response.isSuccessful) Result.success()
            else Result.retry()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}
