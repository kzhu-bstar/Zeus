package pig.dream.zeuslibs.support.statusbar;

import android.app.Activity;
import android.graphics.drawable.Drawable;

class StatusBarHelperImplBase extends StatusBarHelperImpl {

    public StatusBarHelperImplBase(Activity activity) {
        super(activity);
    }

    @Override
    protected void setColor(int color) {
        // do noting
    }

    @Override
    protected void setDrawable(Drawable drawable) {
        // do noting
    }

    @Override
    protected void destroy() {
        // do noting
    }

}
