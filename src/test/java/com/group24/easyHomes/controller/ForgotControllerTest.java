package com.group24.easyHomes.controller;


import com.group24.easyHomes.service.ForgotService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class ForgotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ForgotService forgotService;



    @Test
    void notRegisteredEmailTest() throws Exception {

        mockMvc.perform(post("/user/forgotpassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test852474@gmail.com\"}")
                .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

//    @Test
//    void registeredEmailTest() throws Exception {
//
//        mockMvc.perform(post("/user/forgotpassword")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"email\":\"iamdv43@gmail.com\"}")
//                .accept("application/json"))
//                .andExpect(MockMvcResultMatchers.status().isAccepted());
//    }

    @Test
    void otpverificationPass() throws Exception {

        int testOTP = 123;
        forgotService.setOtp(testOTP);

        mockMvc.perform(post("/user/otpverification")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"otp\":123}")
                .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void otpverificationFail() throws Exception {

        int testOTP = 123;
        forgotService.setOtp(testOTP);

        mockMvc.perform(post("/user/otpverification")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"otp\":124}")
                .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    void newpasswordPass() throws Exception {

        mockMvc.perform(post("/user/newpassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"iamdv43@gmail.com\", \"password\":\"Dhruv\"}")
                .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }
}