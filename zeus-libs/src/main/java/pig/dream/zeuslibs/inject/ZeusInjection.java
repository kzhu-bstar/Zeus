package pig.dream.zeuslibs.inject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhukun on 2017/4/15.
 */

public class ZeusInjection {

    private static final Map<String, Core> FINDER_MAP = new HashMap<>();

    @NonNull
    public static void bind(@NonNull Activity target) {
        View sourceView = target.getWindow().getDecorView();
        createBinding(target, sourceView);
    }

    public static void parseIntent(final Activity target, Bundle savedInstanceState, Intent intent) {
        try {
            getCore(target).parseIntent(target, savedInstanceState, intent);
        } catch (Exception e) {
            throw new RuntimeException("Unable to parseIntent for ", e);
        }
    }

    public static void saveInstanceState(final Activity host, Bundle outState) {
        try {
            getCore(host).saveInstanceState(host, outState);
        } catch (Exception e) {
            throw new RuntimeException("Unable to saveInstanceState for ", e);
        }
    }

    @NonNull
    public static void bind(@NonNull View target) {
        createBinding(target, target);
    }

    public static void createBinding(Object host, View source) {
        String className = host.getClass().getName();
        try {
            Core core = FINDER_MAP.get(className);
            if (core == null) {
                Class<?> finderClass = Class.forName(className + "$$Core");
                core = (Core) finderClass.newInstance();
                FINDER_MAP.put(className, core);
            }
            core.inject(host, source);
        } catch (Exception e) {
            throw new RuntimeException("Unable to inject for " + className, e);
        }
    }

    private static Core getCore(Object object) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = object.getClass().getName();
        Core core = FINDER_MAP.get(className);
        if (core == null) {
            Class<?> finderClass = Class.forName(className + "$$Core");
            core = (Core) finderClass.newInstance();
            FINDER_MAP.put(className, core);
        }
        return core;
    }
}
