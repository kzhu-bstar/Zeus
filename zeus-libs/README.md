# zeus-libs库

## 介绍

### 结构

> 工具类（kit_配套小元件）

    1. DeviceUUIDHelper 手机Device ID获取工具类
    2. JsonHelper gson的帮助类，提供json与对象的转化的静态方法
    3. L android Logcat日志帮助类，提供Debug版本和Release版本下的日志开启和关闭
    4. ResourcesHelper 资源处理帮助类，提供Drawable、Color适配高版本api，获取手机屏幕宽高，dp与px的互转
    5. ScreenSizeHelper 手机屏幕尺寸获取帮助类，提供初始化方法init来获取屏幕宽高并保存到SharedPreferences中，供后面使用
    6. SPHelper SharedPreferences使用帮助类，提供简化SharedPreferences保存数据的api
    7. Timer 时间记录器，提供打印方法执行时间的能力
    8. ViewFinder 视图查找帮助类，提供静态方法类查找Activity和View下的视图根据id
    9. WidgetEnableStatusHelper EditText和CheckBox控件根据条件实现Button的enable状态启用和关闭

> 使用类

> 第三方支持类库

    1. logger 漂亮的log日志输出
    2. recyclerview 列表封装
    3. retrofit2
    4. rxbus RxJava实现的事件发送接收库
    5. statusbar 沉浸式状态栏处理

> layout UI布局层封装

    BaseActivity、BaseFragment是所有页面的基类，可根据所需功能，自定义启用需要的功能，包括（MVP模式，ToolBar集成、SwipeBackLayout滑动返回功能）


## proguard-rules.pro注意

```
# 保留sdk系统自带的一些内容,下面的sdk需要使用
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

# OkHttp3 okio
-dontwarn okhttp3.**
# -keep class okhttp3.** { *;}
-dontwarn okio.**
# -keep class okio.** { *;}

# retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *;}

# RxJava RxAndroid

# Gson
-keep class sun.misc.Unsafe { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# BaseRecyclerViewAdaperHelper
-keep class com.chad.library.adapter.** { *; }

# SerializableOkHttpCookies
-keep class pig.dream.baselib.support.retrofit2.SerializableOkHttpCookies { *; }

```

## 如何在Library库使用BuildConfig

### Library的build.gradle
```
android {
    publishNonDefault true
}
```

### App的build.gradle
```
dependencies {
    releaseCompile project(path: ':library', configuration: 'release')
    debugCompile project(path: ':library', configuration: 'debug')
}
```

## 代码规范

### drawable目录下文件规范

```
名称格式： 此文件的作用_(控件_)使用范围_
例子：
    layer_progressbar_horizontal.xml（progressbar 横向layer-list效果）
    selector_add_new_friend.xml (添加新朋友的按键选中效果)
    selector_corners_6_buy.xml（购买的圆角6dp的选中效果）
    shape_corners_20_solid_white.xml（圆角20dp 白色背景）
```

| Item     | Value | Qty   |
| :------- | ----: | :---: |
| Computer | $1600 |  5    |
| Phone    | $12   |  12   |
| Pipe     | $1    |  234  |