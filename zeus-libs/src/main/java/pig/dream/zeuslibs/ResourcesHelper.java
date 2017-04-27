package pig.dream.zeuslibs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 资源获取帮助类（颜色、图片、尺寸）
 *
 * Created by zhukun on 2017/3/5.
 */

public class ResourcesHelper {

    public static int getColor(Context context, @ColorRes int id) {
        return ResourcesCompat.getColor(context.getResources(), id, null);
    }

    public static Drawable getDrawable(Context context, @DrawableRes int id) {
        return ResourcesCompat.getDrawable(context.getResources(), id, null);
    }

    public static void setTextViewColor(Context context, TextView targetView, @ColorRes int id) {
        targetView.setTextColor(getColor(context, id));
    }

    /**
     * 获取手机DisplayMetrics参数（宽、高、像素密度等）
     *
     * @param context 上下文
     * @return 获取到的手机像素数据对象
     */
    public static DisplayMetrics getPhoneSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        return metric;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
