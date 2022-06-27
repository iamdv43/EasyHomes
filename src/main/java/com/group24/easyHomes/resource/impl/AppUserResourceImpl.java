package com.group24.easyHomes.resource.impl;

import com.group24.easyHomes.dto.AppUserResourceDTO;
import com.group24.easyHomes.model.AppUser;
import com.group24.easyHomes.repository.AppUserRepository;
import com.group24.easyHomes.repository.AppUserRoleRepository;
import com.group24.easyHomes.security.config.JwtTokenProvider;
import com.group24.easyHomes.service.AppUserService;
import com.group24.easyHomes.service.SendMailService;
import com.group24.easyHomes.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class AppUserResourceImpl {

    private final AppUserService appUserService;
    private final SendMailService sendMailService;

    private static final Logger log = LoggerFactory.getLogger(AppUserResourceImpl.class);

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final JwtTokenProvider tokenProvider;

    @Autowired
    private final AppUserRoleRepository roleRepository;

    @Autowired
    private final AppUserRepository userRepository;

    @Autowired
    private final AppUserResourceDTO appUserResourceDTO;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@RequestBody AppUser user) {
        log.info("UserResourceImpl : register");
        JSONObject jsonObject = new JSONObject();
        try {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            user.setRole(roleRepository.findByName(ConstantUtils.USER.toString()));

            if(userRepository.findByEmail(user.getEmail()).isEmpty()){
                AppUser savedUser = userRepository.saveAndFlush(user);

                String token =  appUserService.signUpUser(savedUser);

                String link = appUserResourceDTO.link + token;

                String message = appUserResourceDTO.message + link;

                String subject = appUserResourceDTO.subject;

                sendMailService.send(savedUser.getEmail(), message,subject );
                jsonObject.put("message", savedUser.getFirstName() + " saved successfully");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
            }
            else{
                jsonObject.put("message", "Email is already exists.");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.UNAUTHORIZED);
            }

// ----------------------------------------------------
//            String link = "https://easthomes-develop.herokuapp.com/user/confirm?token=" + token;
// ----------------------------------------------------
        } catch (JSONException e) {
            try {
                jsonObject.put("exception", e.getMessage());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> authenticate(@RequestBody AppUser user) {
        log.info("UserResourceImpl : authenticate");
        JSONObject jsonObject = new JSONObject();
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            if (authentication.isAuthenticated()) {
                String email = user.getEmail();
                Long userId = userRepository.findByEmail(email).get().getId();
                String firstName = userRepository.findById(userId).get().getFirstName();
                jsonObject.put("name", authentication.getName());
                jsonObject.put("authorities", authentication.getAuthorities());
                 jsonObject.put("userId", userId);
                 jsonObject.put("username", firstName);
                jsonObject.put("token", tokenProvider.createToken(email, userRepository.findByEmail(email).get().getRole()));
                return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);
            }
        } catch (JSONException e) {
            try {
                jsonObject.put("exception", e.getMessage());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return new ResponseEntity<>(appUserResourceDTO.loginErrorMessage, HttpStatus.FORBIDDEN);
        }
        return null;
    }
}
