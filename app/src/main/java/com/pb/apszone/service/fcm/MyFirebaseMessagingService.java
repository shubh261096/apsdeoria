package com.pb.apszone.service.fcm;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pb.apszone.service.model.NotificationModel;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.utils.NotificationUtils;
import com.pb.apszone.view.ui.DashboardActivity;
import com.pb.apszone.view.ui.SplashActivity;

import java.util.Map;

import static com.pb.apszone.utils.AppConstants.KEY_USER_LOGIN_STATUS;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingServ";
    private static final String TITLE = "title";
    private static final String EMPTY = "";
    private static final String MESSAGE = "message";
    private static final String IMAGE = "image";
    private static final String ACTION = "action";
    private static final String DATA = "data";
    private static final String ACTION_DESTINATION = "action_destination";
    private KeyStorePref keyStorePref;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        keyStorePref = KeyStorePref.getInstance(this);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            handleData(data);

        } else if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification());
        }// Check if message contains a notification payload.
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    private void handleNotification(RemoteMessage.Notification RemoteMsgNotification) {
        Log.i(TAG, "handleNotification: Inside this");
        String message = RemoteMsgNotification.getBody();
        String title = RemoteMsgNotification.getTitle();
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setTitle(title);
        notificationModel.setMessage(message);
        Intent resultIntent;
        if (keyStorePref.getBoolean(KEY_USER_LOGIN_STATUS)) {
            resultIntent = new Intent(getApplicationContext(), DashboardActivity.class);
        } else {
            resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
        }
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationModel, resultIntent);
        notificationUtils.playNotificationSound();
    }

    private void handleData(Map<String, String> data) {
        Log.i(TAG, "handleData: Inside this");
        String title = data.get(TITLE);
        String message = data.get(MESSAGE);
        String iconUrl = data.get(IMAGE);
        String action = data.get(ACTION);
        String actionDestination = data.get(ACTION_DESTINATION);
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setTitle(title);
        notificationModel.setMessage(message);
        notificationModel.setIconUrl(iconUrl);
        notificationModel.setAction(action);
        notificationModel.setActionDestination(actionDestination);
        Intent resultIntent;
        if (keyStorePref.getBoolean(KEY_USER_LOGIN_STATUS)) {
            resultIntent = new Intent(getApplicationContext(), DashboardActivity.class);
        } else {
            resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
        }
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationModel, resultIntent);
        notificationUtils.playNotificationSound();

    }
}
