package pig.dream.zeuslibs;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Gson 帮助类 单例模式
 *
 * Created by zhukun on 2017/3/10.
 */

public class JsonHelper {

    private static Gson gson=new Gson();

    private JsonHelper() {
    }

    /**
     * 将对象转为JSON串，此方法能够满足大部分需求
     *
     * @param src 将要被转化的对象
     * @return 转化后的JSON串
     */
    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    /**
     * 用来将JSON串转为对象，但此方法不可用来转带泛型的集合
     *
     * @param json 需转化的JSON串
     * @param classOfT 对象的Class
     * @return 返回转化后的对象
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, (Type) classOfT);
    }

    /**
     *  用来将JSON串转为对象，此方法可用来转带泛型的集合，如：Type为
     *              new TypeToken<List<T>>(){}.getType()
     *              ，其它类也可以用此方法调用，就是将List<T>替换为你想要转成的类
     *
     * @param json 需转化的JSON串
     * @param typeOfT 对象的Type
     * @return 返回转化后的对象
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }
}
