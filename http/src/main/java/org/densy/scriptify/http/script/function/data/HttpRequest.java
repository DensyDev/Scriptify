package org.densy.scriptify.http.script.function.data;

import okhttp3.*;
import org.densy.scriptify.api.script.module.export.access.ScriptAccess;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final String url;
    private final HttpMethod method;
    private final Map<String, String> headers = new HashMap<>();
    private String body = "";
    private String mediaType = "";

    @ScriptAccess.Export
    public HttpRequest(String url, HttpMethod method) {
        this.url = url;
        this.method = method;
    }

    @ScriptAccess.Export
    public void setBody(String body, String mediaType) {
        this.body = body;
        this.mediaType = mediaType;
    }

    @ScriptAccess.Export
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    @ScriptAccess.Export
    public Object send(String outputType) {
        return this.send(OutputType.valueOf(outputType));
    }

    @ScriptAccess.Export
    public Object send(OutputType outputType) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url);

        for (Map.Entry<String, String> header : headers.entrySet()) {
            requestBuilder.addHeader(header.getKey(), header.getValue());
        }

        if (method.equals(HttpMethod.POST) || method.equals(HttpMethod.PUT)) {
            if (body != null && !body.isEmpty()) {
                RequestBody requestBody = RequestBody.create(body, MediaType.get(mediaType));
                requestBuilder.method(method.name(), requestBody);
            } else {
                requestBuilder.method(method.name(), RequestBody.create(new byte[0], null));
            }
        } else {
            requestBuilder.method(method.name(), null);
        }

        OkHttpClient client = new OkHttpClient();

        try (Response response = client.newCall(requestBuilder.build()).execute()) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                return switch(outputType) {
                    case STRING -> responseBody.string();
                    case BYTES -> responseBody.bytes();
                };
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
