package com.ipvision.sulaiman.glviewpracticecode.camera;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CallConstants {

    public static Map<Integer, Integer> missCallCounterMap = null;
    public static int SENDER_VIDEO_ORIENTATION = 0;
    public static int RECEIVER_VIDEO_ORIENTATION = 0;
    public static String CALL_NOTIF_ID = "call_notif_id";
    public static long MEDIA_STOP_TIME = 0;

    public static final int TYPE_SEND_REGISTER = 174; // "want to start a call";
    public static final int TYPE_UPDATE_SEND_REGISTER = 374; // "want to start a call";
    public static String EXCELLENT_NETWORK = " Excellent";
    public static String VERYGOOD_NETWORK = " Good";
    public static String AVERAGE_NETWORK = " Average";
    public static String POOR_NETWORK = " Poor";
    public static String NO_NETWORK = "Reconnecting...";
    public static boolean IS_CALL_ENGAGED = false;
    public static final String TAG_SIGNALLING = "CallSignallingLOG";
    public static final int NETWORK_CONNECTION_RECEIVER = 0;
    public static final int WIFI_REGISTER_RECEIVER = 1;
    public static final int CALL_LOG_OFFSET = 100;
    public static int SWITCH_OPPPONENT_SURFACE_TO_SMALL = 1;
    public static int SWITCH_OPPPONENT_SURFACE_TO_BIG = 0;
    public static final int PACKET_SIZE = 2048;
    public static final int VOICE_MEDIA = 0;
    public static final int VOICE_REGISTER = 1;
    public static final int VOICE_UNREGISTERED = 2;
    public static final int VOICE_REGISTER_CONFIRMATION = 3;
    public static final int VIDEO_DATA_AVAILABLE = 110;
    public static long OUTGOING_CALL_TIME = 0;
    //Connectivity Engine Constants
    public static final int Media_Audio = 1;
    public static final int Media_Video = 2;
    public static int telephony_st = -1;

    public static final int maximumVideoResolution = 640;
    public static final int minimumVideoResolution = 352;
    public static int m_iCampturedWidth = 352;
    public static int m_iCampturedHeight = 288;

    public static int maxWidth = 352;
    public static int maxHeight = 352;

    public static int minWidth = 352;
    public static int minHeight = 352;

    public static final int MAX_AUDIO_GAIN = 10;
    public static final int DEFAULT_GAIN = -1;
    public static final int DEFAULT_VOLUME = 0;

    public static final int resolution_code_640 = 205;
    public static final int resolution_code_352_high = 207;
    public static final int resolution_code_352_low = 208;
    public static final int new_resolution_code_352 = 210;
    public static final int new_resolution_code_640 = 211;


    public static int m_iDecodedFrameWidth;
    public static int m_iDecodedFrameHeight;


    /// Video Quality Notifying msg
    public static int VIDEO_QUALITY_LOW = 202;
    public static int VIDEO_QUALITY_HIGH = 204;
    public static int VIDEO_SHOULD_STOP = 203;

    public static String IS_NEW_ACTIVITY_SESSION = "isnewactivitysession";
    public static final String IS_GAIN_ENABLE = "is_gain_enable";
    public static String FULL_NAME = "full_name";
    public static String FRIEND_UT_ID = "friend_ut_id";
    public static String CALLING_MODE = "calling_mode";
    public static String GENERATED_CALL_ID = "generated_call_id";

    /**
     * <blockquote> <b>MESSAGE_RESEND_MAP</b> = this is the resending packet
     * Storage. After Send a Packet we store it into this storage against
     * <b>CallID( every call has a unique id)</b>. Resender Thread use this
     * Storage to resend packet if neccessary.
     */

    public static final int NUMBER_OF_MAX_GARBAGE = 5;
    public static int NUMBER_OF_RESEND = 5;
    public static final int REGISTER_RESEND_TIME = 30000;
    public static final int NO_ANSWER_TIME_CALLER_SIDE = 36;
    public static final int NO_ANSWER_TIME_CALLEE_SIDE = 35;

    public static final String PREF_VOICE_SERVER = "voiceServer";
    public static final String PREF_VOICE_REGISTER_PORT = "registerPort";
    public static final String PREF_VOICE_BINDING_PORT = "bindingPort";
    public static final String PREF_FRIEND_ID = "friendIdentity";
    public static final String PREF_USER_ID = "userIdentity";

    public static boolean IS_GCM_PUSH_RECEIVE_FOR_CALL = false;
    public static boolean IS_DEVICE_LOCKED = false;

    public static boolean VIDEO_DATA_RECEIVED = false;

    public static String USER_IDENTITY = "";
    public static String FRIEND_IDENTITY = "";
    public static boolean isIncommingScreenStarted = false;

    public static final int CALL_TYPE_DIAL = 1;
    public static final int CALL_TYPE_RECEIVE = 2;
    public static final int CALL_TYPE_MISS = 3;

    public static final int VOICECALL = 1;
    public static final int VIDEOCALL = 2;

    public static final int CALLING_MODE_VOICE = 1;
    public static final int CALLING_MODE_VIDEO = 2;

    public static int CONCUR_CALL_TYPE = 1;

    public static final int P2P_ENABLE = 2;
    public static int CameraOrientation = 0;
//    public static boolean RENDER_ON_SURFACEVIEW = false;

    // Video call
    public class VIDEO_CALL {
        public static final byte ADDITIONAL_PACKET_TYPE_FOR_VIDEO_END = 1;
        public final static byte VIDEO_MEDIA = 39;
        public static final byte BINDING_PORT = 40;
        public static final byte BINDING_PORT_CONFIRMATION = 41;
        public static final byte START = 42;
        public static final byte START_CONFIRMATION = 43;
        public static final byte KEEPALIVE = 44;
        public static final byte END = 45;
        public static final byte END_CONFIRMATION = 46;
    }
    // public static boolean ISVALIDCALL = true;

    public class CALL_STATE {
        public final static byte IDLE = 0; // for client side only
        public final static byte DIALING = 1; // for client side only
        public final static byte INCOMING = 2; // for client side only
        public final static byte KEEPALIVE = 4;
        public final static byte CALLING = 5;
        public final static byte RINGING = 6;
        public final static byte IN_CALL = 7;
        public final static byte ANSWER = 8;
        public final static byte BUSY = 9;
        public final static byte CANCELED = 10;
        public final static byte CONNECTED = 11;
        public final static byte DISCONNECTED = 12;
        public final static byte BYE = 13;
        public final static byte NO_ANSWER = 15;
        public final static byte IN_CALL_CONFIRMATION = 18;
        public final static byte VIDEO_MEDIA = 19;
    }

    // public static final int VOICE_AUTO_CONNECTION = 24;
    // public static final int VOICE_AUTO_CONNECTION_CONFIRMATION = 25;
    public static final int VOICE_REGISTER_PUSH = 20;
    public static final int VOICE_REGISTER_PUSH_CONFIRMATION = 21;

    public static final int VOICE_CALL_HOLD = 22;
    public static final int VOICE_CALL_HOLD_CONFIRMATION = 23;
    public static final int VOICE_CALL_UNHOLD = 24;
    public static final int VOICE_UNHOLD_CONFIRMATION = 25;
    public static final int VOICE_BUSY_MESSAGE = 26;
    public static final int VOICE_BUSY_MESSAGE_CONFIRMATION = 27;

    public static String OUTGOING_CALL_ID = "";

    public static final int FAILED_TO_ESTABLISHED_CALL = 100;// for client side
    // only

    /**
     * @serialField NO_ANSWER_AFTER_30_SEC == this is use , when callee does
     * not receive call within 30 secenods. Do not use this for
     * another reason.
     */
    public static final int NO_ANSWER_AFTER_30_SEC = 101;// for client side only
    public static final int NO_ANSWER_OF_TRANSFER_CALL = 102;// for client side
    public static final int CALL_RECEIVE_WITH_VIDEO = 103;
    // only

    public static long CALL_RECEIVE_TIME = 0;// for client side only

    // Call Transfer Singal
    public static final int VOICE_TRANSFER = 28;
    public static final int VOICE_TRANSFER_CONFIRMATION = 29;
    public static final int VOICE_TRANSFER_BUSY = 30;
    public static final int VOICE_TRANSFER_BUSY_CONFIRMATION = 31;
    public static final int VOICE_TRANSFER_SUCCESS = 32;
    public static final int VOICE_TRANSFER_SUCCESS_CONFIRMATION = 33;
    public static final int VOICE_TRANSFER_CONNECTED = 34;
    public static final int VOICE_TRANSFER_CONNECTED_CONFIRMATION = 35;
    public static final int VOICE_TRANSFER_UNHOLD = 36;
    public static final int VOICE_TRANSFER_UNHOLD_CONFIRMATION = 37;


    public static boolean getSamsung() {
        String manufacturer = Build.MANUFACTURER.toUpperCase();
        String model = Build.MODEL.toUpperCase();
//        Constants.debugLog(VoiceCallEventHandler.TAG_MODE, " Build.MANUFACTURER== " + manufacturer + " MODEL== " + model);
        return manufacturer.contains("SAMSUNG") || model.contains("SAMSUNG") || model.contains("SPH-") || model.contains("SGH-") || model.contains("GT-");
    }

    public static int getApiVersion() {
        return Integer.parseInt(Build.VERSION.SDK);
    }

    public static Map<Integer, Integer> getMissCallCounterMap() {
        if (missCallCounterMap == null) {
            missCallCounterMap = new HashMap();
        }

        return missCallCounterMap;
    }

    public static String CALL_SCREEN_OPEN_FROM_PUSH = "CALL_SCREEN_OPEN_FROM_PUSH";
    public static String INCOMING_CALL_RECEIVE_FROM_SDK = "INCOMING_CALL_RECEIVE_FROM_SDK";
    public static String ALREADY_PRESSED_ANSWER_BUTTON = "ALREADY_PRESSED_ANSWER_BUTTON";


    public static void toastShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

public int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static String readRawTextFile(Context context, int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while ((line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString().trim();
    }

}
