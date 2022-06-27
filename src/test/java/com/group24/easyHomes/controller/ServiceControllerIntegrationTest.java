package com.group24.easyHomes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group24.easyHomes.model.PropertyListQuery;
import com.group24.easyHomes.model.ServicesListQuery;
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

import java.util.Random;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ServiceControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    final static String service =" {\n" +
            "        \"service_name\": \"Halifax Tiffin Services\",\n" +
            "        \"service_type\": \"Food Delivery\",\n" +
            "        \"cost\": 199,\n" +
            "        \"plan\": \"monthly\",\n" +
            "        \"description\": \"Welcome to Canada's food guide. ... Eat a variety of healthy foods each day. Healthy foods. Healthy eating is more than the foods\",\n" +
            "        \"city\": \"halifax\",\n" +
            "        \"province\": \"NS\",\n" +
            "        \"country\": \"Canada\",\n" +
            "        \"pincode\": \"h3h5k3\",\n" +
            "        \"address\": \"2040, street\",\n" +
            "        \"user_id\": 1,\n" +
            "        \"user_name\": \"Dhruvrajsinh Omkarsinh Vansia\",\n" +
            "        \"posted_on\": null\n" +
            "    },";

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getServices() throws Exception {
        mockMvc.perform(get("/service/services"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void addService() throws Exception {
        MockHttpServletRequestBuilder request = post("/service/services");
        request= request.contentType(MediaType.APPLICATION_JSON).content(service.getBytes());
        mockMvc.perform(request).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void updateService_SUCCESS() throws Exception {
        MockHttpServletRequestBuilder request = put("/service/services/{serviceId}/update", Constants.serviceID);
        request= request.contentType(MediaType.APPLICATION_JSON).content(service.getBytes());
        mockMvc.perform(request).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void removeService_ERROR() throws Exception {
        MockHttpServletRequestBuilder request =delete("/service/services/{serviceId}", Constants.serviceIDDoesNotExist);
        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void updateService_ERROR() throws Exception {
        MockHttpServletRequestBuilder request = put("/service/services/{serviceId}/update", Constants.serviceIDDoesNotExist);
        request = request.contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterServices_SUCCESS_withRequestBody() throws Exception {
        ServicesListQuery query = new ServicesListQuery();
        query.setService_name("Tiffin");
        query.setPlan("monthly");
        query.setCountry("Canada");
        query.setCity("Halifax");
        query.setService_type("Food Delivery");
        query.setProvince("NS");
        query.setCost(199);

        MockHttpServletRequestBuilder request  = post("/service/services/filter");
        request = request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(query));
        mockMvc.perform(request).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterServices_ERROR_withRequestBody() throws Exception {

        MockHttpServletRequestBuilder request  = post("/service/services/filter");
        request = request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(null));
        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

}