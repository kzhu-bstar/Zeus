package pig.dream.zeuslibs;

import android.text.TextUtils;
import android.util.Log;

import pig.dream.zeuslibs.support.logger.Logger;


/**
 * 日记记录类
 *
 * Created by zhukun on 2017/3/5.
 */

public class L {

    public static boolean DBG = false;
    private static String TAG = "BaseLibTag";

    public static void setTagAndDebug(String tag, boolean debug) {
        if (!TextUtils.isEmpty(tag)) {
            TAG = tag;
        }

        DBG = debug;
        Logger.init(TAG);
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void i(String tag, String format, Object... args) {
        if (!DBG) {
            return ;
        }
        Log.i(tag, TextUtils.isEmpty(format) ? "Message is Empty" : String.format(format, args));
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void w(String tag, String format, Object... args) {
        if (!DBG) {
            return ;
        }

        Log.w(tag, TextUtils.isEmpty(format) ? "Message is Empty" : String.format(format, args));
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(String tag, String format, Object... args) {
        if (!DBG) {
            return ;
        }

        Log.e(tag, TextUtils.isEmpty(format) ? "Message is Empty" : String.format(format, args));
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void d(String tag, String format, Object... args) {
        if (!DBG) {
            return ;
        }

        Log.d(tag, TextUtils.isEmpty(format) ? "Message is Empty" : String.format(format, args));
    }

    public static void json(String msg) {
        if (!DBG) {
            return ;
        }

        if (!TextUtils.isEmpty(msg)) {
            Logger.json(msg);
        }
    }

    public static <T> void json(T t) {
        json(JsonHelper.toJson(t));
    }
}
