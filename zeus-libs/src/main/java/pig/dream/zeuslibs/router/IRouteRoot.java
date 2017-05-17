package pig.dream.zeuslibs.router;

import java.util.Map;

/**
 * @author zhukun on 2017/5/15.
 */

public interface IRouteRoot {

    void loadInto(Map<String, RouteMeta> map);
}
