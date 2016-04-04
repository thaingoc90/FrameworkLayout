package ngoctdn.vng.utils;

import android.app.Application;
import android.content.Context;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.util.AQUtility;

import java.io.File;

/**
 * Created by ngoctdn on 3/22/2016.
 */
public class ConfigAQueryUtils {

    private static final int NUMBER_OF_NETWORK_THREAD = 2;
    private static final int MAX_ITEMS_OF_SMALL_CACHE = 100;
    private static final int MAX_ITEMS_OF_BIG_CACHE = 100;
    private static final int SMALL_CACHE_PIXEL_LIMIT = 128 * 128;
    private static final int BIG_CACHE_PIXEL_LIMIT = 1280 * 1280;
    private static final int MAX_PIXEL_LIMIT = 2000000;


    public static void initAQuery(Context ctx) {
        AQUtility.setCacheDir(new File(Utils.getCacheImageFolder()));
        AQUtility.setContext((Application) ctx);
        AQUtility.cleanCacheAsync(ctx);
        AjaxCallback.setNetworkLimit(NUMBER_OF_NETWORK_THREAD);
        BitmapAjaxCallback.setPixelLimit(BIG_CACHE_PIXEL_LIMIT);
        BitmapAjaxCallback.setSmallPixel(SMALL_CACHE_PIXEL_LIMIT);
        BitmapAjaxCallback.setCacheLimit(MAX_ITEMS_OF_BIG_CACHE);
        BitmapAjaxCallback.setMaxPixelLimit(MAX_PIXEL_LIMIT);
        BitmapAjaxCallback.setIconCacheLimit(MAX_ITEMS_OF_SMALL_CACHE);

    }
}
