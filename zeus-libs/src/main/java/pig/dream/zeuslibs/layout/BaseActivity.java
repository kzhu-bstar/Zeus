package pig.dream.zeuslibs.layout;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;
import pig.dream.zeuslibs.L;
import pig.dream.zeuslibs.R;
import pig.dream.zeuslibs.ViewFinder;
import pig.dream.zeuslibs.inject.ZeusInjection;
import pig.dream.zeuslibs.layout.mvp.MvpPresenter;
import pig.dream.zeuslibs.layout.mvp.MvpView;
import pig.dream.zeuslibs.support.statusbar.StatusBarHelper;

/**
 * 基本Activity类
 *
 * @author zhukun 2017/3/11.
 * @version 1.0
 */

public abstract class BaseActivity<P extends MvpPresenter> extends AppCompatActivity implements IBaseLayout, SwipeBackActivityBase, MvpView,
        StatusBarHelper.OnConsumeInsetsCallback, NoDoubleClickDelegate.NoDoubleClickListener {

    private static final String TAG = "UIName";

    private SwipeBackActivityHelper mHelper;
    protected StatusBarHelper statusBarHelper;
    protected P presenter;
    private BaseUI baseUIDelegate;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseUIDelegate = new BaseUIImpl();
        baseUIDelegate.onCreate(this);
        if (parseIntentAndCheck(savedInstanceState)) {
            finish();
            return;
        }
        baseUIDelegate.initFunctionFlag(initFunctionFlag());
        initSwipeBack();
        setContentView(getContentView());
        ZeusInjection.bind(this);
        init();
        L.iByTag(TAG, "==========>>>>Activity onCreate() %s flag %d", getClass().getSimpleName());
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        initToolBar();
        initPresenter();
        initStatusBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        baseUIDelegate.finish(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        L.iByTag(TAG, "==========>>>>Activity onDestroy() %s", getClass().getSimpleName());

        if (this.presenter != null) {
            this.presenter.detachView();
        }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ZeusInjection.saveInstanceState(this, outState);
    }

    public View.OnClickListener onClickListener() {
        return baseUIDelegate;
    }

    public int initFunctionFlag() {
        return BaseUI.FLAG_ALL_DISABLE;
    }

    /**
     * 设置界面的居中的标题
     * @param title 标题
     */
    public void setTitle(String title) {
        TextView textView = ViewFinder.find(this, R.id.zeus_tv_title);
        textView.setText(title);
    }

    /**
     * 设置界面的居中的标题
     * @param id 标题的id
     */
    public void setTitle(@StringRes int id) {
        TextView textView = ViewFinder.find(this, R.id.zeus_tv_title);
        textView.setText(id);
    }

    public void autoSetOnClickListener(@IdRes int id) {
        autoSetOnClickListener(ViewFinder.find(this, id));
    }

    public void autoSetOnClickListener(View view) {
        view.setOnClickListener(baseUIDelegate);
    }

    private void initToolBar() {
        if (!baseUIDelegate.enableFunctionFlag(BaseUI.FLAG_TOOLBAR_ENABLE)) {
            return ;
        }

        Toolbar toolbar = ViewFinder.find(this, R.id.zeus_toolbar);
        if (toolbar == null) {
            throw new NullPointerException("please add toolbar in layout");
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initSwipeBack() {
        if (!baseUIDelegate.enableFunctionFlag(BaseUI.FLAG_SWIPEBACK_ENABLE)) {
            return ;
        }

        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }

    private void initPresenter() {
        if (!baseUIDelegate.enableFunctionFlag(BaseUI.FLAG_PRESENTER_ENABLE)) {
            return ;
        }

        this.presenter = (P) baseUIDelegate.getPresenterFromParameterizedTypes(this);
        this.presenter.setContext(this);
        this.presenter.attachView(this);
    }

    private void initStatusBar() {
        if (!baseUIDelegate.enableFunctionFlag(BaseUI.FLAG_STATUSBAR_ENABLE)) {
            return ;
        }
        this.statusBarHelper = new StatusBarHelper(this, StatusBarHelper.LEVEL_19_TRANSLUCENT, StatusBarHelper.LEVEL_21_NORMAL_FULL);
        statusBarHelper.setActivityRootLayoutFitSystemWindows(true);
        statusBarHelper.setConsumeInsets(this);
        statusBarHelper.setColor(android.R.attr.colorPrimary);
    }

    public boolean parseIntentAndCheck(@Nullable Bundle savedInstanceState) {
        ZeusInjection.parseIntent(this, savedInstanceState, getIntent());
        return false;
    }

    /** SwipeBackActivityBase --- start implements **/
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mHelper != null) {
            mHelper.onPostCreate();
        }
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        if (mHelper == null) {
            return null;
        }
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        if (mHelper == null) {
            return;
        }
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        if (mHelper == null) {
            return;
        }
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
    /** SwipeBackActivityBase --- end implements **/

    /** StatusBarHelper.OnConsumeInsetsCallback  --- start implements */
    @Override
    public void onInsetsChanged(Rect insets) {}

    /** StatusBarHelper.OnConsumeInsetsCallback --- end implements */

    /** NoDoubleClickListener --- start implements **/
    @Override
    public void onNoDoubleClick(View view) {}

    /** NoDoubleClickListener --- start implements **/

}
