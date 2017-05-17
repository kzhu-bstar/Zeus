package pig.dream.zeuslibs.layout;

import android.view.View;

/**
 * 防止连续点击代理类
 *
 * Created by zhukun on 2017/3/16.
 */

public class NoDoubleClickDelegate {
    public static final long DELAY_TIME = 500;

    private long time;
    private NoDoubleClickListener noDoubleClickListener;

    public NoDoubleClickDelegate(NoDoubleClickListener noDoubleClickListener) {
        this.noDoubleClickListener = noDoubleClickListener;
    }

    public void onClickDelegate(View view) {
        if (noDoubleClickListener == null) {
            return ;
        }
        long clickTime = System.currentTimeMillis();
        if (clickTime - time > DELAY_TIME) {
            noDoubleClickListener.onNoDoubleClick(view);
        }
        time = clickTime;
    }

    public interface NoDoubleClickListener {
        public void onNoDoubleClick(View view);
    }
}
