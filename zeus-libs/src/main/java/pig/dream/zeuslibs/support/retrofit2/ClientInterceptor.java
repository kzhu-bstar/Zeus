package pig.dream.zeuslibs.support.retrofit2;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pig.dream.baselib.L;

public class ClientInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request request = chain.request().newBuilder()
                .addHeader("Connection", "Keep-Alive")
                .addHeader("Charset", "UTF-8")
                .build();
        if (L.DBG) {
            Response response = chain.proceed(request);
            return interceptLogWithResponse(response, request);
        }

        return chain.proceed(request);
    }

    private Response interceptLogWithResponse(Response response, Request request) {
        try {
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            ResponseBody body = clone.body();
            if (body == null) {
                return response;
            }

            MediaType mediaType = body.contentType();
            if (mediaType == null) {
                return response;
            }

            String resp = body.string();
            printRequestUrl(request);
            L.json(resp);
            body = ResponseBody.create(mediaType, resp);
            return response.newBuilder().body(body).build();
        } catch (Exception ignored) {
        }

        return response;
    }

    private void printRequestUrl(Request request) {
        if (request.body() instanceof FormBody) {
            FormBody formBody = (FormBody) request.body();
            if (formBody.size() < 3) {
                return;
            }
            String requestMethod = formBody.encodedValue(2);
            L.d(">>>>>>>>>>>> " + request.url().url() + "/" + requestMethod);
        }
    }

}
