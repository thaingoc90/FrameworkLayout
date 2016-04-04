package ngoctdn.vng.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

import java.io.File;

import ngoctdn.vng.activity.R;
import ngoctdn.vng.app.MainApplication;

/**
 * Created by CPU11303-local on 3/19/2016.
 */
public class Utils {

    public static final int SWIPE_MIN_DISTANCE = 120;
    public static final int SWIPE_MAX_OFF_PATH = 250;
    public static final String APP_ROOT_FOLDER = "FLayout";
    public static final String IMAGE_CACHE_FOLDER = "imagecache";
    private static ViewConfiguration mViewConfiguration;
    private static ImageOptions mImageOptions;

    public static ViewConfiguration getViewConfiguration() {
        if (mViewConfiguration == null) {
            mViewConfiguration = ViewConfiguration.get(MainApplication.getAppContext());
        }
        return mViewConfiguration;
    }

    public static String getCacheImageFolder() {
        return getAppFolder() + File.separator + IMAGE_CACHE_FOLDER;
    }

    public static String getAppFolder() {
        String folder = Environment.getExternalStorageDirectory().getAbsolutePath();
        return folder + File.separator + APP_ROOT_FOLDER;
    }

    public static void showToastLong(String msg) {
        Toast.makeText(MainApplication.getAppContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static void showToastShort(String msg) {
        Toast.makeText(MainApplication.getAppContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean isCompatible(int apiLevel) {
        return android.os.Build.VERSION.SDK_INT >= apiLevel;
    }

    public static ImageOptions getImageOptions() {
        if (mImageOptions == null) {
            Bitmap mPreImage = BitmapFactory.decodeResource
                    (MainApplication.getAppContext().getResources(), R.drawable.holder);
            mImageOptions = new ImageOptions();
            mImageOptions.fileCache = true;
            mImageOptions.memCache = true;
            mImageOptions.animation = AQuery.FADE_IN;
            mImageOptions.targetWidth = 200;
            mImageOptions.preset = mPreImage;
        }
        return  mImageOptions;
    }
}
