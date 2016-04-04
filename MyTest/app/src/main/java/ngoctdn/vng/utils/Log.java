package ngoctdn.vng.utils;

/**
 * Created by ngoctdn on 3/22/2016.
 */
public class Log {

    public static void d(String tag, String msg) {
        if (AppSettings.ENABLE_LOG) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (AppSettings.ENABLE_LOG) {
            android.util.Log.e(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (AppSettings.ENABLE_LOG) {
            android.util.Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (AppSettings.ENABLE_LOG) {
            android.util.Log.w(tag, msg);
        }
    }
}
