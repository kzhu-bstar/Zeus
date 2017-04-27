package pig.dream.zeuslibs.support.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.Window;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class StatusBarHelperImpl21View extends StatusBarHelperImpl19 {

    public StatusBarHelperImpl21View(Activity activity) {
        super(activity);
    }

    @Override
    protected void setDrawable(Drawable drawable) {
        Window window = mActivity.getWindow();
        // 设置全屏
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.TRANSPARENT);
        setupStatusBarView();
        setActivityRootLayoutFitSystemWindowsInternal();
        mStatusBarDrawable = drawable;
        mStatusBarView.setBackground(drawable);
    }

}
