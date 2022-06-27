package com.group24.easyHomes.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppUserResourceDTO {

    @Value("${email.verification.link}")
    public String link;

    @Value("${email.verification.message}")
    public String message;

    @Value("${email.verification.subject}")
    public String subject;

    @Value("${login.error}")
    public String loginErrorMessage;



}
