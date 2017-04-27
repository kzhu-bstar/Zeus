package pig.dream.zeuslibs.support.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class StatusBarHelperImpl21Normal extends StatusBarHelperImplBase {

    public StatusBarHelperImpl21Normal(Activity activity) {
        super(activity);
    }

    @Override
    protected void setColor(int color) {
        mActivity.getWindow().setStatusBarColor(color);
        setActivityRootLayoutFitSystemWindowsInternal();
    }

    @Override
    protected void setDrawable(Drawable drawable) {
        // not support
    }

    @Override
    protected void destroy() {
        // don't need
    }
}
