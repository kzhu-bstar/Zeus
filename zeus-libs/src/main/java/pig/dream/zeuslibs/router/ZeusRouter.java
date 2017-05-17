package pig.dream.zeuslibs.router;

import android.app.Application;

/**
 * @author zhukun on 2017/5/15.
 */

public class ZeusRouter {
    private final static String TAG = "ZeusRouter";
    private static boolean DBG = true;


    private volatile static boolean hasInit = false;
    private ZeusRouter() {
    }

    private static final class SingletonHolder {
        private static ZeusRouter instance = new ZeusRouter();
    }

    /**
     * Init, it must be call before used router.
     */
    public static void init(Application application) {
        if (!hasInit) {
            hasInit = _ZeusRouter.init(application);
        }
    }

    public static ZeusRouter getInstance() {
        if (!hasInit) {
            throw new RuntimeException("Please ZeusRouter Init First.");
        }

        return SingletonHolder.instance;
    }

    public static RouteRequest request(String routePath) {
        validateInit();
        return _ZeusRouter.getInstance().request(routePath);
    }

    private static void validateInit() {
        if (!hasInit) {
            throw new RuntimeException("Please ZeusRouter Init First.");
        }
    }
}
