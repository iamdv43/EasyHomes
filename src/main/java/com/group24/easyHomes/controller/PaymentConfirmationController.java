package com.group24.easyHomes.controller;

import com.group24.easyHomes.model.AppUser;
import com.group24.easyHomes.model.Services;
import com.group24.easyHomes.repository.AppUserRepository;
import com.group24.easyHomes.repository.ServiceRepository;
import com.group24.easyHomes.service.SendMailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;


@RestController
@CrossOrigin
@AllArgsConstructor
public class PaymentConfirmationController {


    @Autowired
    private final Environment env;

    @Autowired
    private final ServiceRepository serviceRepository;

    @Autowired
    private final AppUserRepository appUserRepository;

    @Autowired
    private final SendMailService sendMailService;


    @PostMapping(value = "/paymentreceipt/confirmation", consumes = {"application/json"},produces ={"application/json"})
    public ResponseEntity<String> paymentReceipt(@RequestBody Map<String, String> paymentDetails) throws ParseException {

        Services service = serviceRepository.findByServices(Long.valueOf(paymentDetails.get("service_id")));
        AppUser customer = appUserRepository.findByUserId(Long.valueOf(paymentDetails.get("user_id")));
        AppUser provider = appUserRepository.findByUserId(service.getUser_id());

        String emailBody = env.getProperty("email.payment.body");

        assert emailBody != null;
        String finalEmailBody = String.format(emailBody, service.getService_name(), paymentDetails.get("amount"),
                paymentDetails.get("dateTime"), service.getService_name(), service.getService_type(),
                service.getCost(), service.getDescription(), service.getAddress(), customer.getFirstName(), provider.getFirstName());

        String emailSubject = env.getProperty("email.payment.subject");

        sendMailService.send(customer.getEmail(), finalEmailBody, emailSubject);
        sendMailService.send(provider.getEmail(), finalEmailBody, emailSubject);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
