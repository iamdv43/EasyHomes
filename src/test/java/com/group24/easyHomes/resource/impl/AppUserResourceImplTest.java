package com.group24.easyHomes.resource.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group24.easyHomes.model.AppUser;
import com.group24.easyHomes.repository.AppUserRepository;
import com.group24.easyHomes.service.ForgotService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
class AppUserResourceImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ForgotService forgotService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppUserRepository appUserRepository;

    private String email = "";


//    @Test
//    public void registerSuccess() throws Exception {
//
//        String name = "dv";
//        String domain = "@gmail.com";
//
//        String email = name + domain;
//
//        if(appUserRepository.findByEmail(email).isPresent()){
//            email = name + Math.random() + domain;
//        }
//
//        AppUser user = new AppUser( "Dhruv", "Vansia","abc", email);
//
//        mockMvc.perform(post("/user/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(user))
//                .accept("application/json"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }

    @Test
    public void registerFailUserExist() throws Exception {

        AppUser user = new AppUser( "Dhruv", "Vansia","abc", email);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
                .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

//    @Test
//    public void authenticateSuccess() throws Exception {
//
//        AppUser user = new AppUser( "Dhruvrajsinh Omkarsinh", "Vansia","okn", "iamdv43@gmail.com");
//
//        mockMvc.perform(post("/user/authenticate")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(user))
//                .accept("application/json"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }

    @Test
    public void authenticateFail() throws Exception {

        AppUser user = new AppUser( "Dhruvrajsinh Omkarsinh", "Vansia","plm", "iamdv43@gmail.com");

        mockMvc.perform(post("/user/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
                .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}