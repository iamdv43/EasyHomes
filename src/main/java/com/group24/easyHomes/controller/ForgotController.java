package com.group24.easyHomes.controller;

import com.group24.easyHomes.service.ForgotService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class ForgotController{

    private final ForgotService forgotService;

    @Autowired
    private Environment env;

    @PostMapping("/forgotpassword")
    public ResponseEntity<String> forgotpassword(@RequestBody Map<String, String> email){
        String verifyEmail = forgotService.verifyEmail(email.get("email"));
        if(verifyEmail.equals(env.getProperty("verify.success"))){
            return new ResponseEntity<>(verifyEmail, HttpStatus.ACCEPTED);
        }else{
            return new ResponseEntity<>(verifyEmail, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/otpverification")
    public ResponseEntity<String> otpverification(@RequestBody Map<String, String> otp){
        String verifyOTP = forgotService.verifyOTP(Integer.parseInt(otp.get("otp")));
        if(verifyOTP.equals(env.getProperty("verify.otp.success"))){
            return new ResponseEntity<>(verifyOTP, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(verifyOTP, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/newpassword")
    public ResponseEntity<String> newpassword(@RequestBody Map<String, String> userDetails){
        String changed = forgotService.newPassword(userDetails.get("email"), userDetails.get("password"));
        return new ResponseEntity<>(changed, HttpStatus.ACCEPTED);
    }

}
