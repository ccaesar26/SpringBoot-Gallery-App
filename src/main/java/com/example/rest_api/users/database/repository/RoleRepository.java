package com.example.rest_api.users.database.repository;

import com.example.rest_api.users.database.model.RoleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    public List<RoleEntity> findAllByName(String name);
    Boolean existsByName(String name);

    Optional<List<RoleEntity>> findByNameAndAlbumId(String name, Long albumId);

    Boolean existsByNameAndAlbumId(String name, Long albumId);

    @Modifying
    @Query(value = "select * from roles where album_id = :albumId", nativeQuery = true)
    List<RoleEntity> findAllByAlbumId(Long albumId);
}
