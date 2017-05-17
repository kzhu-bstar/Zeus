package pig.dream.zeuslibs.layout.application;

import android.app.Application;

/**
 * Created by zhukun on 2017/5/14.
 */

public abstract class BaseApplication extends Application {

    private BaseApplicationDelegate baseApplicationDelegate;

    @Override
    public final void onCreate() {
        super.onCreate();
        baseApplicationDelegate = new BaseApplicationDelegate();
        initialize();

        baseApplicationDelegate.onCreate();
    }

    public abstract void initialize();

    public void addSonApplication(SonApplication sonApplication) {
        baseApplicationDelegate.addSonApplication(sonApplication);
    }

    public void removeSonApplication(SonApplication sonApplication) {
        baseApplicationDelegate.removeSonApplication(sonApplication);
    }
}
