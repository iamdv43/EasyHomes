package com.group24.easyHomes.resource.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group24.easyHomes.model.AppUser;
import com.group24.easyHomes.model.AppUserRole;
import com.group24.easyHomes.repository.AppUserRepository;
import com.group24.easyHomes.repository.AppUserRoleRepository;
import com.group24.easyHomes.security.config.JwtTokenProvider;
import com.group24.easyHomes.service.AppUserService;
import com.group24.easyHomes.service.SendMailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AppUserResourceImplUnitTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @MockBean
    private AppUserService service;

    @MockBean
    private AppUserRepository userRepository;

    @MockBean
    private AppUserRoleRepository roleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private  SendMailService sendMailService;

    @MockBean
    private Authentication authentication;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @Test
    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
    public void register_Success() throws Exception {
        AppUser user = new AppUser( "test", "test","test@123", "test@gmail.com");
        AppUserRole role = new AppUserRole("USER");

        doReturn(role).when(roleRepository).findByName(any());
        doReturn(user).when(userRepository).saveAndFlush(any(AppUser.class));
        doReturn("$2a$10$5FpROrnL6AAk3.zlHpOXG.5Z").when(service).signUpUser(any());
        doNothing().when(sendMailService).send(any(),any(), any());

        MockHttpServletRequestBuilder request = post("/user/register");
        request= request.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user));
        mockMvc.perform(request).andExpect(status().isOk());
    }

//    @Test
//    @WithMockUser(username = "dv", password = "pwd", authorities = "USER")
//    public void authenticate_Success() throws Exception {
//        AppUser user = new AppUser( "test", "test","test@123", "test@gmail.com");
//        Optional<AppUser> optionalUser = Optional.of(user);
//
//        doReturn(true).when(authentication).isAuthenticated();
//        doReturn("").when(authentication).getName();
//        doReturn(null).when(authentication).getAuthorities();
//        doReturn(authentication).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
//        doReturn(optionalUser).when(userRepository).findByEmail(any());
//        doReturn("").when(tokenProvider).createToken(any(),any());
//
//        MockHttpServletRequestBuilder request = post("/user/authenticate");
//        request= request.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user));
//        mockMvc.perform(request).andExpect(status().isOk());
//    }



}

