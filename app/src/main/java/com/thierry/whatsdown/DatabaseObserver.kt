package com.thierry.whatsdown

import android.app.Service
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import com.google.firebase.firestore.Query
import com.thierry.whatsdown.database.DataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DatabaseObserver() : Service() {
    private var wakeLock: PowerManager.WakeLock? = null
    private var isServiceStarted = false
    private var currentUser: User? = null

    override fun onBind(p0: Intent?): IBinder? {
        Log.d(TAG, "onBind: Some component want to bind with the service")
        // We don't provide binding, so return null
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: The service has been created".uppercase())
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: The service has been destroyed".uppercase())
    }

    private var lastCheckTime: Long = 0

    private fun checkForNewMessage(currentUser: User) {
        val connection = DataBase.connect()

        GlobalScope.launch(Dispatchers.Main) {
            val chats = User.getChats(currentUser)

            chats.forEach { chat ->
                val chatRef = connection.collection("chats").document(chat.id)

                val dbMessage = connection.collection("messages")
                    .whereEqualTo("chat", chatRef)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .await()
                    .documents
                    .firstOrNull()

                dbMessage?.getTimestamp("timestamp")?.let { messageTime ->
                    if (messageTime.toDate().time > lastCheckTime) {
                        //TODO: Send notification
                        Log.d(TAG, "New message in chat ${chat.id}")
                    } else {
                        Log.d(TAG, "checkForNewMessage: No new messages")
                    }
                }
            }

            // Update the last check time
            lastCheckTime = System.currentTimeMillis()
        }
    }

    private fun startService() {
        if (isServiceStarted) return
        isServiceStarted = true

        // we need this lock so our service gets not affected by Doze Mode
        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DatabaseObserver::lock").apply {
                    acquire()
                }
            }

        // we're starting a loop in a coroutine
        GlobalScope.launch(Dispatchers.IO) {
            while (isServiceStarted) {
                launch(Dispatchers.IO) {
                    currentUser?.let { checkForNewMessage(it) }
                }
                delay(1 * 5 * 1000)
            }
        }

        Log.d(TAG, "startService: Service started")
    }

    private fun stopService() {
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            stopForeground(true)
            stopSelf()
        } catch (e: Exception) {
            Log.d(TAG, "stopService: Service stopped without being started: ${e.message}")
        }
        isServiceStarted = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        currentUser = intent?.getSerializableExtra("user") as User
        Log.d(TAG, "onStartCommand: Service started")
        startService()
        return START_STICKY
    }
}