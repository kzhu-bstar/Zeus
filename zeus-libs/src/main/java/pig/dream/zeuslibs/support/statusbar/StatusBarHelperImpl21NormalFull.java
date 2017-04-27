package pig.dream.zeuslibs.support.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class StatusBarHelperImpl21NormalFull extends StatusBarHelperImpl21Normal {

    public StatusBarHelperImpl21NormalFull(Activity activity) {
        super(activity);
    }

    @Override
    protected void setColor(int color) {
        // 设置全屏
        mActivity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setActivityRootLayoutFitSystemWindowsInternal();
        // 设置颜色
        super.setColor(color);
    }

}
