package org.densy.scriptify.http.script.function.data;

import org.densy.scriptify.api.script.ScriptObject;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final String url;
    private final String method;
    private final Map<String, String> headers = new HashMap<>();
    private String body = "";
    private String mediaType = "";

    public HttpRequest(String url, String method) {
        this.url = url;
        this.method = method.toUpperCase();
    }

    public void setBody(String body, String mediaType) {
        this.body = body;
        this.mediaType = mediaType;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public Object send(String outputType) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url);

        for (Map.Entry<String, String> header : headers.entrySet()) {
            requestBuilder.addHeader(header.getKey(), header.getValue());
        }

        if (method.equals("POST") || method.equals("PUT")) {

            if (body != null && !body.isEmpty()) {
                RequestBody requestBody = RequestBody.create(body, MediaType.get(mediaType));
                requestBuilder.method(method, requestBody);
            } else {
                requestBuilder.method(method, RequestBody.create(new byte[0], null));
            }
        } else {
            requestBuilder.method(method, null);
        }

        OkHttpClient client = new OkHttpClient();

        try (Response response = client.newCall(requestBuilder.build()).execute()) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                return ScriptObject.of(switch(outputType.toUpperCase()) {
                    case "STRING" -> responseBody.string();
                    case "BYTES" -> responseBody.bytes();
                    default -> throw new IllegalArgumentException("Unsupported output type: " + outputType);
                });
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
