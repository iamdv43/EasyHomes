package com.group24.easyHomes.controller;

import com.group24.easyHomes.dto.ServiceDTO;
import com.group24.easyHomes.mappers.ServiceToServiceDTO;
import com.group24.easyHomes.model.*;
import com.group24.easyHomes.repository.AppUserRepository;
import com.group24.easyHomes.service.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/service",produces = "application/json")
@CrossOrigin(origins = "*", maxAge = 3600)
public class  ServiceController {

    @Autowired
    private ServicesService service;

    @Autowired
    private ServiceToServiceDTO serviceToServiceDTO;

    @Autowired
    private AppUserRepository userRepository;

    @GetMapping("/services")
    public ResponseEntity<List<Services>> list(){
        return new ResponseEntity<>(service.listAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/services",consumes = {"application/json"},produces ={"application/json"})
    public ResponseEntity<Services> addService(@RequestBody ServiceDTO serviceDTO)
{
        try {
            Services services = serviceToServiceDTO.convert(serviceDTO);
            AppUser user = userRepository.getById(serviceDTO.getUser_id());
            String name = null ;
            if( user!= null)
            {
                name = user.getFirstName() + " " + user.getLastName();
            }
            services.setUser_name(name);
            service.addService(services);
            return new ResponseEntity<>(services,HttpStatus.CREATED) ;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/services/{serviceId}")
    public ResponseEntity<HttpStatus>  removeService(@PathVariable Long serviceId)
    {
        if("SUCCESS".equalsIgnoreCase(service.delete(serviceId))) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }


    @PutMapping(value = "/services/{serviceId}/update",consumes = {"application/json"},produces ={"application/json"})
    public ResponseEntity<Services>  updateProperty(@PathVariable Long serviceId, @RequestBody ServiceDTO serviceDTO)
    {
        Services services = serviceToServiceDTO.convert(serviceDTO);
        return new ResponseEntity<>(service.updateService(serviceId,services),HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/services/filter",consumes = {"application/json"},produces ={"application/json"})
    public ResponseEntity<List<Services>> filterServices(@RequestBody ServicesListQuery servicesListQuery)
    {
        return new ResponseEntity<>(service.filterServices(servicesListQuery),HttpStatus.OK);
    }
}
