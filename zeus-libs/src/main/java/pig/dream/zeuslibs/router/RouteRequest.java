package pig.dream.zeuslibs.router;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhukun on 2017/5/16.
 */

public final class RouteRequest {

    private String routePath;
    private Map<String, Object> values = new HashMap<>();

    private Intent intent = new Intent();
    private RouteRequest(){}

    public static RouteRequest create() {
        return new RouteRequest();
    }

    public RouteRequest routePath(String routePath) {
        this.routePath = routePath;
        return this;
    }

    public RouteRequest withInt(String key, int value) {
        intent.putExtra(key, value);
        return this;
    }

    public RouteRequest withBoolean(String key, boolean value) {
        intent.putExtra(key, value);
        return this;
    }

    public RouteRequest withString(String key, String value) {
        intent.putExtra(key, value);
        return this;
    }

    public RouteRequest withSerializable(String key, Serializable value) {
        intent.putExtra(key, value);
        return this;
    }

    public RouteRequest navigation(Fragment fragment) {
        _ZeusRouter.getInstance().navigation(fragment, this);
        return this;
    }

    public RouteRequest navigation(android.app.Fragment fragment) {
        _ZeusRouter.getInstance().navigation(fragment, this);
        return this;
    }

    public RouteRequest navigation(Activity activity) {
        _ZeusRouter.getInstance().navigation(activity, this);
        return this;
    }

    String getRoutePath() {
        return routePath;
    }

    Map<String, Object> getValues() {
        return values;
    }

    Intent getIntent() {
        return intent;
    }
}
