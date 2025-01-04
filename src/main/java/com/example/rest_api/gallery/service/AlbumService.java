package com.example.rest_api.gallery.service;

import com.example.rest_api.gallery.database.model.AlbumEntity;
import com.example.rest_api.gallery.database.repository.AlbumRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Transactional
    public List<AlbumEntity> getAllAlbums() {
        return albumRepository.findAll();
    }

    @Transactional
    public AlbumEntity getAlbumById(Long albumId) {
        return albumRepository.findById(albumId).orElse(null);
    }

    @Transactional
    public List<AlbumEntity> getAlbumsByOwner(Long ownerId) {
        return albumRepository.findAllByOwnerId(ownerId);
    }

    @Transactional
    public List<AlbumEntity> searchAlbums(String title) {
        return albumRepository.findByTitleContainingIgnoreCase(title);
    }

    public AlbumEntity saveAlbum(AlbumEntity album) {
        album.setCreatedAt(LocalDateTime.now());
        return albumRepository.save(album);
    }
}
