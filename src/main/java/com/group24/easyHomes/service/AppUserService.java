package com.group24.easyHomes.service;

import com.group24.easyHomes.model.AppUser;
import com.group24.easyHomes.model.TokenValidation;
import com.group24.easyHomes.repository.AppUserRepository;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    @Autowired
    private final Environment env;

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenValidationService tokenValidationService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(Objects.requireNonNull(env.getProperty("user.not.found.message")),email)));
    }

    public String signUpUser(AppUser appUser){
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();

        if(userExists){
            String token = UUID.randomUUID().toString();

            TokenValidation tokenValidation = new TokenValidation(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(60*24),appUser);

            tokenValidationService.storeToken(tokenValidation);

            return token;
        }
        else{
            return null;
        }
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

    public AppUser getById(long id){
        return appUserRepository.findById(id).get();
    }

}
