package com.diconium.microservice.utils;

import com.diconium.microservice.exception.RestCallTemplateException;
import com.diconium.microservice.logging.LogBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class RestCallTemplate {

    private HttpClient client;
    private int statusCode;
    private InputStream content;
    private String reasonPhrase;

    public RestCallTemplate() {
        client = HttpClientBuilder.create().build();
    }

    public InputStream execute(String url, HttpMethod method, Map<String, String> headers,
        String sessionId) {

        Map<String, String> args = URLBuilder.cleanMap(url, headers);
        args.put("method", method.toString());

        LogBuilder
            .logStartWithInput(RestCallTemplate.class.getSimpleName(), sessionId, "execute", args);

        HttpRequestBase request;
        switch (method) {
            case GET:
                request = new HttpGet(url);
                break;
            case POST:
                request = new HttpPost(url);
                break;
            case DELETE:
                request = new HttpDelete(url);
                break;
            case HEAD:
                request = new HttpHead(url);
                break;
            case PATCH:
                request = new HttpPatch(url);
                break;
            case PUT:
                request = new HttpPut(url);
                break;
            default:
                throw new IllegalArgumentException("Invalid or null HttpMethod: " + method);
        }

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request.addHeader(entry.getKey(), entry.getValue());
        }

        HttpResponse response = null;

        try {
            response = client.execute(request);
        } catch (IOException e) {
            LogBuilder.logError(RestCallTemplate.class.getSimpleName(), sessionId, e.getMessage());
        }

        try {
            if (response != null && response.getStatusLine() != null) {
                statusCode = response.getStatusLine().getStatusCode();
                content = response.getEntity().getContent();
                reasonPhrase = response.getStatusLine().getReasonPhrase();
            }
        } catch (RestCallTemplateException | IOException e) {
            LogBuilder.logError(RestCallTemplate.class.getSimpleName(), sessionId, e.getMessage());
        }

        if (statusCode == 200) {

            LogBuilder.logEnd(RestCallTemplate.class.getSimpleName(), sessionId, "execute");
            return content;

        } else {
            LogBuilder.logError(RestCallTemplate.class.getSimpleName(), sessionId,
                " Error: " + statusCode + " - " + reasonPhrase);
            throw new RestCallTemplateException("Error: " + statusCode + " - " + reasonPhrase);
        }
    }
}
