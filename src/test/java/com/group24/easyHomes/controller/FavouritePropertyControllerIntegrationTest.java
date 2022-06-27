package com.group24.easyHomes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group24.easyHomes.model.FavoriteProperty;
import com.group24.easyHomes.repository.FavoritePropertyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FavouritePropertyControllerIntegrationTest {
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
    private FavoritePropertyRepository favoritePropertyRepository;

    FavoriteProperty favoriteProperty = new FavoriteProperty(Constants.markedAsFavoriteByUserID,Constants.favoritePropertyID);

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void newFavoriteProperty() throws Exception {
        MockHttpServletRequestBuilder request = post("/favorite-property/add");
        request= request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(favoriteProperty));
        mockMvc.perform(request).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getFavoriteProperties() throws Exception {
        MockHttpServletRequestBuilder request = get("/favorite-property/{userId}",Constants.markedAsFavoriteByUserID);
        request= request.contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void deleteFavoriteProperty() throws Exception {
        MockHttpServletRequestBuilder request = delete("/favorite-property/delete/{favoritePropertyPrimaryId}",Constants.favoritePropertyPrimaryID);
        mockMvc.perform(request).andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllFavoriteProperties() throws Exception {
        mockMvc.perform(get("/favorite-property/favorites"))
                .andExpect(status().isOk());
    }
}
