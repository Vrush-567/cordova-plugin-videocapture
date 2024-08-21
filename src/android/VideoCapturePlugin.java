package com.example.videocapture;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.provider.MediaStore;
import android.app.Activity;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class VideoCapturePlugin extends CordovaPlugin {

    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private CallbackContext callbackContext;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        if (action.equals("captureVideo")) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10); // Limit video to 10 seconds
            cordova.startActivityForResult(this, intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    InputStream videoStream = cordova.getActivity().getContentResolver().openInputStream(intent.getData());
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];

                    int len = 0;
                    while ((len = videoStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, len);
                    }

                    String base64Video = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                    this.callbackContext.success(base64Video);
                } catch (Exception e) {
                    this.callbackContext.error("Failed to capture video: " + e.getMessage());
                }
            } else {
                this.callbackContext.error("Video capture failed or was canceled.");
            }
        }
    }
}
