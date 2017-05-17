package pig.dream.zeuslibs.layout.mvp;

/**
 *
 * Created by pig on 2016/6/18.
 */
public interface Presenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView();
}