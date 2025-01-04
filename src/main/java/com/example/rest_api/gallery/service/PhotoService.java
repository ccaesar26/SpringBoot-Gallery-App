package com.example.rest_api.gallery.service;

import com.example.rest_api.gallery.database.model.AlbumEntity;
import com.example.rest_api.gallery.database.model.PhotoEntity;
import com.example.rest_api.gallery.database.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PhotoService {
    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    // Save a list of photos for an album
    public void savePhotosForAlbum(List<MultipartFile> photos, AlbumEntity album) {
        for (MultipartFile photo : photos) {
            if (!photo.isEmpty()) {
                try {
                    PhotoEntity photoEntity = new PhotoEntity();
                    photoEntity.setName(photo.getOriginalFilename());
                    photoEntity.setImageType(photo.getContentType());
                    photoEntity.setImageData(photo.getBytes());
                    photoEntity.setAlbum(album);  // Associate with album

                    photoRepository.save(photoEntity);
                } catch (IOException e) {
                    throw new RuntimeException("Error while saving photo", e);
                }
            }
        }
    }

    // Get photos by album ID
    public List<PhotoEntity> getPhotosByAlbumId(Long albumId) {
        return photoRepository.findByAlbumId(albumId);
    }

    // Delete a photo by ID
    @Transactional
    public void deletePhoto(Long photoId) {
        photoRepository.deleteById(photoId);
    }

    // Get a single photo by ID
    public PhotoEntity getPhotoById(Long photoId) {
        return photoRepository.findById(photoId).orElseThrow();
    }

    public void addPhotoToAlbum(AlbumEntity album, MultipartFile photo) {
        if (!photo.isEmpty()) {
            try {
                PhotoEntity photoEntity = new PhotoEntity();
                photoEntity.setName(photo.getOriginalFilename());
                photoEntity.setImageType(photo.getContentType());
                photoEntity.setImageData(photo.getBytes());
                photoEntity.setAlbum(album);

                photoRepository.save(photoEntity);
            } catch (IOException e) {
                throw new RuntimeException("Error while saving photo", e);
            }
        }
    }
}
