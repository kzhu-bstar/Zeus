package pig.dream.zeuslibs.layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pig.dream.zeuslibs.layout.mvp.MvpView;
import pig.dream.zeuslibs.layout.mvp.Presenter;

/**
 * 基本Fragment类
 *
 * Created by zhukun on 2017/3/15.
 */

public abstract class BaseFragment<P extends Presenter> extends Fragment implements IBaseLayout, MvpView, View.OnClickListener, NoDoubleClickDelegate.NoDoubleClickListener  {

    private SparseArray<View> viewArray;
    private P presenter;
    protected NoDoubleClickDelegate noDoubleClickDelegate;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initPresenter();
        return inflater.inflate(getContentView(), container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewArray = new SparseArray<>();
        noDoubleClickDelegate = new NoDoubleClickDelegate(this);
        init();
    }

    @Override
    public void onDestroyView() {
        if (this.presenter != null) {
            this.presenter.detachView();
            this.presenter = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        if (noDoubleClickDelegate != null) {
            noDoubleClickDelegate.onClickDelegate(v);
        }
    }

    @Override
    public void onNoDoubleClick(View view) {}

    private void initPresenter() {
//        this.presenter = getPresenter();
//        this.presenter.attachView(this);
    }
}
