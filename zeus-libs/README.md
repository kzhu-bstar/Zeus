# zeus-libs

## 介绍

### 结构

> 工具类

    1.

> 使用类

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