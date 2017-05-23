package pig.dream.zeuslibs;

/**
 * 用于打印方法执行的时间
 *
 * @author zhukun on 2017/3/15.
 */

public class Timer {

    public static final String TAG = "Timer";

    private static long startTime = 0;

    public static void init() {
        startTime = System.currentTimeMillis();
    }

    public static void out(String msg) {
        if (!L.DBG) {
            return ;
        }
        long time = System.currentTimeMillis();
        L.i(TAG, msg + (time - startTime));
        startTime = time;
    }
}
