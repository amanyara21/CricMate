package com.aman.cricmate.screen

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.aman.cricmate.di.UserSessionManager
import com.aman.cricmate.viewModel.CameraViewModel
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@Composable
fun CameraScreen(navController: NavHostController, user:UserSessionManager, viewModel: CameraViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    LaunchedEffect(user) {
        user.id.value?.let { viewModel.setUserId(it) }
    }
    LaunchedEffect(Unit) {
        user.id.value?.let { viewModel.setUserId(it) }
        val cameraProvider = context.getCameraProvider()
        val preview = Preview.Builder().build().also {
            it.surfaceProvider = previewView.surfaceProvider
        }

        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HD))
            .build()

        val videoCapture = VideoCapture.withOutput(recorder)
        viewModel.videoCapture = videoCapture

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview,
            videoCapture
        )
    }

    Column(Modifier.fillMaxSize()) {
        AndroidView(factory = { previewView }, modifier = Modifier.weight(1f))

        Column(Modifier.padding(16.dp)) {
            Text("Angle: ${viewModel.angle}")
            Spacer(Modifier.height(12.dp))


            Button(
                onClick = {
                    viewModel.startRecordingRequest()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewModel.isRecording.value
            ) {
                Text(if (viewModel.isRecording.value) "Stop Recording" else "Start Recording")
            }
        }
    }
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCancellableCoroutine { cont ->
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            cont.resume(cameraProviderFuture.get())
        }, ContextCompat.getMainExecutor(this))
    }
