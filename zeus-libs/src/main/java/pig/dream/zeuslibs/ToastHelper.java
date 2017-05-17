package pig.dream.zeuslibs;

import android.content.Context;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 特殊的Toast 解决重复显示问题
 *
 * Created by kzhu on 2016/10/28.
 */
public class ToastHelper {
    /** 之前显示的内容 */
    private static String oldMsg = null ;
    /** 第一次时间 */
    private static long oneTime = 0 ;
    /** 第二次时间 */
    private static long twoTime = 0 ;

    private final static Map<String, Toast> toasts = new HashMap<>();

    /**
     * 显示Toast
     * @param context 上下文
     * @param message 显示的内容
     */
    public static void showToast(Context context, String message) {
        if(context==null) {
            return;
        }
        // fix: message is null
        if (message == null) {
            message = "";
        }
        String simpleName = context.getClass().getSimpleName();
        Toast toast = toasts.get(simpleName);
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.show();
            toasts.put(simpleName, toast);
            oneTime = System.currentTimeMillis() ;
        } else {
            twoTime = System.currentTimeMillis() ;
            if(message.equals(oldMsg)){
                if(twoTime - oneTime > Toast.LENGTH_SHORT){
                    toast.show() ;
                }
            }else{
                oldMsg = message ;
                toast.setText(message) ;
                toast.show() ;
            }
            oneTime = twoTime ;
        }
    }

    public static void close(Context context) {
        if(context==null) {
            return;
        }
        String simpleName = context.getClass().getSimpleName();
        Toast toast = toasts.remove(simpleName);
        if (toast != null) {
            toast.cancel();
        }
    }
}
