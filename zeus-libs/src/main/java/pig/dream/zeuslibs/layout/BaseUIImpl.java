package pig.dream.zeuslibs.layout;

import android.app.Activity;
import android.content.res.TypedArray;
import android.view.View;

import java.lang.reflect.Type;

import pig.dream.zeuslibs.Timer;
import pig.dream.zeuslibs.utils.ReflectionUtils;

/**
 * 基础UI页面代理接口实现类
 *
 * Created by zhukun on 2017/3/23.
 */

public class BaseUIImpl implements BaseUI {

    private int activityCloseEnterAnimation;
    private int activityCloseExitAnimation;
    private NoDoubleClickDelegate noDoubleClickDelegate;
    private int functionFlag = 0;

    @SuppressWarnings("ResourceType")
    @Override
    public void onCreate(Activity activity) {
        TypedArray activityStyle = activity.getTheme().obtainStyledAttributes(new int[] { android.R.attr.windowAnimationStyle });
        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
        activityStyle = activity.getTheme().obtainStyledAttributes(windowAnimationStyleResId,
                new int[] { android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation });
        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
        activityStyle.recycle();

        if (activity instanceof NoDoubleClickDelegate.NoDoubleClickListener) {
            noDoubleClickDelegate = new NoDoubleClickDelegate((NoDoubleClickDelegate.NoDoubleClickListener) activity);
        }
    }

    @Override
    public void finish(Activity activity) {
        activity.overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }

    @Override
    public void initFunctionFlag(int flag) {
        this.functionFlag = flag;
    }

    @Override
    public void onClick(View v) {
        if (noDoubleClickDelegate != null) {
            noDoubleClickDelegate.onClickDelegate(v);
        }
    }

    @Override
    public boolean enableFunctionFlag(int flag) {
        return (this.functionFlag & flag) == flag;
    }

    @Override
    public Object getPresenterFromParameterizedTypes(Object object) {
        Object result = null;
        try {
            Timer.init();
            Type[] parameterizedTypes = ReflectionUtils.getParameterizedTypes(object);
            if (parameterizedTypes != null && parameterizedTypes.length > 0) {
                result = ReflectionUtils.newInstance(parameterizedTypes[0]);
            }
            Timer.out("Presenter newInstance time ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
