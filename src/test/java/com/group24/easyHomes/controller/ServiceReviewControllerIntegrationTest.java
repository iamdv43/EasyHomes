package com.group24.easyHomes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group24.easyHomes.dto.ServiceReviewDTO;
import com.group24.easyHomes.model.ServiceReview;
import com.group24.easyHomes.model.Services;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ServiceReviewControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void addServiceReview_SUCCESS() throws Exception {
        ServiceReviewDTO serviceReviewDTO = new ServiceReviewDTO();
        serviceReviewDTO.setService_id(1L);
        serviceReviewDTO.setReview_id(1);
        serviceReviewDTO.setReview_description("Great customer service");
        serviceReviewDTO.setReview_subject("Good");
        serviceReviewDTO.setReview_rating(1);
        serviceReviewDTO.setUser_id(1L);
        MockHttpServletRequestBuilder request = post("/serviceReview/add");
        request= request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(serviceReviewDTO));
        mockMvc.perform(request).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void deleteReview_ERROR() throws Exception {
        MockHttpServletRequestBuilder request = delete("/serviceReview/{serviceID}/{reviewID}",
                Constants.reviewForServiceID,Constants.reviewIDToBeDeleted);
        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

}
