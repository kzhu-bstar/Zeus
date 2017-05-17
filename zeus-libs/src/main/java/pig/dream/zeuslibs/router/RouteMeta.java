package pig.dream.zeuslibs.router;

/**
 * @author zhukun on 2017/5/15.
 */

public class RouteMeta {

    String className;

    public static RouteMeta build(String className) {
        RouteMeta routeMeta = new RouteMeta();
        routeMeta.className = className;
        return routeMeta;
    }
}
