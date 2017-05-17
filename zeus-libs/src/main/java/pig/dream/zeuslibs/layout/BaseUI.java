package pig.dream.zeuslibs.layout;

import android.app.Activity;
import android.view.View;

/**
 * 基础UI页面代理接口
 *
 * Created by zhukun on 2017/3/23.
 */

public interface BaseUI extends View.OnClickListener {
    int FLAG_ALL_DISABLE = 0x00000000;
    int FLAG_TOOLBAR_ENABLE = 0x00000001;
    int FLAG_SWIPEBACK_ENABLE = 0x00000002;
    int FLAG_PRESENTER_ENABLE = 0x00000004;
    int FLAG_STATUSBAR_ENABLE = 0x00000008;

    void onCreate(Activity activity);
    void finish(Activity activity);
    void initFunctionFlag(int flag);
    boolean enableFunctionFlag(int flag);
    Object getPresenterFromParameterizedTypes(Object object);
}
