package com.diconium.microservice.utils;

import com.diconium.microservice.logging.LogBuilder;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class URLBuilder {

    public UriComponentsBuilder getUrl(String url, Map<String, String> map, String sessionId) {

        Map<String, String> args = cleanMap(url, map);

        LogBuilder.logStartWithInput(URLBuilder.class.getSimpleName(), sessionId, "getUrl", args);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        LogBuilder.logEnd(URLBuilder.class.getSimpleName(), sessionId, "getUrl");
        return builder;
    }

    public static Map<String, String> cleanMap(String url, Map<String, String> map) {
        Map<String, String> args = new HashMap<>();
        args.put("url", url);

        Map<String, String> publicMap = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!entry.getKey().equals("Authorization")) {
                publicMap.put(entry.getKey(), entry.getValue());
            }
        }
        if (!publicMap.isEmpty()) {
            args.put("header", publicMap.values().toString());
        }
        return args;
    }
}
