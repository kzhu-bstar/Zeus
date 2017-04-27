package pig.dream.zeuslibs;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 屏幕尺寸大小帮助类
 *  依赖ResourcesHelper 获取屏幕尺寸
 *  依赖SPHelper SharedPreferences保存到文件中
 *
 * Created by zhukun on 2017/3/5.
 */

public class ScreenSizeHelper {

    public int screenWidth;
    public int screenHeight;

    private ScreenSizeHelper() {}

    private static class InstanceHolder {
        public static  ScreenSizeHelper instance = new ScreenSizeHelper();
    }

    public ScreenSizeHelper getInstance() {
        return InstanceHolder.instance;
    }

    public void init(Context context) {
        DisplayMetrics dm = ResourcesHelper.getPhoneSize(context);
        SPHelper.put(context, ScreenSizeSP.FILE_NAME, ScreenSizeSP.WIDTH_KEY, dm.widthPixels);
        SPHelper.put(context, ScreenSizeSP.FILE_NAME, ScreenSizeSP.HEIGHT_KEY, dm.heightPixels);
    }

    public int getWidth(Context context) {
        if (screenWidth == 0) {
            screenWidth = (int) SPHelper.get(context, ScreenSizeSP.FILE_NAME, ScreenSizeSP.WIDTH_KEY, 0);
        }
        return screenWidth;
    }

    public int getHeight(Context context) {
        if (screenHeight == 0) {
            screenHeight = (int) SPHelper.get(context, ScreenSizeSP.FILE_NAME, ScreenSizeSP.HEIGHT_KEY, 0);
        }
        return screenHeight;
    }

    public static class ScreenSizeSP {
        public static final String FILE_NAME = "screen_size";

        public static final String WIDTH_KEY = "width_key";
        public static final String HEIGHT_KEY = "height_key";

    }
}
