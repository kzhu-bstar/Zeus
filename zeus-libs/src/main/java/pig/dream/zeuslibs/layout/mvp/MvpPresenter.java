package pig.dream.zeuslibs.layout.mvp;


import android.content.Context;

import java.lang.ref.SoftReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by pig on 2016/6/18.
 */
public class MvpPresenter<T extends MvpView> implements Presenter<T> {

    private SoftReference<Context> softRef;
    protected T mvpView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void attachView(T mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void detachView() {
        mvpView = null;
        mCompositeDisposable.clear();
    }

    public boolean isViewAttached() {
        return mvpView != null;
    }

    public T getMvpView() {
        return mvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public void setContext(Context context) {
        if (softRef == null) {
            softRef = new SoftReference<Context>(context);
        }
    }

    public Context getContext() {
        if (softRef == null) {
            throw new IllegalArgumentException("Please first setContext ");
        }
        return softRef.get();
    }

    public void addLifeCycle(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}