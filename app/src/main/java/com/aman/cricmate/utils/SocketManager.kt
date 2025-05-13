package com.aman.cricmate.utils

//import android.util.Log
//import io.socket.client.IO
//import io.socket.client.Socket
//import org.json.JSONObject
//class SocketManager {
//
//    var socket: Socket? = null
//    private val serverUrl = "http://192.168.114.88:5000"
//
//    fun connectSocket(sessionId: String, onStartRecording: () -> Unit, onSessionComplete: (JSONObject) -> Unit) {
//        try {
//            socket = IO.socket(serverUrl)
//            socket?.connect()
//
//            socket?.on(Socket.EVENT_CONNECT) {
//                Log.d("Socket", "Connected")
//                socket?.emit("join-room", sessionId)
//            }
//
//            socket?.on("start-recording") {
//                Log.d("Socket", "Start Recording Triggered")
//                onStartRecording()
//            }
//
//            socket?.on("session-complete") { args ->
//                val data = args[0] as JSONObject
//                Log.d("Socket", "Session Complete: $data")
//                onSessionComplete(data)
//            }
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    fun sendProcessedData(
//        sessionId: String,
//        deviceId: String,
//        angle: String,
//        result: JSONObject
//    ) {
//        val data = JSONObject()
//        data.put("sessionId", sessionId)
//        data.put("deviceId", deviceId)
//        data.put("angle", angle)
//        data.put("result", result)
//        socket?.emit("processed-data", data)
//        Log.d("Socket", "Sent processed data: $data")
//    }
//
//    fun disconnect() {
//        socket?.disconnect()
//    }
//}
