package com.group24.easyHomes.controller;

import com.group24.easyHomes.model.FavoriteProperty;
import com.group24.easyHomes.model.Property;
import com.group24.easyHomes.repository.FavoritePropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/favorite-property", produces = "application/json")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FavoritePropertyController {
    @Autowired
    FavoritePropertyRepository favoritePropertyRepository;

    @GetMapping("/favorites")
    public List<FavoriteProperty> getAllFavoriteProperties() {
        return favoritePropertyRepository.findAll();
    }

    @PostMapping("/add")
    public ResponseEntity<FavoriteProperty> newFavoriteProperty(@RequestBody FavoriteProperty favoriteProperty) {
        favoriteProperty = favoritePropertyRepository.save(favoriteProperty);
        return new ResponseEntity<>(favoritePropertyRepository.save(favoriteProperty), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteProperty>> getFavoriteProperties(@PathVariable Long userId) {
        return new ResponseEntity<>(favoritePropertyRepository.findByUserId(userId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{favoritePropertyId}")
    public ResponseEntity<HttpStatus> deleteFavoriteProperty(@PathVariable Long favoritePropertyId) {
        favoritePropertyRepository.deleteById(favoritePropertyId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
