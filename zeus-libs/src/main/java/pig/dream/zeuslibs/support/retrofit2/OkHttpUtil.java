package pig.dream.zeuslibs.support.retrofit2;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class OkHttpUtil {
	private static OkHttpClient mOkHttpClient = null;
	private static final int DEFAULT_TIMEOUT_MS = 30000;

	public static OkHttpClient getOkHttpClient(Context ctx, boolean supportCookieJar) {
		if (mOkHttpClient == null) {
            if (supportCookieJar) {
                mOkHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(DEFAULT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                        .readTimeout(DEFAULT_TIMEOUT_MS / 2, TimeUnit.MILLISECONDS)
                        .writeTimeout(DEFAULT_TIMEOUT_MS / 2, TimeUnit.MILLISECONDS)
                        .addInterceptor(new ClientInterceptor())
                        .cookieJar(new CookiesManager(ctx))
                        .build();
            } else {
                mOkHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(DEFAULT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                        .readTimeout(DEFAULT_TIMEOUT_MS / 2, TimeUnit.MILLISECONDS)
                        .writeTimeout(DEFAULT_TIMEOUT_MS / 2, TimeUnit.MILLISECONDS)
                        .addInterceptor(new ClientInterceptor())
                        .build();
            }
		}
		return mOkHttpClient;
	}

}
