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

import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin
@AllArgsConstructor
public class ServiceAppointmentController {

    @Autowired
    private final Environment env;

    @Autowired
    private final ServiceRepository serviceRepository;

    @Autowired
    private final AppUserRepository appUserRepository;

    @Autowired
    private final SendMailService sendMailService;


    @PostMapping(value = "/service/contact", consumes = {"application/json"},produces ={"application/json"})
    public ResponseEntity<String> servicecontact(@RequestBody Map<String, String> scheduleMeetingDetails) {

        try{
            Services serviceDetails = serviceRepository.findByServices(Long.valueOf(scheduleMeetingDetails.get("service_id")));
            AppUser serviceOwner = appUserRepository.findByUserId(Long.valueOf((scheduleMeetingDetails.get("service_user_id"))));
            AppUser interestedUser = appUserRepository.findByUserId(Long.valueOf((scheduleMeetingDetails.get("user_id"))));

            String contentHTML = env.getProperty("email.meeting.body");
            String datetime = scheduleMeetingDetails.get("appointmentTime");
            String date = datetime.substring(0, 10);
            String time = datetime.substring(11, 16);
            String confirmLink = env.getProperty("email.confirm.link");

            assert confirmLink != null;
            String finalLink = String.format(confirmLink, serviceDetails.getService_id(), datetime,
                    interestedUser.getEmail());

            assert contentHTML != null;
            String content = String.format(contentHTML, serviceOwner.getFirstName(), interestedUser.getFirstName(),
                    serviceDetails.getService_name(), serviceDetails.getService_type(), serviceDetails.getCost(),
                    serviceDetails.getDescription(),
                    serviceDetails.getAddress(), interestedUser.getFirstName() + " " + interestedUser.getLastName(),
                    interestedUser.getEmail(), scheduleMeetingDetails.get("message"), date + " " + time, finalLink);

            String subjectTemp = env.getProperty("email.meeting.subject");

            assert subjectTemp != null;
            String subject = String.format(subjectTemp, serviceDetails.getService_name());

            sendMailService.send(serviceOwner.getEmail(), content, subject);

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }


    @GetMapping(path = "/servicecontact/confirm")
    public ResponseEntity<String> confirm(@RequestParam("serviceid") String serviceid,
                          @RequestParam("date") String date,
                          @RequestParam("useremail") String useremail){

        try{
            Services serviceDetails = serviceRepository.findByServices(Long.valueOf(serviceid));
            AppUser interestedUser = appUserRepository.findByUserEmail(useremail);


            String content = String.format(Objects.requireNonNull(env.getProperty("email.confirm.body")),
                    interestedUser.getFirstName(), serviceDetails.getService_name(),
                    date.substring(0, 10) + " " + date.substring(11, 16));

            sendMailService.send(useremail, content, env.getProperty("email.confirm.subject"));

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}

