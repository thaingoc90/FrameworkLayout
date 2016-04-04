package ngoctdn.vng.app;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import ngoctdn.vng.utils.ConfigAQueryUtils;

/**
 * Created by CPU11303-local on 3/19/2016.
 */
public class MainApplication extends Application {

    private static Context context;
    private static int screenWidth;
    private static int screenHeight;

    public static Context getAppContext() {
        return MainApplication.context;
    }

    public static int getScreenWidth() {
        return MainApplication.screenWidth;
    }

    public static int getScreenHeight() {
        return MainApplication.screenHeight;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MainApplication.context = getApplicationContext();
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        ConfigAQueryUtils.initAQuery(this);
    }
}
