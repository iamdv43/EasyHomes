package com.group24.easyHomes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group24.easyHomes.model.Property;
import com.group24.easyHomes.model.PropertyListQuery;
import com.group24.easyHomes.model.Services;
import com.group24.easyHomes.model.ServicesListQuery;
import com.group24.easyHomes.service.AppUserService;
import com.group24.easyHomes.service.ServicesService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @MockBean
    private ServicesService mockService;

    @MockBean
    private AppUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    final static String service =" {\n" +
            "        \"service_id\": 16,\n" +
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
            "        \"user_name\": \"Daman Kaur\",\n" +
            "        \"posted_on\": null\n" +
            "    },";

    Services serviceResponse = new Services("Halifax Tiffin Services",
            "Food Delivery",
            199,
            "monthly",
            "Welcome to Canada's food guide. ... Eat a variety of healthy foods each day. Healthy foods. Healthy eating is more than the foods",
            "halifax",
            "NS",
            "Canada",
            "h3h5k3",
            "2040, street",
            4L);

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getServices_Success() throws Exception {

        Services services = new Services();

        List<Services> allServices = new ArrayList<>();
        allServices.add(services);

        given(mockService.listAll()).willReturn(allServices);

        mockMvc.perform(get("/service/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void removeService_SUCCESS() throws Exception {
        when(mockService.delete(Constants.serviceID)).thenReturn("SUCCESS");
        mockMvc.perform(delete("/service/services/{serviceId}", Constants.serviceID))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void removeService_ERROR() throws Exception {
        when(mockService.delete(Constants.serviceID)).thenReturn("ERROR");
        MockHttpServletRequestBuilder request =delete("/service/services/{serviceId}", Constants.serviceID);
        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void updateProperty_ERROR() throws Exception {
        when(mockService.updateService(Constants.serviceID, serviceResponse)).thenReturn(null);
        MockHttpServletRequestBuilder request  = put("/service/services/{serviceId}/update", Constants.serviceID);
        request= request.contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void updateService_SUCCESS() throws Exception {
        when(mockService.updateService(Constants.serviceID,serviceResponse)).thenReturn(serviceResponse);
        MockHttpServletRequestBuilder request = put("/service/services/{serviceId}/update", Constants.serviceID);
        request= request.contentType(MediaType.APPLICATION_JSON).content(service.getBytes());
        mockMvc.perform(request).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS() throws Exception {
        List<Services> services = new ArrayList<>();
        services.add(Constants.services);

        ServicesListQuery query = new ServicesListQuery();
        query.setService_name("Tiffin");
        query.setService_type("Food Delivery");
        query.setCost(199);
        query.setPlan("monthly");
        query.setCity("Halifax");
        query.setProvince("NS");
        query.setCountry("Canada");


        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
        mockMvc.perform(request).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_ERROR() throws Exception {
        List<Services> services = new ArrayList<>();
        services.add(Constants.services);

        ServicesListQuery query = new ServicesListQuery();
        query.setService_name("Tiffin");
        query.setService_type(null);
        query.setCost(null);
        query.setPlan(null);
        query.setCity(null);
        query.setProvince(null);
        query.setCountry(null);

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_ERROR_Bad_Request() throws Exception {
        List<Services> services = new ArrayList<>();
        services.add(Constants.services);

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null));
        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_Service_Type() throws Exception {
        List<Services> services = new ArrayList<>();
        services.add(Constants.services);

        ServicesListQuery query = new ServicesListQuery();
        query.setService_type("Food Delivery");

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_Cost() throws Exception {
        List<Services> services = new ArrayList<>();
        services.add(Constants.services);

        ServicesListQuery query = new ServicesListQuery();
        query.setCost(199);

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_Cost_And_Empty_List() throws Exception {
        List<Services> services = new ArrayList<>();

        ServicesListQuery query = new ServicesListQuery();
        query.setCost(199);

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_Plan() throws Exception {
        List<Services> services = new ArrayList<>();
        services.add(Constants.services);

        ServicesListQuery query = new ServicesListQuery();
        query.setPlan("monthly");

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_Plan_And_Empty_List() throws Exception {
        List<Services> services = new ArrayList<>();

        ServicesListQuery query = new ServicesListQuery();
        query.setPlan("Plan_monthly");

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_Plan_And_Cost() throws Exception {
        List<Services> services = new ArrayList<>();
        services.add(Constants.services);

        ServicesListQuery query = new ServicesListQuery();
        query.setPlan("monthly");
        query.setCost(199);

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_Plan_And_Cost_And_Empty_List() throws Exception {
        List<Services> services = new ArrayList<>();

        ServicesListQuery query = new ServicesListQuery();
        query.setPlan("plan_monthly");
        query.setCost(199);

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // implement test case to filter services by city
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_City() throws Exception {
        List<Services> services = new ArrayList<>();
        services.add(Constants.services);

        ServicesListQuery query = new ServicesListQuery();
        query.setCity("Halifax");

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_City_And_Empty_List() throws Exception {
        List<Services> services = new ArrayList<>();

        ServicesListQuery query = new ServicesListQuery();
        query.setCity("Halifax");

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_City_And_Plan() throws Exception {
        List<Services> services = new ArrayList<>();
        services.add(Constants.services);

        ServicesListQuery query = new ServicesListQuery();
        query.setCity("Halifax");
        query.setPlan("monthly");

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_City_And_Plan_And_Empty_List() throws Exception {
        List<Services> services = new ArrayList<>();

        ServicesListQuery query = new ServicesListQuery();
        query.setCity("Halifax");
        query.setPlan("monthly");

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_City_And_Plan_And_Service_Type() throws Exception {
        List<Services> services = new ArrayList<>();
        services.add(Constants.services);

        ServicesListQuery query = new ServicesListQuery();
        query.setCity("Halifax");
        query.setPlan("monthly");
        query.setService_type("Food Delivery");

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_City_And_Plan_And_Service_Type_And_Empty_List() throws Exception {
        List<Services> services = new ArrayList<>();

        ServicesListQuery query = new ServicesListQuery();
        query.setCity("Halifax");
        query.setPlan("monthly");
        query.setService_type("Food Delivery");

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_City_And_Plan_And_Service_Type_And_Cost() throws Exception {
        List<Services> services = new ArrayList<>();
        services.add(Constants.services);

        ServicesListQuery query = new ServicesListQuery();
        query.setCity("Halifax");
        query.setPlan("monthly");
        query.setService_type("Food Delivery");
        query.setCost(199);

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_City_And_Plan_And_Service_Type_And_Cost_And_Empty_List() throws Exception {
        List<Services> services = new ArrayList<>();

        ServicesListQuery query = new ServicesListQuery();
        query.setCity("Halifax");
        query.setPlan("monthly");
        query.setService_type("Food Delivery");
        query.setCost(199);

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_Province_And_Empty_List() throws Exception {
        List<Services> services = new ArrayList<>();

        ServicesListQuery query = new ServicesListQuery();
        query.setProvince("NS");

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_City_And_Plan_And_Service_Type_And_Cost_And_Province() throws Exception {
        List<Services> services = new ArrayList<>();
        services.add(Constants.services);

        ServicesListQuery query = new ServicesListQuery();
        query.setCity("Halifax");
        query.setPlan("monthly");
        query.setService_type("Food Delivery");
        query.setCost(199);
        query.setProvince("NS");

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_City_And_Plan_And_Service_Type_And_Cost_And_Province_And_Empty_List() throws Exception {
        List<Services> services = new ArrayList<>();

        ServicesListQuery query = new ServicesListQuery();
        query.setCity("Halifax");
        query.setPlan("monthly");
        query.setService_type("Food Delivery");
        query.setCost(199);
        query.setProvince("NS");

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_Province_And_City_And_Plan_And_Service_Type_And_Cost() throws Exception {
        List<Services> services = new ArrayList<>();
        services.add(Constants.services);

        ServicesListQuery query = new ServicesListQuery();
        query.setProvince("NS");
        query.setCity("Halifax");
        query.setPlan("monthly");
        query.setService_type("Food Delivery");
        query.setCost(199);

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllServices_SUCCESS_By_Province_And_City_And_Plan_And_Service_Type_And_Cost_And_Empty_List() throws Exception {
        List<Services> services = new ArrayList<>();

        ServicesListQuery query = new ServicesListQuery();
        query.setProvince("NS");
        query.setCity("Halifax");
        query.setPlan("monthly");
        query.setService_type("Food Delivery");
        query.setCost(199);

        doReturn(services).when(mockService).filterServices(any(ServicesListQuery.class));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/service/services/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

}
