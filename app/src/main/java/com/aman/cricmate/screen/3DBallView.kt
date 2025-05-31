package com.aman.cricmate.screen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.aman.cricmate.viewModel.ThreeDViewModel
import com.google.android.filament.Camera
import io.github.sceneview.Scene
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import kotlinx.coroutines.delay
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


@Composable
fun ThreeDBallView(ballId: String, viewModel: ThreeDViewModel = hiltViewModel()) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val cameraNode = rememberCameraNode(engine)

    LaunchedEffect(Unit) {
        viewModel.getBallDetails(ballId)
    }

    cameraNode.position = Position(0f, 0f, 20f)
    cameraNode.setProjection(
        fovInDegrees = 90.0,
        aspect = 9.0/16,
        near = 0.1,
        far = 100.0,
        direction = Camera.Fov.VERTICAL
    )



    val ballNode = remember {
        ModelNode(
            modelInstance = modelLoader.createModelInstance("model/ball.glb"),
            scaleToUnits = 1f
        ).apply {
            position = Position(0f, 0f, 0f)
        }
    }

    val pitchNode = remember {
        ModelNode(
            modelInstance = modelLoader.createModelInstance("model/pitch.glb")
        ).apply {
            position = Position(0f, 0f, 0f)
        }
    }

    val trackingPoints = viewModel.trackingPoints

    val traceNodes = remember { mutableStateListOf<ModelNode>() }

    LaunchedEffect(trackingPoints) {
        if (trackingPoints.size >= 2) {
            Log.d("trackingPoints2", trackingPoints.toString())

            for (i in 0 until trackingPoints.lastIndex) {
                val (x1, y1, z1) = trackingPoints[i]
                val (x2, y2, z2) = trackingPoints[i + 1]

                val fromPos = mapToSceneCoordinates3D( x1, y1, z1)
                val toPos = mapToSceneCoordinates3D(x2, y2, z2)

                animateXYZPosition(ballNode, fromPos, toPos, 100)
                val traceNode = createTraceLine(modelLoader, fromPos, toPos)
                traceNodes.addAll(traceNode)
            }
        }
    }


    Scene(
        modifier = Modifier.fillMaxSize(),
        engine = engine,
        modelLoader = modelLoader,
        cameraNode = cameraNode,
        childNodes = listOf(ballNode, pitchNode)+ traceNodes
    )

}

suspend fun animateXYZPosition(node: ModelNode, from: Position, to: Position, durationMs: Long) {
    val steps = 60
    val stepTime = durationMs / steps
    val deltaX = (to.x - from.x) / steps
    val deltaY = (to.y - from.y) / steps
    val deltaZ = (to.z - from.z) / steps

    for (i in 0..steps) {
        val newX = from.x + deltaX * i
        val newY = from.y + deltaY * i
        val newZ = from.z + deltaZ * i
        node.position = Position(newX, newY, newZ)
        delay(stepTime)
    }
}

fun mapToSceneCoordinates3D(
    x: Float,
    y: Float,
    z: Float
): Position {
    val xMin = -360f
    val xMax = 360f
    val yMin = 1280f
    val yMax = 0f
    val zMin = 0f
    val zMax = 720f

    val sceneXRange = -4f to 4f
    val sceneYRange = -10f to 10f
    val sceneZRange = 0f to 10f

    val xNormalized = (x - xMin) / (xMax - xMin)
    val yNormalized = (y - yMin) / (yMax - yMin)
    val zNormalized = (z - zMin) / (zMax - zMin)

    val xScene = sceneXRange.first + xNormalized * (sceneXRange.second - sceneXRange.first)
    val yScene = sceneYRange.second - yNormalized * (sceneYRange.second - sceneYRange.first)
    val zScene = sceneZRange.first + zNormalized * (sceneZRange.second - sceneZRange.first)

    return Position(xScene, yScene, zScene)
}



fun createTraceLine(
    modelLoader: ModelLoader,
    from: Position,
    to: Position,
    segments: Int = 10
): List<ModelNode> {
    val dir = Position(to.x - from.x, to.y - from.y, to.z - from.z)
    val length = sqrt(dir.x * dir.x + dir.y * dir.y + dir.z * dir.z)

    // Normalize direction
    val nx = dir.x / length
    val ny = dir.y / length
    val nz = dir.z / length

    val nodes = mutableListOf<ModelNode>()

    val up = floatArrayOf(0f, 1f, 0f)
    val dirVec = floatArrayOf(nx, ny, nz)

    val axis = floatArrayOf(
        up[1] * dirVec[2] - up[2] * dirVec[1],
        up[2] * dirVec[0] - up[0] * dirVec[2],
        up[0] * dirVec[1] - up[1] * dirVec[0]
    )

    val dot = up[0] * dirVec[0] + up[1] * dirVec[1] + up[2] * dirVec[2]
    val angle = acos(dot.coerceIn(-1f, 1f))

    val segmentLength = length / segments

    for (i in 0 until segments) {
        val t = (i + 0.5f) / segments
        val x = from.x + dir.x * t
        val y = from.y + dir.y * t
        val z = from.z + dir.z * t

        val node = ModelNode(
            modelInstance = modelLoader.createModelInstance("model/cylinder.glb")
        ).apply {
            position = Position(x, y, z)
            scale = Position(1f, segmentLength, 1f) // Scale vertically
            rotation = axisAngleToEuler(axis[0], axis[1], axis[2], angle)
        }

        nodes.add(node)
    }

    return nodes
}

fun axisAngleToEuler(x: Float, y: Float, z: Float, angleRad: Float): Rotation {
    val sinHalfAngle = sin(angleRad / 2f)
    val cosHalfAngle = cos(angleRad / 2f)

    val qx = x * sinHalfAngle
    val qy = y * sinHalfAngle
    val qz = z * sinHalfAngle
    val qw = cosHalfAngle

    // Convert quaternion to Euler angles
    val ysqr = qy * qy

    // Roll (x-axis rotation)
    val t0 = +2.0f * (qw * qx + qy * qz)
    val t1 = +1.0f - 2.0f * (qx * qx + ysqr)
    val roll = atan2(t0, t1)

    // Pitch (y-axis rotation)
    var t2 = +2.0f * (qw * qy - qz * qx)
    t2 = t2.coerceIn(-1.0f, 1.0f)
    val pitch = asin(t2)

    // Yaw (z-axis rotation)
    val t3 = +2.0f * (qw * qz + qx * qy)
    val t4 = +1.0f - 2.0f * (ysqr + qz * qz)
    val yaw = atan2(t3, t4)

    return Rotation(x = roll, y = pitch, z = yaw)
}
