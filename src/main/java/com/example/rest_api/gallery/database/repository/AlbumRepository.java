package com.example.rest_api.gallery.database.repository;

import com.example.rest_api.gallery.database.model.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {
    // Fetch all albums owned by a specific user
    List<AlbumEntity> findByOwnerId(Long userId);

    // Search albums by title (case-insensitive)
    List<AlbumEntity> findByTitleContainingIgnoreCase(String title);

    // Fetch all albums owned by a specific user
    List<AlbumEntity> findAllByOwnerId(Long ownerId);
}
