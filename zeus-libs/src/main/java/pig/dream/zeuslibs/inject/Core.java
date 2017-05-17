package pig.dream.zeuslibs.inject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @author zhukun on 2017/4/15.
 */

public interface Core<T> {
    void inject(T host, View source);
    void parseIntent(T host, Bundle savedInstanceState, Intent intent);
    void saveInstanceState(T host, Bundle outState);
}
