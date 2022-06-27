package com.group24.easyHomes.controller;

import com.group24.easyHomes.model.FavoriteProperty;
import com.group24.easyHomes.model.FavoriteService;
import com.group24.easyHomes.repository.FavoriteServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/favorite-service", produces = "application/json")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FavoriteServiceController {

    @Autowired
    FavoriteServiceRepository favoriteServiceRepository;

    @GetMapping("/favorites")
    public List<FavoriteService> getAllFavoriteServices() {
        return favoriteServiceRepository.findAll();
    }

    @PostMapping("/add")
    public ResponseEntity<FavoriteService> newFavoriteService(@RequestBody FavoriteService favoriteService) {
        favoriteService = favoriteServiceRepository.save(favoriteService);
        return new ResponseEntity<>(favoriteServiceRepository.save(favoriteService), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteService>> getFavoriteServices(@PathVariable Long userId) {
        return new ResponseEntity<>(favoriteServiceRepository.findByUserId(userId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{favoriteServiceId}")
    public ResponseEntity<HttpStatus> deleteFavoriteService(@PathVariable Long favoriteServiceId) {
        favoriteServiceRepository.deleteById(favoriteServiceId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
