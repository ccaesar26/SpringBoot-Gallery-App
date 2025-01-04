package com.example.rest_api.users.service;

import com.example.rest_api.users.database.model.RoleEntity;
import com.example.rest_api.users.database.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public RoleEntity save(RoleEntity role) {
        return this.roleRepository.save(role);
    }

    public Boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    public Boolean existsByNameAndAlbumId(String name, Long albumId) {
        return roleRepository.existsByNameAndAlbumId(name, albumId);
    }

    public Optional<List<RoleEntity>> findByNameAndAlbumId(String name, Long albumId) {
        return roleRepository.findByNameAndAlbumId(name, albumId);
    }

    public Optional<RoleEntity> findByNameAlbumIdAndUserId(String name, Long albumId, Long userId) {
        var roles = roleRepository.findByNameAndAlbumId(name, albumId);
        return roles.flatMap(roleEntities -> roleEntities.stream().filter(role -> role.getUsers().stream().anyMatch(user -> user.getId().equals(userId))).findFirst());
    }

    public List<RoleEntity> findAll() {
        return roleRepository.findAll();
    }

    public RoleEntity findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }
}
