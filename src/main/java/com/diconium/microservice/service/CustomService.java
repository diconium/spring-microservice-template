package com.diconium.microservice.service;

import com.diconium.microservice.exception.CustomException;
import com.diconium.microservice.exception.RestCallTemplateException;
import com.diconium.microservice.logging.LogBuilder;
import com.diconium.microservice.model.dto.CustomDTO;
import com.diconium.microservice.utils.RestCallTemplate;
import com.diconium.microservice.utils.URLBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class CustomService {

    private final RestCallTemplate restCallTemplate;
    private final ObjectMapper objectMapper;
    private final URLBuilder urlBuilder;

    @Value("${diconium.ext.url}")
    String extUrl;
    @Value("${diconium.ext.getdto}")
    String extUrlGetDto;
    @Value("${diconium.ext.mockedUsername}")
    private String mockedUser;
    @Value("${diconium.ext.mockedPassword}")
    private String mockedPassword;

    public CustomService(RestCallTemplate restCallTemplate,
        ObjectMapper objectMapper, URLBuilder urlBuilder) {
        this.restCallTemplate = restCallTemplate;
        this.objectMapper = objectMapper;
        this.urlBuilder = urlBuilder;
    }

    public List<CustomDTO> getDtos(String sessionId) {

        LogBuilder.logStart(CustomService.class.getSimpleName(), sessionId, "getDtos");

        List<CustomDTO> result = callDtosBase(sessionId);

        LogBuilder.logEnd(CustomService.class.getSimpleName(), sessionId, "getDtos");
        return result;
    }

    private List<CustomDTO> callDtosBase(String sessionId) {

        LogBuilder.logStart(CustomService.class.getSimpleName(), sessionId, "callDtosBase");

        String url = extUrl + extUrlGetDto;

        Map<String, String> headers = new HashMap<>();
        NameValuePair authorization = parseBasicAuth(mockedUser, mockedPassword, sessionId);

        headers.put(authorization.getName(), authorization.getValue());
        String urlString = urlBuilder.getUrl(url, headers, sessionId).toUriString();

        List<CustomDTO> result;

        try {
            InputStream response = restCallTemplate
                .execute(urlString, HttpMethod.GET, headers, sessionId);

            if (response != null) {
                result = objectMapper.readValue(response, new TypeReference<List<CustomDTO>>() {
                });
            } else {
                LogBuilder.logError(CustomService.class.getSimpleName(), sessionId, "No Data!");
                throw new CustomException("No Data!");
            }

        } catch (IOException e) {
            LogBuilder.logError(CustomService.class.getSimpleName(), sessionId, e.getMessage());
            throw new RestCallTemplateException(e.getMessage());
        }

        LogBuilder.logEnd(CustomService.class.getSimpleName(), sessionId, "callDtosBase");
        return result;
    }

    private NameValuePair parseBasicAuth(String username, String password, String sessionId) {

        Map<String, String> args = new HashMap<>();
        args.put("username", username);
        args.put("password", "******");
        LogBuilder.logStartWithInput(CustomService.class.getSimpleName(), sessionId, "execute", args);

        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(
            auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);

        LogBuilder.logEnd(CustomService.class.getSimpleName(), sessionId, "parseBasicAuth");
        return new BasicNameValuePair("Authorization", authHeader);
    }

}
