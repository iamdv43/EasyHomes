package com.group24.easyHomes.service;

import com.group24.easyHomes.model.TokenValidation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final TokenValidationService tokenValidationService;

    @Autowired
    private final Environment env;

    @Transactional
    public String confirmToken(String token){
        TokenValidation tokenValidation = tokenValidationService.getToken(token).orElseThrow(() ->
                new IllegalStateException(env.getProperty("token.not.found")));

        if(tokenValidation.getVerifiedTime() != null){
            return env.getProperty("email.verified");
        }

        if(tokenValidation.getEndTime().isBefore(LocalDateTime.now())){
            return env.getProperty("token.expired");
        }

        tokenValidationService.setVerifiedAt(token);

        appUserService.enableAppUser(tokenValidation.getAppUser().getUsername());

        return "Confirmed";
    }
}
