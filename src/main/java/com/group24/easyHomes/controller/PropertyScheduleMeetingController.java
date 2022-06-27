package com.group24.easyHomes.controller;

import com.group24.easyHomes.model.AppUser;
import com.group24.easyHomes.model.Property;
import com.group24.easyHomes.repository.AppUserRepository;
import com.group24.easyHomes.repository.PropertyRepository;
import com.group24.easyHomes.service.SendMailService;
import lombok.AllArgsConstructor;
import org.hibernate.jdbc.Expectation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin
@AllArgsConstructor
public class PropertyScheduleMeetingController {

    @Autowired
    private final Environment env;

    @Autowired
    private final PropertyRepository propertyRepository;

    @Autowired
    private final AppUserRepository appUserRepository;

    @Autowired
    private final SendMailService sendMailService;


    @PostMapping(value = "/property/owner/contact", consumes = {"application/json"},produces ={"application/json"})
    public ResponseEntity<String> propertyownercontact(@RequestBody Map<String, String> scheduleMeetingDetails) throws ParseException {

        try{
            Property propertyDetails = propertyRepository.findByProperty(Integer.parseInt(scheduleMeetingDetails.get("property_id")));
            AppUser serviceOwner = appUserRepository.findByUserId(Long.valueOf((scheduleMeetingDetails.get("property_user_id"))));
            AppUser interestedUser = appUserRepository.findByUserId(Long.valueOf((scheduleMeetingDetails.get("user_id"))));

            String contentHTML = env.getProperty("email.property.meeting.body");
            String datetime = scheduleMeetingDetails.get("appointmentTime");
            String date = datetime.substring(0, 10);
            String time = datetime.substring(11, 16);
            String confirmLink = env.getProperty("email.property.confirm.link");

            assert confirmLink != null;
            String finalLink = String.format(confirmLink, propertyDetails.getProperty_id(), datetime,
                    interestedUser.getEmail());

            assert contentHTML != null;
            String content = String.format(contentHTML, serviceOwner.getFirstName(), interestedUser.getFirstName(),
                    propertyDetails.getProperty_name(), propertyDetails.getRent(),
                    interestedUser.getFirstName() + " " + interestedUser.getLastName(),
                    interestedUser.getEmail(), scheduleMeetingDetails.get("message"), date + " " + time, finalLink);

            String subjectTemp = env.getProperty("email.property.meeting.subject");

            assert subjectTemp != null;
            String subject = String.format(subjectTemp, propertyDetails.getProperty_name());

            sendMailService.send(serviceOwner.getEmail(), content, subject);

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }


    @GetMapping(path = "/property/contact/confirm")
    public ResponseEntity<String> confirm(@RequestParam("propertyid") String propertyid,
                          @RequestParam("date") String date,
                          @RequestParam("useremail") String useremail){
        try{
            Property propertyDetails = propertyRepository.findByProperty(Integer.parseInt(propertyid));
            AppUser interestedUser = appUserRepository.findByUserEmail(useremail);


            String content = String.format(Objects.requireNonNull(env.getProperty("email.property.confirm.body")),
                    interestedUser.getFirstName(), propertyDetails.getProperty_name(),
                    date.substring(0, 10) + " " + date.substring(11, 16));

            sendMailService.send(useremail, content, env.getProperty("email.property.confirm.subject"));

            return new ResponseEntity<>( "Confirmed",HttpStatus.ACCEPTED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }
}
