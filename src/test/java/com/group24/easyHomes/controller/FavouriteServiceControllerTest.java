package com.group24.easyHomes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group24.easyHomes.model.FavoriteService;
import com.group24.easyHomes.repository.FavoriteServiceRepository;
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
public class FavouriteServiceControllerTest {
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
    private FavoriteServiceRepository favoriteServiceRepository;

    FavoriteService favoriteService = new FavoriteService(Constants.markedAsFavoriteByUserID,Constants.favoriteServiceID);

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void newFavoriteService() throws Exception {
        doReturn(favoriteService).when(favoriteServiceRepository).save(any(FavoriteService.class));
        MockHttpServletRequestBuilder request = post("/favorite-service/add");
        request= request.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(favoriteService));
        mockMvc.perform(request).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getFavoriteServices() throws Exception {
        FavoriteService favoriteService1 = new FavoriteService(Constants.markedAsFavoriteByUserID,Constants.favoriteServiceID);

        List<FavoriteService> favoriteServices = new ArrayList<>();
        favoriteServices.add(favoriteService1);
        doReturn(favoriteServices).when(favoriteServiceRepository).findByUserId(anyLong());
        MockHttpServletRequestBuilder request = get("/favorite-service/{userId}",Constants.markedAsFavoriteByUserID);
        request= request.contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void deleteFavoriteService() throws Exception {
        doNothing().when(favoriteServiceRepository).deleteById(anyLong());
        MockHttpServletRequestBuilder request = delete("/favorite-service/delete/{favoriteServicePrimaryId}",Constants.favoriteServicePrimaryID);
        mockMvc.perform(request).andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void getAllFavoriteServices() throws Exception {
        FavoriteService favoriteService1 = new FavoriteService(Constants.markedAsFavoriteByUserID,Constants.favoriteServiceID);
        List<FavoriteService> favoriteServices = new ArrayList<>();
        favoriteServices.add(favoriteService1);
        doReturn(favoriteServices).when(favoriteServiceRepository).findAll();
        mockMvc.perform(get("/favorite-service/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
