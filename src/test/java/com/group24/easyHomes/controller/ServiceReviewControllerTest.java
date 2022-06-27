package com.group24.easyHomes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group24.easyHomes.dto.ServiceReviewDTO;
import com.group24.easyHomes.model.PaymentDetails;
import com.group24.easyHomes.model.Property;
import com.group24.easyHomes.model.ServiceReview;
import com.group24.easyHomes.service.PaymentService;
import com.group24.easyHomes.service.ServiceReviewServices;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceReviewControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @MockBean
    private ServiceReviewServices service;


    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void addReview() throws Exception {
        ServiceReview serviceReview = new ServiceReview(Constants.reviewGivenByUserID,"Great customer service",
                "Good",Constants.reviewRating,null);
        doReturn(serviceReview).when(service).saveOrUpdateReview(any());
        MockHttpServletRequestBuilder request = post("/serviceReview/add");
        request= request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(serviceReview));
        mockMvc.perform(request).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void deleteReview_ERROR() throws Exception {
        doReturn(false).when(service).deletebyId(anyLong(),any());
        MockHttpServletRequestBuilder request = delete("/serviceReview/{serviceID}/{reviewID}",
                Constants.reviewForServiceID,Constants.reviewIDToBeDeleted);
        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void deleteReview_SUCCESS() throws Exception {
        doReturn(true).when(service).deletebyId(anyLong(),any());
        MockHttpServletRequestBuilder request = delete("/serviceReview/{serviceID}/{reviewID}",Constants.reviewForServiceID,Constants.reviewIDToBeDeleted);
        mockMvc.perform(request).andExpect(status().isOk());
    }
}
