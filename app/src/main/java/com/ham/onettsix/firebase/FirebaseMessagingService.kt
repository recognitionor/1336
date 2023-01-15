package com.ham.onettsix.firebase

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ham.onettsix.MainActivity
import com.ham.onettsix.R
import com.ham.onettsix.data.local.PreferencesHelper
import java.io.BufferedInputStream
import java.net.URL
import java.net.URLConnection

class FirebaseMessagingService : FirebaseMessagingService() {

    companion object {

        private const val TAG = "FirebaseMessaging"

        private const val REMOTE_DATA_KEY_TITLE = "title"
        private const val REMOTE_DATA_KEY_BODY = "body"
        private const val REMOTE_DATA_KEY_SUMMARY = "summary"
        private const val REMOTE_DATA_KEY_LARGE_IMAGE = "largeImage"
        private const val REMOTE_DATA_KEY_BIG_IMAGE = "bigImage"
        private const val REMOTE_DATA_KEY_ACTION = "action"

    }

    override fun onNewToken(token: String) {
        PreferencesHelper.getInstance(this).setFireBaseToken(token)
//        sendNotification("테스트 타이틀", "body", "summary", "largeImage", "smallImage", "action")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.notification != null) {
            val title = message.notification?.title ?: ""
            val body = message.notification?.body ?: ""
            sendNotification(title, body, "summary", "largeImage", "smallImage", "action")
        } else {
            sendNotification("title"," body", "summary", "largeImage", "smallImage", "action")
        }
    }

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private fun scheduleJob() {
        // [START dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        token?.let { tokenStr ->
            if (tokenStr.isNotEmpty()) {
                /*갱신된 토큰을 일단 Preferences 에 저장을 해둔다.*/
                PreferencesHelper.getInstance(this)
            }
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    @SuppressLint("CheckResult")
    private fun sendNotification(
        title: String,
        messageBody: String,
        summary: String,
        largeImage: String,
        bigImage: String,
        action: String
    ) {
        val intent = if (action.isNotEmpty()) {
            Intent(Intent.ACTION_VIEW, Uri.parse(action))
        } else {
            Intent(this, MainActivity::class.java)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        //이미지 온라인 링크를 가져와 비트맵으로 바꾼다.
        val channelId = getString(R.string.app_name)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder =
            NotificationCompat.Builder(this@FirebaseMessagingService, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        getBitmapFromUrl(largeImage).let {
            notificationBuilder.setLargeIcon(it)
        }

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Big Image 가 존재 할경우에만 해당 스타일 적용
        getBitmapFromUrl(bigImage).let {
            if (it != null) {
                val style: NotificationCompat.BigPictureStyle = NotificationCompat.BigPictureStyle()
                style.setSummaryText(summary)
                style.bigPicture(it)
                notificationBuilder.setStyle(style)
            }
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun getBitmapFromUrl(imgUrl: String): Bitmap? {
        try {
            val url = URL(imgUrl)
            val conn: URLConnection = url.openConnection()
            conn.connect()
            val bis = BufferedInputStream(conn.getInputStream())
            return BitmapFactory.decodeStream(bis)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
