package com.diconium.microservice.unit;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.diconium.microservice.controller.Controller;
import com.diconium.microservice.logging.LogBuilder;
import com.diconium.microservice.model.dto.CustomDTO;
import com.diconium.microservice.service.CustomService;
import com.diconium.microservice.util.FileParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
public class TestController {

    private MockMvc mockMvc;

    @Mock
    private CustomService service;

    @InjectMocks
    @Autowired
    private Controller controller;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockUser();
    }

    private void mockUser() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void Should_ReturnDtoList() {

        try {

            String minimalResponse = "[]";
            ObjectMapper objectMapper = new ObjectMapper();

            List<CustomDTO> validServiceResponse = objectMapper
                .readValue(FileParser.parseJSONArray("DTOServiceValidResponse.json").toJSONString(),
                    new TypeReference<List<CustomDTO>>() {
                    });

            when(service.getDtos(""))
                .thenReturn(validServiceResponse);

            MockHttpServletResponse response =
                mockMvc
                    .perform(
                        get("/api/dto"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse();

            List responseObject = objectMapper.readValue(response.getContentAsString(), List.class);

            Assert.assertFalse(response.getContentAsString().isEmpty());
            Assert.assertTrue(response.getContentAsString().length() >= minimalResponse.length());
            Assert.assertEquals(3, responseObject.size());
        } catch (Exception e) {
            LogBuilder.logError(TestController.class.getSimpleName(), "", e.getMessage());
        }
    }
}
