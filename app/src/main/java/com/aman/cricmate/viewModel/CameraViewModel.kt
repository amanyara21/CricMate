package com.aman.cricmate.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.aman.cricmate.utils.BallDetectionWorker
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val preferenceHelper: PreferenceHelper
) : ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference
    var isRecording = mutableStateOf(false)
    var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    var angle = preferenceHelper.getAngle()
    private var sessionId = ""
    var customUserId by mutableStateOf<String?>(null)
    private val processedSessions = mutableSetOf<String>()

    fun setUserId(id: String) {
        customUserId = id
        if (angle.equals("Side", ignoreCase = true)) {
            listenForSessionChanges()
        }
    }

    fun startRecordingRequest() {
        sessionId = UUID.randomUUID().toString()
        val sessionRef = database.child("sessions").child(customUserId!!).child(sessionId)
        sessionRef.setValue(mapOf("start" to "initiate"))
        listenForSessionChanges()
    }

    private fun listenForSessionChanges() {
        val sessionUserRef = database.child("sessions").child(customUserId!!)
        var change = false
        sessionUserRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val newSessionId = snapshot.key ?: return

                if (processedSessions.contains(newSessionId)) return
                processedSessions.add(newSessionId)

                val startValue = snapshot.child("start").getValue(String::class.java)

                if (startValue == "initiate" && angle.equals("Side", ignoreCase = true)) {
                    database.child("sessions").child(customUserId!!).child(newSessionId)
                        .child("start").setValue("started")
                    change = true
                    sessionId = newSessionId
                }

                snapshot.ref.child("start").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(startSnap: DataSnapshot) {
                        val status = startSnap.getValue(String::class.java)

                        if (status == "started" && !isRecording.value) {
                            if (angle.equals("Side", ignoreCase = true) && change) {
                                startRecording(preferenceHelper.context)
                            } else if (angle.equals("front", ignoreCase = true)) {
                                startRecording(preferenceHelper.context)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    @SuppressLint("CheckResult")
    fun startRecording(context: Context) {
        if (isRecording.value) return
        Toast.makeText(preferenceHelper.context, sessionId, Toast.LENGTH_LONG).show()

        isRecording.value = true
        val time = System.currentTimeMillis()
        Log.d("videoTime", time.toString())
        val fileName = "video_${time}.mp4"
        val outputFile = File(context.cacheDir, fileName)
        val outputOptions = FileOutputOptions.Builder(outputFile).build()

        recording = videoCapture?.output
            ?.prepareRecording(context, outputOptions)
            ?.start(ContextCompat.getMainExecutor(context)) { event ->
                if (event is VideoRecordEvent.Finalize) {
                    isRecording.value = false
                    CoroutineScope(Dispatchers.IO).launch {


                        enqueueBallDetectionWorker(
                            context = context,
                            time = time.toString(),
                            videoFile = outputFile,
                            sessionId = sessionId,
                            userId = customUserId!!,
                            angle = angle ?: "front"
                        )

                    }

                }
            }

        CoroutineScope(Dispatchers.Main).launch {
            delay(10000)
            stopRecording()
        }
    }

    private fun stopRecording() {
        recording?.stop()
        recording = null
        isRecording.value = false
    }

    private fun enqueueBallDetectionWorker(
        context: Context,
        time: String,
        videoFile: File,
        sessionId: String,
        userId: String,
        angle: String
    ) {

        Log.d("videoTime", time)
        val inputData = workDataOf(
            "videoPath" to videoFile.absolutePath,
            "sessionId" to sessionId,
            "userId" to userId,
            "angle" to angle,
            "time" to time
        )

        val workRequest = OneTimeWorkRequestBuilder<BallDetectionWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

}
