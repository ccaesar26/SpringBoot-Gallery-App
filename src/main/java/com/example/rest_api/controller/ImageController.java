package com.example.rest_api.controller;

import com.example.rest_api.gallery.database.model.PhotoEntity;
import com.example.rest_api.gallery.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ImageController {

    private final PhotoService photoService;

    @Autowired
    public ImageController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/images/{photoId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long photoId) {
        PhotoEntity photo = photoService.getPhotoById(photoId);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(photo.getImageType()))
                .body(photo.getImageData());
    }
}
