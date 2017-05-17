package pig.dream.zeuslibs.layout;

import android.support.annotation.LayoutRes;

/**
 * 基本页面 需要实现此接口（包括Activity Fragment）
 *
 * Created by zhukun on 2017/3/11.
 */

public interface IBaseLayout {


    public @LayoutRes int getContentView();

    public void init();
}
