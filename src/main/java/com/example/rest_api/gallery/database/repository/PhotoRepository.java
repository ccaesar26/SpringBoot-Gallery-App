package com.example.rest_api.gallery.database.repository;

import com.example.rest_api.gallery.database.model.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {
    // Fetch all photos in a specific gallery
    List<PhotoEntity> findByAlbumId(Long albumId);

    @Modifying
    @Query("DELETE FROM PhotoEntity p WHERE p.id = :photoId")
    void deleteById(@Param("photoId") Long photoId);
}
