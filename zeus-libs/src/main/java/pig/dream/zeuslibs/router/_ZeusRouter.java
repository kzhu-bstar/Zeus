package pig.dream.zeuslibs.router;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * @author zhukun on 2017/5/15.
 */

final class _ZeusRouter {
    private static final String ZeusTAG = "ZeusRouter";
    private volatile static boolean hasInit = false;

    private static Context appContext;
    private final static String ROUTE_ROOT_PAKCAGE = "pig.dream.zeuslibs.routes";

    private _ZeusRouter() {
    }

    private static final class SingletonHolder {
        private static _ZeusRouter instance = new _ZeusRouter();
    }

    protected static synchronized boolean init(Application application) {
        appContext = application;
        parseClass();
        hasInit = true;

        // It's not a good idea.
        // if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        //     application.registerActivityLifecycleCallbacks(new AutowiredLifecycleCallback());
        // }
        return true;
    }


    public static _ZeusRouter getInstance() {
        if (!hasInit) {
            throw new RuntimeException("Please ZeusRouter Init First!");
        }
        return SingletonHolder.instance;
    }


    public RouteRequest request(String routePath) {
        return RouteRequest.create().routePath(routePath);
    }

    void navigation(Object target, RouteRequest routeRequest) {
        RouteMeta routeMeta = Warehouse.routes.get(routeRequest.getRoutePath());
        if (routeMeta == null) {
            Log.e(ZeusTAG, String.format("u route path is not found, please check it (path:[%s])!", routeRequest.getRoutePath()));
            return;
        }

        Intent intent = routeRequest.getIntent();
        intent.setClassName(appContext, routeMeta.className);

        if (target instanceof Activity) {
            ((Activity) target).startActivity(intent);
        } else if (target instanceof Fragment) {
            ((Fragment) target).startActivity(intent);
        } else if (target instanceof android.app.Fragment) {
            ((android.app.Fragment) target).startActivity(intent);
        }
    }

    private static void parseClass() {
        try {
            String className = "pig.dream.zeuslibs.routes.Router$$ModuleName$$App";
            ((IRouteRoot) (Class.forName(className).getConstructor().newInstance())).loadInto(Warehouse.routes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
