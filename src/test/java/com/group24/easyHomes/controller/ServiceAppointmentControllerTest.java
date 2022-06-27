package com.group24.easyHomes.controller;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class ServiceAppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    void bookAppointment() throws Exception {
//
//        mockMvc.perform(post("/service/contact")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"user_id\":\"1\",\"service_id\":\"106\"," +
//                        "\"service_user_id\":\"1\",\"message\":\"test\"," +
//                        "\"appointmentTime\":\"22/12/2022T01:02:00\"}")
//                .accept("application/json"))
//                .andExpect(MockMvcResultMatchers.status().isAccepted());
//    }

    @Test
    void scheduleMeetingFail() throws Exception {

        mockMvc.perform(post("/service/contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"user_id\":\"iamdv43@gmail.com\",\"service_id\":\"106\"," +
                        "\"service_user_id\":\"100\",\"message\":\"test\"," +
                        "\"appointmentTime\":\"22/12/2022T01:02:00\"}")
                .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

//    @Test
//    void sendConfirmation() throws Exception {
//
//        mockMvc.perform(get("/servicecontact/confirm?serviceid=106&date=22/12/2022T01:02:00&useremail=iamdv43@gmail.com")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept("application/json"))
//                .andExpect(MockMvcResultMatchers.status().isAccepted());
//    }

    @Test
    void sendConfirmationFail() throws Exception {

        mockMvc.perform(get("/servicecontact/confirm?serviceid=&date=22/12/2022T01:02:00&useremail=iamdv43@gmail.com")
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }
}