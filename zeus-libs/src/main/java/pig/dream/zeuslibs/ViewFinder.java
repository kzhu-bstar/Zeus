package pig.dream.zeuslibs;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.view.View;

/**
 * View find helper
 *
 * @author zhukun on 2017/3/5.
 */

public class ViewFinder {

    @SuppressWarnings("unchecked")
    public static <T extends View> T find(Activity activity, @IdRes int id) {
        return (T) activity.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    public static <T> T find(View view, @IdRes int id) {
        return (T) view.findViewById(id);
    }
}
