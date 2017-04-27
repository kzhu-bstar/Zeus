package pig.dream.zeuslibs.support.rxbus;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import pig.dream.baselib.support.rxbus.annotation.Subscribe;

/**
 * RxBus
 * <p>
 * Created by zhukun on 2017/3/22.
 */

public class RxBus {
    private Subject mRxBusObserverable = PublishSubject.create().toSerialized();

    //存放订阅者信息
    protected Map<Object, CompositeDisposable> subscriptions = new HashMap<>();

    private RxBus() {
    }

    public static RxBus getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    public void post(Object object) {
        post(0, object);
    }

    /**
     * 发布事件
     *
     * @param code 值使用RxBus.getInstance().getTag(class,value)获取
     * @param obj  为需要被处理的事件
     */
    public void post(@NonNull int code, @NonNull Object obj) {
        if (mRxBusObserverable.hasObservers()) {
            mRxBusObserverable.onNext(new Msg(code, obj));
        }
    }

    /**
     * 订阅者注册
     *
     * @param subscriber 订阅者
     */
    public void register(final Object subscriber) {
        Flowable.just(subscriber)
                .filter(new Predicate<Object>() {

                    @Override
                    public boolean test(@NonNull Object o) throws Exception {
                        //判断订阅者没有在序列中
                        return subscriptions.get(subscriber) == null;
                    }
                })
                .flatMap(new Function<Object, Publisher<Method>>() {
                    @Override
                    public Publisher<Method> apply(@NonNull Object o) throws Exception {
                        return Flowable.fromArray(o.getClass().getDeclaredMethods());
                    }
                })
                .map(new Function<Method, Method>() {
                    @Override
                    public Method apply(@NonNull Method o) throws Exception {
                        o.setAccessible(true);
                        return o;
                    }
                })
                .filter(new Predicate<Method>() {
                    @Override
                    public boolean test(@NonNull Method method) throws Exception {
                        return method.isAnnotationPresent(Subscribe.class);
                    }
                })
                .subscribe(new Consumer<Method>() {
                    @Override
                    public void accept(@NonNull Method method) throws Exception {
                        addSubscription(method, subscriber);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    /**
     * 解除订阅者
     *
     * @param subscriber 订阅者
     */
    public void unRegister(final Object subscriber) {
        Flowable.just(subscriber)
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(@NonNull Object o) throws Exception {
                        return o != null;
                    }
                })
                .map(new Function<Object, CompositeDisposable>() {
                    @Override
                    public CompositeDisposable apply(@NonNull Object o) throws Exception {
                        return subscriptions.get(o);
                    }
                })
                .filter(new Predicate<CompositeDisposable>() {
                    @Override
                    public boolean test(@NonNull CompositeDisposable o) throws Exception {
                        return o != null;
                    }
                })
                .subscribeWith(new Subscriber<CompositeDisposable>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(CompositeDisposable compositeDisposable) {
                        compositeDisposable.dispose();
                        subscriptions.remove(subscriber);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void addSubscription(final Method m, final Object subscriber) {
        //获取方法内参数
        Class[] parameterType = m.getParameterTypes();
        //只获取第一个方法参数，否则默认为Object
        Class cla = Object.class;
        if (parameterType.length > 1) {
            cla = parameterType[0];
        }
        //获取注解
        Subscribe sub = m.getAnnotation(Subscribe.class);
        //订阅事件
        Disposable disposable = tObservable(sub.tag(), cla)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        try {
                            m.invoke(subscriber, o);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        System.out.println("this object is not invoke");
                    }
                });
        putSubscriptionsData(subscriber, disposable);
    }

    /**
     * 订阅事件
     *
     * @return
     */
    private <T> Observable tObservable(final int code, final Class<T> eventType) {
        return mRxBusObserverable.ofType(Msg.class)//判断接收事件类型
                .filter(new Predicate<Msg>() {
                    @Override
                    public boolean test(Msg msg) throws Exception {
                        return msg.code == code;
                    }
                })
                .map(new Function<Msg, Object>() {
                    @Override
                    public Object apply(Msg msg) throws Exception {
                        return msg.object;
                    }
                })
                .cast(eventType)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 添加订阅者到map空间来unRegister
     *
     * @param subscriber 订阅者
     * @param disposable 订阅者 Subscription
     */
    protected void putSubscriptionsData(Object subscriber, Disposable disposable) {
        CompositeDisposable subs = subscriptions.get(subscriber);
        if (subs == null) {
            subs = new CompositeDisposable();
        }
        subs.add(disposable);
        subscriptions.put(subscriber, subs);
    }

    private static class SingleTonHolder {
        private static final RxBus INSTANCE = new RxBus();
    }

    private static class Msg {
        public int code;
        public Object object;

        private Msg(int code, Object object) {
            this.code = code;
            this.object = object;
        }
    }

}
