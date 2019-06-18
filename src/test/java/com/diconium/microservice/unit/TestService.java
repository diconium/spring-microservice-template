package com.diconium.microservice.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.diconium.microservice.MicroserviceApplication;
import com.diconium.microservice.logging.LogBuilder;
import com.diconium.microservice.model.dto.CustomDTO;
import com.diconium.microservice.service.CustomService;
import com.diconium.microservice.util.FileParser;
import com.diconium.microservice.utils.RestCallTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MicroserviceApplication.class)
public class TestService {

    @Value("${diconium.ext.mockedUsername}")
    private String mockedUser;
    @Value("${diconium.ext.mockedPassword}")
    private String mockedPassword;
    @Mock
    private RestCallTemplate restCallTemplate;

    @InjectMocks
    @Autowired
    private CustomService service;

    private String validApiResponse;
    private List<CustomDTO> validServiceResponse;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        try {
            prepareJsonMocks();
            prepareMocks();
        } catch (IOException | ParseException e) {
            LogBuilder.logError(TestService.class.getSimpleName(), "", e.getMessage());
        }
    }

    private void prepareJsonMocks() throws IOException, ParseException {
        ObjectMapper objectMapper = new ObjectMapper();

        validApiResponse = FileParser.parseJSONArray("DTOApiValidResponse.json").toJSONString();

        validServiceResponse = objectMapper
            .readValue(FileParser.parseJSONArray("DTOServiceValidResponse.json").toJSONString(),
                new TypeReference<List<CustomDTO>>() {
                });
    }

    private void prepareMocks() {
        try {
            InputStream stream = new ByteArrayInputStream(validApiResponse.getBytes());
            when(restCallTemplate.execute(anyString(), any(HttpMethod.class), any(HashMap.class), anyString()))
                .thenReturn(stream);
        } catch (Exception e) {
            LogBuilder.logError(TestService.class.getSimpleName(), "", e.getMessage());
        }

    }

    @Test
    public void Should_ReturnAValidResponse() {

        try {
            List<CustomDTO> response = service.getDtos("");

            Assert.assertEquals(validServiceResponse, response);
        } catch (Exception e) {
            LogBuilder.logError(TestService.class.getSimpleName(), "", e.getMessage());
        }

    }
}
