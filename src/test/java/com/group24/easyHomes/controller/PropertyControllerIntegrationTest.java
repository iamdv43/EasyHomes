package com.group24.easyHomes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group24.easyHomes.EasyHomesApplication;
import com.group24.easyHomes.model.AppUser;
import com.group24.easyHomes.model.Property;
import com.group24.easyHomes.model.PropertyAddress;
import com.group24.easyHomes.model.PropertyListQuery;
import com.group24.easyHomes.service.AppUserService;
import com.group24.easyHomes.service.PropertyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PropertyControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    final static String property = "{" + "\"user_id\": 1,\n" +
            "\"property_name\": \"Apt "+ ((new Random().nextInt(900-700)) +700)  + " Iris Apartments\",\n" +
            "        \"address\":{\n" +
            "            \"location\" : \"University Street\",\n" +
            "            \"city\": \"Halifax\",\n" +
            "            \"province\":\"NS\",\n" +
            "            \"country\": \"Canada\",\n" +
            "            \"postal_code\": \"H2Y8IK\"\n" +
            "        },\n" +
            "        \"amenities\":\"Laundry\",\n" +
            "        \"property_type\":\"1 BHK\",\n" +
            "         \"bathrooms\":1,\n" +
            "         \"bedrooms\":2,\n" +
            "        \"parking_included\":\"true\",\n" +
            "        \"rent\":\"500.0\"\n" +
            "}";

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getProperties() throws Exception {

        mockMvc.perform(get("/property/properties"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void addProperty() throws Exception {

        MockHttpServletRequestBuilder request = post("/property/property");
        request= request.contentType(MediaType.APPLICATION_JSON).content(property.getBytes());
        mockMvc.perform(request).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void updateProperty_SUCCESS() throws Exception {
        MockHttpServletRequestBuilder request = put("/property/properties/{propertyId}/update", Constants.propertyID);
        request= request.contentType(MediaType.APPLICATION_JSON).content(property.getBytes());
        mockMvc.perform(request).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void removeProperty_ERROR() throws Exception {
        MockHttpServletRequestBuilder request =delete("/property/properties/{propertyId}", Constants.propertyIDDoesNotExist);
        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void updateProperty_ERROR() throws Exception {
        MockHttpServletRequestBuilder request  = put("/property/properties/{propertyId}/update", Constants.propertyIDDoesNotExist);
        request= request.contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperty_SUCCESS() throws Exception {
        MockHttpServletRequestBuilder request  = put("/property/properties/{propertyId}/update", Constants.propertyIDDoesNotExist);
        request= request.contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withRequestBody() throws Exception {
        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name(null);
        query.setAmenities(null);
        query.setCountry(null);
        query.setCity("Halifax");
        query.setProperty_type(null);
        query.setProvince(null);
        query.setParkingIncluded(null);
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setRent(null);

        MockHttpServletRequestBuilder request  = post("/property/properties/filter");
        request= request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(query));
        mockMvc.perform(request).andExpect(jsonPath("$", hasSize(greaterThan(1))));;
    }

    // implement filterProperties_ERROR_withRequestBody()
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_ERROR_withRequestBody() throws Exception {
//        PropertyListQuery query = new PropertyListQuery();
//        query.setProperty_name(null);
//        query.setAmenities(null);
//        query.setCountry(null);
//        query.setCity("Halifax");
//        query.setProperty_type(null);
//        query.setProvince(null);
//        query.setParkingIncluded(null);
//        query.setNumberOfBathrooms(null);
//        query.setNumberOfBedrooms(null);
//        query.setRent(null);

        MockHttpServletRequestBuilder request  = post("/property/properties/filter");
        request = request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(null));
        mockMvc.perform(request).andExpect(status().isBadRequest());;
    }

    // implement filterProperties_SUCCESS_withEmptyRequestBody
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withEmptyRequestBody() throws Exception {
        PropertyListQuery query = new PropertyListQuery();

        MockHttpServletRequestBuilder request  = post("/property/properties/filter");
        request = request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(query));
        mockMvc.perform(request).andExpect(jsonPath("$", hasSize(greaterThan(1))));;
    }

    // implement filterProperties_SUCCESS_withAllNullRequestBody
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withAllNullRequestBody() throws Exception {
        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name(null);
        query.setProperty_type(null);
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity(null);
        query.setProvince(null);
        query.setCountry(null);

        MockHttpServletRequestBuilder request  = post("/property/properties/filter");
        request = request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(query));
        mockMvc.perform(request).andExpect(jsonPath("$", hasSize(greaterThan(1))));;
    }

    // implement filterProperties_SUCCESS_withAllEmptyRequestBody
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withAllEmptyRequestBody() throws Exception {
        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name("");
        query.setProperty_type("");
        query.setNumberOfBathrooms(null);
        query.setNumberOfBedrooms(null);
        query.setParkingIncluded(null);
        query.setRent(null);
        query.setCity("");
        query.setProvince("");
        query.setCountry("");

        MockHttpServletRequestBuilder request  = post("/property/properties/filter");
        request = request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(query));
        mockMvc.perform(request).andExpect(jsonPath("$", hasSize(greaterThan(1))));;
    }

    // implement filterProperties_SUCCESS_withPropertyNameAndPropertyType
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withPropertyNameAndPropertyType() throws Exception {
        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name("Killam");
        query.setProperty_type("House");

        MockHttpServletRequestBuilder request  = post("/property/properties/filter");
        request = request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(query));
        mockMvc.perform(request).andExpect(jsonPath("$", hasSize(0)));;
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_withPropertyName_noResults() throws Exception {
        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_name("House");

        MockHttpServletRequestBuilder request  = post("/property/properties/filter");
        request = request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(query));
        mockMvc.perform(request).andExpect(jsonPath("$", hasSize(0)));;
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withPropertyType_noResults() throws Exception {
        PropertyListQuery query = new PropertyListQuery();
        query.setProperty_type("House type");

        MockHttpServletRequestBuilder request  = post("/property/properties/filter");
        request = request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(query));
        mockMvc.perform(request).andExpect(jsonPath("$", hasSize(0)));;
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withNumberOfBedrooms() throws Exception {
        PropertyListQuery query = new PropertyListQuery();
        query.setNumberOfBedrooms(1);

        MockHttpServletRequestBuilder request  = post("/property/properties/filter");
        request = request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(query));
        mockMvc.perform(request).andExpect(jsonPath("$", hasSize(greaterThan(1))));;
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withNumberOfBedrooms_noResults() throws Exception {
        PropertyListQuery query = new PropertyListQuery();
        query.setNumberOfBedrooms(10);

        MockHttpServletRequestBuilder request  = post("/property/properties/filter");
        request = request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(query));
        mockMvc.perform(request).andExpect(jsonPath("$", hasSize(0)));;
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withNumberOfBathrooms() throws Exception {
        PropertyListQuery query = new PropertyListQuery();
        query.setNumberOfBathrooms(1);

        MockHttpServletRequestBuilder request  = post("/property/properties/filter");
        request = request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(query));
        mockMvc.perform(request).andExpect(jsonPath("$", hasSize(greaterThan(1))));;
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withNumberOfBathrooms_noResults() throws Exception {
        PropertyListQuery query = new PropertyListQuery();
        query.setNumberOfBathrooms(10);

        MockHttpServletRequestBuilder request  = post("/property/properties/filter");
        request = request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(query));
        mockMvc.perform(request).andExpect(jsonPath("$", hasSize(0)));;
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withParkingIncluded() throws Exception {
        PropertyListQuery query = new PropertyListQuery();
        query.setParkingIncluded(true);

        MockHttpServletRequestBuilder request  = post("/property/properties/filter");
        request = request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(query));
        mockMvc.perform(request).andExpect(jsonPath("$", hasSize(greaterThan(1))));;
    }


    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withCity() throws Exception {
        PropertyListQuery query = new PropertyListQuery();
        query.setCity("London");

        MockHttpServletRequestBuilder request  = post("/property/properties/filter");
        request = request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(query));
        mockMvc.perform(request).andExpect(jsonPath("$", hasSize(greaterThan(1))));;
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withCity_noResults() throws Exception {
        PropertyListQuery query = new PropertyListQuery();
        query.setCity("London test123");

        MockHttpServletRequestBuilder request  = post("/property/properties/filter");
        request = request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(query));
        mockMvc.perform(request).andExpect(jsonPath("$", hasSize(greaterThan(1))));;
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withProvince() throws Exception {
        PropertyListQuery query = new PropertyListQuery();
        query.setProvince("ON");

        MockHttpServletRequestBuilder request  = post("/property/properties/filter");
        request = request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(query));
        mockMvc.perform(request).andExpect(jsonPath("$", hasSize(greaterThan(1))));;
    }
    
    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void filterProperties_SUCCESS_withCountry() throws Exception {
        PropertyListQuery query = new PropertyListQuery();
        query.setCountry("Canada");

        MockHttpServletRequestBuilder request  = post("/property/properties/filter");
        request = request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(query));
        mockMvc.perform(request).andExpect(jsonPath("$", hasSize(greaterThan(1))));;
    }


}
